package com.comino.mav.control;

import java.util.List;
import java.util.Map;

import org.mavlink.messages.MAVLinkMessage;

import com.comino.msp.main.control.listener.IMSPModeChangedListener;
import com.comino.msp.model.DataModel;
import com.comino.msp.model.segment.Message;

public interface IMAVController {

	public boolean connect();
	public boolean close();
	public boolean start();
	public boolean stop();

	public boolean isSimulation();
	public boolean isConnected();
	public boolean isCollecting();

	public DataModel getCurrentModel();
	public List<DataModel> getModelList();


	public List<Message> getMessageList();

	public Map<Class<?>,MAVLinkMessage> getMavLinkMessageMap();

	public boolean sendMAVLinkCmd(int command, float...params);
	public boolean sendMSPLinkCmd(int command, float...params);

	public void addModeChangeListener(IMSPModeChangedListener listener);


}
