/****************************************************************************
 *
 *   Copyright (c) 2017,2018 Eike Mansfeld ecm@gmx.de. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 ****************************************************************************/

package com.comino.mav.mavlink;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.channels.ByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.messages.MAV_SEVERITY;
import org.mavlink.messages.lquac.msg_command_ack;
import org.mavlink.messages.lquac.msg_statustext;
import org.mavlink.messages.lquac.msg_timesync;

import com.comino.mav.comm.IMAVComm;
import com.comino.mav.control.IMAVCmdAcknowledge;
import com.comino.mav.mavlink.plugins.MAVLinkPluginBase;
import com.comino.msp.execution.control.listener.IMAVLinkListener;
import com.comino.msp.execution.control.listener.IMAVMessageListener;
import com.comino.msp.log.MSPLogger;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.segment.LogMessage;
import com.comino.msp.model.segment.Status;
import com.comino.msp.utils.MSPPluginHelper;

public class MAVLinkToModelParser {


	private static int TIME_SYNC_CYCLE_MS = 1000;
	private static double OFFSET_AVG_ALPHA = 0.6d;

	private DataModel model;
	private MSPLogger logger = null;

	private HashMap<Class<?>, MAVLinkMessage> mavList = null;

	private IMAVComm link = null;

	private HashMap<Class<?>, List<IMAVLinkListener>> listeners = null;

	private List<IMAVLinkListener> mavListener = null;
	private List<IMAVMessageListener> msgListener = null;

	private long time_offset_ns = 0;

	private LogMessage lastMessage = null;

	private long time_sync_cycle;

	private IMAVCmdAcknowledge cmd_ack = null;

