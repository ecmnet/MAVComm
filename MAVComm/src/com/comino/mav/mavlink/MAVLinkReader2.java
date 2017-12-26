

package com.comino.mav.mavlink;

import java.util.Arrays;
import java.util.Vector;

import org.mavlink.IMAVLinkCRC;
import org.mavlink.IMAVLinkMessage;
import org.mavlink.MAVLinkCRC;
import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.messages.MAVLinkMessageFactory;


public class MAVLinkReader2 {


	private static final byte MAVLINK_IFLAG_SIGNED = 0x01;

	private static int MAVLINK_SIGNATURE_BLOCK_LEN = 13;
	private static int MAVLINK_MAX_PAYLOAD_SIZE = 255;


	private enum t_parser_state  {
		MAVLINK_PARSE_STATE_IDLE,
		MAVLINK_PARSE_STATE_GOT_STX,
		MAVLINK_PARSE_STATE_GOT_LENGTH,
		MAVLINK_PARSE_STATE_GOT_INCOMPAT_FLAGS,
		MAVLINK_PARSE_STATE_GOT_COMPAT_FLAGS,
		MAVLINK_PARSE_STATE_GOT_SEQ,
		MAVLINK_PARSE_STATE_GOT_SYSID,
		MAVLINK_PARSE_STATE_GOT_COMPID,
		MAVLINK_PARSE_STATE_GOT_MSGID1,
		MAVLINK_PARSE_STATE_GOT_MSGID2,
		MAVLINK_PARSE_STATE_GOT_MSGID3,
		MAVLINK_PARSE_STATE_GOT_PAYLOAD,
		MAVLINK_PARSE_STATE_GOT_CRC1,
		MAVLINK_PARSE_STATE_GOT_BAD_CRC1,
		MAVLINK_PARSE_STATE_SIGNATURE_WAIT
	};

	private enum mavlink_framing_t {
		MAVLINK_FRAMING_INCOMPLETE,
		MAVLINK_FRAMING_OK,
		MAVLINK_FRAMING_BAD_CRC,
		MAVLINK_FRAMING_BAD_SIGNATURE,
		MAVLINK_FRAMING_BAD_SEQUENCE,
	};

	private volatile t_parser_state state = t_parser_state.MAVLINK_PARSE_STATE_IDLE;
	private volatile RxMsg rxmsg          = new RxMsg();


	private final int[] lastPacket = new int[256];

	private int packet_lost=0;

	/**
	 * MAVLink messages received
	 */
	private final Vector<MAVLinkMessage> packets = new Vector<MAVLinkMessage>();

	private volatile int lengthToRead = 0;
	private volatile boolean noCRCCheck = false;


	public MAVLinkReader2(int id) {
		this(id,false);
	}

	public MAVLinkReader2(int id, boolean noCRCCheck) {
		this.noCRCCheck = noCRCCheck;
		for (int i = 0; i < lastPacket.length; i++) {
			lastPacket[i] = -1;
		}
		if(noCRCCheck)
			System.out.println("MAVLinkReader2 "+id+" started without CRC");
		else
		System.out.println("MAVLinkReader2 "+id+" started");
	}

	/**
	 * @return the number of unread messages
	 */
	public int nbUnreadMessages() {
		return packets.size();
	}


	/**
	 * @return the protocol start tag
	 */
	public int getProtocol() {
		return rxmsg.start;
	}



	public void put(byte buf[],int len) {
		synchronized(this) {
			for(int i=0;i<len;i++)
				readMavLinkMessageFromBuffer(buf[i]);
		}
	}


	public MAVLinkMessage getNextMessage(byte buf[],int len) {
		for(int i=0;i<len;i++)
			readMavLinkMessageFromBuffer(buf[i]);

		MAVLinkMessage msg = null;
		if(packets.size()>0) {
			msg = packets.remove(0);
		}
		return msg;
	}