	public MAVLinkToModelParser(DataModel model, IMAVComm link) {

		this.model = model;
		this.link = link;
		this.mavList = new HashMap<Class<?>, MAVLinkMessage>();

		this.mavListener = new ArrayList<IMAVLinkListener>();
		this.msgListener = new ArrayList<IMAVMessageListener>();

		model.sys.setStatus(Status.MSP_READY, true);

		listeners = new HashMap<Class<?>, List<IMAVLinkListener>>();

		registerPlugins();


		registerListener(msg_command_ack.class, new IMAVLinkListener() {

			@Override
			public void received(Object o) {

				msg_command_ack ack = (msg_command_ack) o;

				if(cmd_ack!=null) {
					cmd_ack.received(ack.command, ack.result);
					cmd_ack = null;
				}
				if(model.sys.isStatus(Status.MSP_PROXY)) {
					return;
				}

				if(logger==null)
					logger = MSPLogger.getInstance();
				switch (ack.result) {
				case 1:
					logger.writeLocalMsg("Command " + ack.command + " failed",MAV_SEVERITY.MAV_SEVERITY_WARNING);
					break;
				case 2:
					logger.writeLocalMsg("Command " + ack.command + " denied",MAV_SEVERITY.MAV_SEVERITY_WARNING);
					break;
				case 3:
					logger.writeLocalMsg("Command " + ack.command + " is unsupported",MAV_SEVERITY.MAV_SEVERITY_WARNING);
				default:
				}
			}

		});

		registerListener(msg_statustext.class, new IMAVLinkListener() {
			@Override
			public void received(Object o) {
				msg_statustext msg = (msg_statustext) o;
				LogMessage m = new LogMessage();
				m.msg = (new String(msg.text)).trim();
				m.tms = model.sys.getSynchronizedPX4Time_us();
				m.severity = msg.severity;
				model.msg.set(m);
				writeMessage(m);
			}
		});


		registerListener(msg_timesync.class, new IMAVLinkListener() {

			@Override
			public void received(Object o) {
				try {

					if(!link.isSerial())
						return;

					long now_ns = System.currentTimeMillis() * 1000000L;
					msg_timesync sync = (msg_timesync) o;

					if (sync.tc1 == 0) {
						msg_timesync sync_s = new msg_timesync(255, 1);
						sync_s.tc1 = now_ns;
						sync_s.ts1 = sync.ts1;
						link.write(sync_s);
						return;

					} else if (sync.tc1 > 0) {
						long offset_ns = (sync.ts1 + now_ns - sync.tc1 * 2L) / 2L;
						long dt = time_offset_ns - offset_ns;
						// System.out.println("TS1="+sync.ts1+" TC="+sync.tc1+"
						// TO="+time_offset_ns+" OFS="+offset_ns+"
						// PX4="+now_ns+" DT="+Math.abs(dt/1e9d));
						if (dt > 10000000L || dt < -10000000L) {
							time_offset_ns = offset_ns;
							System.out.println("[sys]  Clock skew detected: " + dt);
							model.sys.tms = model.sys.getSynchronizedPX4Time_us();
						} else {
							time_offset_ns = (long) (OFFSET_AVG_ALPHA * offset_ns
									+ (1.0d - OFFSET_AVG_ALPHA) * time_offset_ns);
						}
						model.sys.t_offset_ns = time_offset_ns;
						//		System.out.println("OFFSET="+model.sys.t_offset_ns+":"+sync.ts1);
						// PX4="+model.sys.getSynchronizedPX4Time_us());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		System.out.println("MAVMSP parser: " + listeners.size() + " MAVLink messagetypes registered");

		model.sys.tms = System.currentTimeMillis() * 1000;

	}

	private void registerPlugins() {
		System.out.println("Loading MAVLinkMessage plugins...");
		try {
			ArrayList<Class<?>> classes = MSPPluginHelper.getClassesForPackage(this.getClass().getPackage().getName()+".plugins");
			classes.forEach((c) -> {
				if(c.getName().endsWith("Plugin")) {
					try {
						Constructor<?> constructor = c.getConstructor();
						MAVLinkPluginBase plugin = (MAVLinkPluginBase)constructor.newInstance();
						plugin.setDataModel(model);
						registerListener(plugin.getMessageClass(),plugin);
					} catch (Exception e) { e.printStackTrace(); }
				}
			});
		} catch (ClassNotFoundException e) { e.printStackTrace(); }
	}

	public void addMAVLinkListener(IMAVLinkListener listener) {
		mavListener.add(listener);
	}

	public void addMAVMessagekListener(IMAVMessageListener listener) {
		msgListener.add(listener);
	}

	public Map<Class<?>, MAVLinkMessage> getMavLinkMessageMap() {
		return mavList;
	}

	public void start(ByteChannel channel) {
		System.err.println("Error: Deprecated ParserWorker");
	}

	public boolean isConnected() {
		if (!model.sys.isStatus(Status.MSP_CONNECTED)) {
			model.clear();
			return false;
		}
		return model.sys.isStatus(Status.MSP_CONNECTED);
	}

	public void setCmdAcknowledgeListener(IMAVCmdAcknowledge ack) {
		this.cmd_ack = ack;
	}

	public void writeMessage(LogMessage m) {
		if (lastMessage == null || lastMessage.tms < m.tms) {
			System.out.println(m.msg);
			if (msgListener != null) {
				for (IMAVMessageListener msglistener : msgListener)
					msglistener.messageReceived(m);
			}
		}
	}

	private void registerListener(Class<?> clazz, IMAVLinkListener listener) {
		List<IMAVLinkListener> listenerList = null;
		if (!listeners.containsKey(clazz)) {
			listenerList = new ArrayList<IMAVLinkListener>();
			listeners.put(clazz, listenerList);
		} else
			listenerList = listeners.get(clazz);
		listenerList.add(listener);
	}

	public void parseMessage(MAVLinkMessage msg) throws IOException {

		if (msg != null) {
			model.sys.setStatus(Status.MSP_CONNECTED, true);
			model.sys.tms = model.sys.getSynchronizedPX4Time_us();

			try {
				if (mavListener != null && mavListener.size() > 0) {
					for (IMAVLinkListener mavlistener : mavListener)
						mavlistener.received(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				mavList.put(msg.getClass(), msg);
				final List<IMAVLinkListener> listenerList = listeners.get(msg.getClass());
				if (listenerList != null && listenerList.size() > 0) {
					for (IMAVLinkListener listener : listenerList)
						listener.received(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if ((System.currentTimeMillis() - time_sync_cycle) > TIME_SYNC_CYCLE_MS && TIME_SYNC_CYCLE_MS > 0) {

			if(!link.isSerial())
				return;

			time_sync_cycle = System.currentTimeMillis();
			msg_timesync sync_s = new msg_timesync(255, 1);
			sync_s.tc1 = 0;
			sync_s.ts1 = System.currentTimeMillis() * 1000000L;
			link.write(sync_s);
		}

	}
}