	public MAVLinkMessage getNextMessage() {
		MAVLinkMessage msg = null;
		if(packets.size()>0) {
			msg = packets.remove(0);
		}
		return msg;
	}



	public  boolean readMavLinkMessageFromBuffer(int v) {
		try {

			int c = (v & 0x00FF);
			//	System.out.println(state+":"+byteToHex(c));

			switch(state) {
			case MAVLINK_PARSE_STATE_IDLE:
				if((byte)c==IMAVLinkMessage.MAVPROT_PACKET_START_V20) {
					rxmsg.clear();
					rxmsg.start = IMAVLinkMessage.MAVPROT_PACKET_START_V20;
					state = t_parser_state.MAVLINK_PARSE_STATE_GOT_STX;
					return true;
				}
				if((byte)c==IMAVLinkMessage.MAVPROT_PACKET_START_V10) {
					rxmsg.clear();
					rxmsg.start = IMAVLinkMessage.MAVPROT_PACKET_START_V10;
					state = t_parser_state.MAVLINK_PARSE_STATE_GOT_STX;
					return true;
				}
				break;
			case MAVLINK_PARSE_STATE_GOT_STX:
				rxmsg.len=c; lengthToRead=0;
				rxmsg.crc = MAVLinkCRC.crc_accumulate((byte)c, rxmsg.crc);
				if(rxmsg.start==IMAVLinkMessage.MAVPROT_PACKET_START_V10)
					state = t_parser_state.MAVLINK_PARSE_STATE_GOT_COMPAT_FLAGS;
				else
					state = t_parser_state.MAVLINK_PARSE_STATE_GOT_LENGTH;
				break;
			case MAVLINK_PARSE_STATE_GOT_LENGTH:
				rxmsg.incompat = c;
				rxmsg.crc = MAVLinkCRC.crc_accumulate((byte)c, rxmsg.crc);
				state = t_parser_state.MAVLINK_PARSE_STATE_GOT_INCOMPAT_FLAGS;
				break;
			case MAVLINK_PARSE_STATE_GOT_INCOMPAT_FLAGS:
				rxmsg.compat = c;
				rxmsg.crc = MAVLinkCRC.crc_accumulate((byte)c, rxmsg.crc);
				state = t_parser_state.MAVLINK_PARSE_STATE_GOT_COMPAT_FLAGS;
				break;
			case MAVLINK_PARSE_STATE_GOT_COMPAT_FLAGS:
				rxmsg.packet = c;
				rxmsg.crc = MAVLinkCRC.crc_accumulate((byte)c, rxmsg.crc);
				state = t_parser_state.MAVLINK_PARSE_STATE_GOT_SEQ;
				break;
			case MAVLINK_PARSE_STATE_GOT_SEQ:
				rxmsg.sysId = c;
				rxmsg.crc = MAVLinkCRC.crc_accumulate((byte)c, rxmsg.crc);
				state = t_parser_state.MAVLINK_PARSE_STATE_GOT_SYSID;
				break;
			case MAVLINK_PARSE_STATE_GOT_SYSID:
				rxmsg.componentId = c;
				rxmsg.crc = MAVLinkCRC.crc_accumulate((byte)c, rxmsg.crc);
				state = t_parser_state.MAVLINK_PARSE_STATE_GOT_COMPID;
				break;
			case MAVLINK_PARSE_STATE_GOT_COMPID:
				rxmsg.msgId = c;
				rxmsg.crc = MAVLinkCRC.crc_accumulate((byte)c, rxmsg.crc);
				if(rxmsg.start==IMAVLinkMessage.MAVPROT_PACKET_START_V10)
					state = t_parser_state.MAVLINK_PARSE_STATE_GOT_MSGID3;
				else
					state = t_parser_state.MAVLINK_PARSE_STATE_GOT_MSGID1;
				break;
			case MAVLINK_PARSE_STATE_GOT_MSGID1:
				rxmsg.msgId |= c << 8;
				rxmsg.crc = MAVLinkCRC.crc_accumulate((byte)c, rxmsg.crc);
				state = t_parser_state.MAVLINK_PARSE_STATE_GOT_MSGID2;
				break;
			case MAVLINK_PARSE_STATE_GOT_MSGID2:
				rxmsg.msgId |= c << 16;
				rxmsg.crc = MAVLinkCRC.crc_accumulate((byte)c, rxmsg.crc);
				state = t_parser_state.MAVLINK_PARSE_STATE_GOT_MSGID3;
				break;
			case MAVLINK_PARSE_STATE_GOT_MSGID3:
				rxmsg.rawData[lengthToRead] = (byte)c;
				rxmsg.crc = MAVLinkCRC.crc_accumulate((byte)c, rxmsg.crc);
				if(++lengthToRead >= rxmsg.len)
					state = t_parser_state.MAVLINK_PARSE_STATE_GOT_PAYLOAD;
				break;
			case MAVLINK_PARSE_STATE_GOT_PAYLOAD:
				try {
					if (IMAVLinkCRC.MAVLINK_EXTRA_CRC)
						rxmsg.crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[rxmsg.msgId], rxmsg.crc);
				} catch(Exception e) {
					state = t_parser_state.MAVLINK_PARSE_STATE_GOT_BAD_CRC1;
				}

				if(c!=(rxmsg.crc & 0x00FF))
					state = t_parser_state.MAVLINK_PARSE_STATE_GOT_BAD_CRC1;
				else
					state = t_parser_state.MAVLINK_PARSE_STATE_GOT_CRC1;
				break;
			case MAVLINK_PARSE_STATE_GOT_BAD_CRC1:
			case MAVLINK_PARSE_STATE_GOT_CRC1:
				if ((state == t_parser_state.MAVLINK_PARSE_STATE_GOT_BAD_CRC1 || c != ((int)(rxmsg.crc >> 8) & 0x00FF)) && !noCRCCheck) {

					// CRC workaraounds
					if(rxmsg.msgId == 36 || rxmsg.msgId == 140 || rxmsg.msgId == 74 || rxmsg.msgId == 83 || rxmsg.msgId == 30
						|| rxmsg.msgId == 32 || rxmsg.msgId == 31 || rxmsg.msgId == 106 || rxmsg.msgId == 85 || rxmsg.msgId == 24
						|| rxmsg.msgId == 242|| rxmsg.msgId ==77 || rxmsg.msgId ==148)
						rxmsg.msg_received = mavlink_framing_t.MAVLINK_FRAMING_OK;
					else {
						rxmsg.msg_received = mavlink_framing_t.MAVLINK_FRAMING_BAD_CRC;
						//System.out.println("BadCRC: "+rxmsg.msgId+":"+rxmsg.len);
					}


				}
				else
					rxmsg.msg_received = mavlink_framing_t.MAVLINK_FRAMING_OK;

				if((rxmsg.incompat & MAVLINK_IFLAG_SIGNED)==MAVLINK_IFLAG_SIGNED) {
					rxmsg.msg_received = mavlink_framing_t.MAVLINK_FRAMING_INCOMPLETE;
					rxmsg.signature_wait = MAVLINK_SIGNATURE_BLOCK_LEN;
					state = t_parser_state.MAVLINK_PARSE_STATE_SIGNATURE_WAIT;
				}  else {
					state = t_parser_state.MAVLINK_PARSE_STATE_IDLE;
				}

				if(rxmsg.msg_received == mavlink_framing_t.MAVLINK_FRAMING_OK) {
					MAVLinkMessage msg = MAVLinkMessageFactory.getMessage(rxmsg.msgId, rxmsg.sysId, rxmsg.componentId, rxmsg.rawData);
					if(msg!=null && checkPacket(rxmsg.sysId,rxmsg.packet)) {
						msg.isValid = true;
						msg.packet = rxmsg.packet;
						packets.addElement(msg);
						//System.out.println("added: "+rxmsg.packet+":"+msg);
					} else {
						packet_lost++;
						//System.out.println(rxmsg);
					}
				} else {
					System.out.println(rxmsg);
					packet_lost++;
				}
				break;

			case MAVLINK_PARSE_STATE_SIGNATURE_WAIT:
				rxmsg.signature[MAVLINK_SIGNATURE_BLOCK_LEN-rxmsg.signature_wait] = (byte)c;
				rxmsg.signature_wait--;
				if ( rxmsg.signature_wait == 0) {
					// check signature here
					// ...
					state = t_parser_state.MAVLINK_PARSE_STATE_IDLE;
					if(rxmsg.msg_received == mavlink_framing_t.MAVLINK_FRAMING_OK) {
						MAVLinkMessage msg = MAVLinkMessageFactory.getMessage(rxmsg.msgId, rxmsg.sysId, rxmsg.componentId, rxmsg.rawData);
						if(msg!=null && checkPacket(rxmsg.sysId,rxmsg.packet)) {
							msg.packet = rxmsg.packet;
							packets.addElement(msg);
//							System.out.println("added: "+rxmsg.packet+":"+msg);
						} else {
							packet_lost++;
						}
					} else {
						packet_lost++;
					}
				}
				break;
			default: break;
			}
			return true;
		} catch(Exception io) {
			io.printStackTrace();
			state = t_parser_state.MAVLINK_PARSE_STATE_IDLE;
			return false;
		}
	}

	public int getLostPackages() {
		return packet_lost;
	}



	/**
	 * Check if we don't lost messages...
	 *
	 * @param sequence
	 *            current sequence
	 * @return true if we don't lost messages
	 */
	protected boolean checkPacket(int sysId, int packet) {
		boolean check = false;

		if(sysId!=1)
			return true;

		if (lastPacket[sysId] == -1) {
			// it is the first message read
			check = true;
		}


		//		if(packet==255) {
		//			packet = lastPacket[sysId] + 1;
		//			check = true;
		//		}

		if (lastPacket[sysId] < packet) {
			if (packet - lastPacket[sysId] == 1) {
				check = true;
			}
		}
		else
			// We have reached the max number (255) and restart to 0
			if (packet + 256 - lastPacket[sysId] == 1) {
				check = true;
			}


		lastPacket[sysId] = packet;
		return true;
	}

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes, int len) {
		char[] hexChars = new char[len * 2];
		for ( int j = 0; j <len; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static String byteToHex(int c) {
		int v = c & 0xFF;
		char[] hexChars = new char[2];
		hexChars[0] = hexArray[v >>> 4];
		hexChars[1] = hexArray[v & 0x0F];
		return new String(hexChars);
	}

	private class RxMsg {
		public int start;
		public int len;
		public int incompat;
		public int compat;
		public int packet;
		public int sysId;
		public int componentId;
		public int msgId;
		public int crc = MAVLinkCRC.crc_init();;
		public byte[] rawData = new byte[MAVLINK_MAX_PAYLOAD_SIZE+1];
		public byte[] signature = new byte[MAVLINK_SIGNATURE_BLOCK_LEN];

		public mavlink_framing_t msg_received;
		public int signature_wait = MAVLINK_SIGNATURE_BLOCK_LEN;

		public void clear() {
			start = 0;
			len = 0;
			incompat=0;
			compat=0;
			packet=0;
			sysId=0;
			componentId=0;
			msgId=0;
			signature_wait = MAVLINK_SIGNATURE_BLOCK_LEN;
			msg_received = mavlink_framing_t.MAVLINK_FRAMING_INCOMPLETE;
			crc= MAVLinkCRC.crc_init();
			Arrays.fill(rawData, (byte)0x00);
		}

		public String toString() {
			return" MSG "+msgId+" STX="+start+" LEN="+len+" PAK="+packet+" SYS="+sysId+" => " + msg_received;
		}

	}

}
