package org.mavlink;



import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.messages.MAVLinkMessageFactory;

/**
 * @author ghelle
 * @version $Rev: 20 $
 */
public class MAVLinkReader {

	/**
	 * Input stream
	 */
	private DataInputStream dis = null;

	public final static int RECEIVED_OFFSET = 10;

	/**
	 *
	 */
	public final static int RECEIVED_BUFFER_SIZE = 280;

	/**
	 *
	 */
	public final static int MAX_TM_SIZE = 280;

	/**
	 *
	 */
	private final byte[] receivedBuffer = new byte[RECEIVED_BUFFER_SIZE];

	/**
	 * Nb bytes received
	 */
	private int nbReceived = 0;

	/**
	 * Last sequence number received
	 */
	private final int[] lastPacket = new int[256];

	/**
	 * Read MAVLink V1.0 packets by default;
	 */
	private byte start = IMAVLinkMessage.MAVPROT_PACKET_START_V20;

	/**
	 * MAVLink messages received
	 */
	private final Vector<MAVLinkMessage> packets = new Vector<MAVLinkMessage>();

	/**
	 * True if we are reading a message
	 */
	private boolean messageInProgress = false;

	/**
	 * True if we have received the payload length
	 */
	private boolean lengthReceived = false;

	private int lengthToRead = 0;

	private int lostBytes = 0;

	private int badSequence = 0;

	private int badCRC = 0;

	private long nbMessagesReceived = 0;

	private long totalBytesReceived = 0;

	private final byte[] bytes = new byte[8192];

	private int offset = MAX_TM_SIZE;

	private int id;

	/**
	 * Constructor with MAVLink 1.0 by default and without stream. Must be used whith byte array read methods.
	 */
	public MAVLinkReader(int id) {
		// Issue 1 by BoxMonster44 : use correct packet start
		this((byte)IMAVLinkMessage.MAVPROT_PACKET_START_V20,id);
	}


	/**
	 * Constructor
	 *
	 * @param dis
	 *            Data input stream
	 * @param start
	 *            Start byte for MAVLink version
	 */
	public MAVLinkReader(DataInputStream dis, byte start) {
		this.dis = dis;
		this.start = start;
		for (int i = 0; i < lastPacket.length; i++) {
			lastPacket[i] = -1;
		}
	}

	/**
	 * Constructor for byte array read methods.
	 *
	 * @param start
	 *            Start byte for MAVLink version
	 */
	public MAVLinkReader(byte start, int id) {
		this.id = id;
		this.dis = null;
		this.start = start;
		for (int i = 0; i < lastPacket.length; i++) {
			lastPacket[i] = -1;
		}
		System.out.println("MAVLinkReader "+id+" started");
	}

	/**
	 * @return the number of unread messages
	 */
	public int nbUnreadMessages() {
		return packets.size();
	}






	public MAVLinkMessage getNextMessage(byte[] buffer, int len) throws IOException {
		MAVLinkMessage msg = null;

		if (packets.isEmpty() || len > 0 ) {
			for (int i = offset; i < len + offset; i++) {
				bytes[i] = buffer[i - offset];
			}

			dis = new DataInputStream(new ByteArrayInputStream(bytes, 0, len + offset));

			while (dis.available() >  lengthToRead+RECEIVED_OFFSET)
				readNextMessageWithoutBlocking();

			offset = dis.available();
			for (int j = 0; j < offset; j++) {
				bytes[j] = dis.readByte();
			}
			dis.close();
			dis = null;
		}
		if (!packets.isEmpty()) {
			msg = (MAVLinkMessage) packets.firstElement();
			packets.removeElementAt(0);
		}

		return msg;
	}

	/**
	 * Return next message. If bytes available, try to read it. Don't wait message is completed, it will be retruned nex time
	 *
	 * @return MAVLink message or null
	 */
	public MAVLinkMessage getNextMessageWithoutBlocking() {
		MAVLinkMessage msg = null;
		if (packets.isEmpty()) {
			readNextMessageWithoutBlocking();
		}
		if (!packets.isEmpty()) {
			msg = (MAVLinkMessage) packets.firstElement();
			packets.removeElementAt(0);
		}
		return msg;
	}


	/**
	 * @return The lostBytes
	 */
	public int getLostBytes() {
		return lostBytes;
	}

	/**
	 * @return The badSequence
	 */
	public int getBadSequence() {
		return badSequence;
	}

	/**
	 * @return The badCRC
	 */
	public int getBadCRC() {
		return badCRC;
	}

	/**
	 * @return The nbMessagesReceived
	 */
	public long getNbMessagesReceived() {
		return nbMessagesReceived;
	}

	/**
	 * @return The totalBytesReceived
	 */
	public long getTotalBytesReceived() {
		return totalBytesReceived;
	}

	/**
	 * Try to read next message. Can't be blocked on read
	 *
	 * @return true if data are valid
	 */
	protected boolean readNextMessageWithoutBlocking() {
		boolean validData = false;
		try {
			if (messageInProgress == false) {
				// Waiting for a new message
				if (dis==null || dis.available() == 0)
					return validData;
				receivedBuffer[nbReceived] = dis.readByte();
				totalBytesReceived++;
				if (receivedBuffer[nbReceived++] == start) {

					messageInProgress = true;
					if (dis.available() == 0)
						return validData;
					lengthToRead = receivedBuffer[nbReceived++] = dis.readByte();
					totalBytesReceived++;
					lengthToRead &= 0X00FF;
					lengthReceived = true;

					if (dis.available() < RECEIVED_OFFSET + lengthToRead)
						return validData;
					validData = readEndMessage();
					messageInProgress = false;
					lengthReceived = false;
					lengthToRead = 0;
					// restart buffer
					nbReceived = 0;
				}
				else {
					lostBytes++;
					nbReceived = 0;
					return validData;
				}
			}
			else {
				// Message in progress
				if (!lengthReceived) {
					if (dis.available() == 0)
						return validData;
					lengthToRead = receivedBuffer[nbReceived++] = dis.readByte();
					totalBytesReceived++;
					lengthToRead &= 0X00FF;
					lengthReceived = true;
				}
				if (dis.available() < RECEIVED_OFFSET + lengthToRead)
					return validData;
				validData = readEndMessage();
				messageInProgress = false;
				lengthReceived = false;
				lengthToRead = 0;
				// restart buffer
				lostBytes=0;
				nbReceived = 0;
			}
		}
		catch (Exception e) {
			nbReceived = 0;
			validData = false;
		}

		return validData;
	}

	/**
	 * Read the end of message after the start byte and the payload length. Called only if there are available character to read all the rest of
	 * message
	 *
	 * @return true if no error occurs
	 * @throws IOException
	 *             on read byte function...
	 */
	protected boolean readEndMessage() throws IOException, EOFException {
		boolean validData = false;
		int incompat;
		int compat;
		int tmp;
		int packet;
		int sysId;
		int componentId;
		int msgId;
		byte crcLow;
		byte crcHigh;
		byte[] rawData = null;
		MAVLinkMessage msg = null;

		incompat = receivedBuffer[nbReceived++] = dis.readByte();
		incompat &= 0X00FF;
		totalBytesReceived++;

		compat = receivedBuffer[nbReceived++] = dis.readByte();
		compat &= 0X00FF;
		totalBytesReceived++;

		packet = receivedBuffer[nbReceived++] = dis.readByte();
		packet &= 0X00FF;
		totalBytesReceived++;

		sysId = receivedBuffer[nbReceived++] = dis.readByte();
		sysId &= 0X00FF;
		totalBytesReceived++;

		componentId = receivedBuffer[nbReceived++] = dis.readByte();
		componentId &= 0X00FF;
		totalBytesReceived++;

		msgId = receivedBuffer[nbReceived++] = dis.readByte();
		msgId &= 0X00FF;
		totalBytesReceived++;

		tmp = receivedBuffer[nbReceived++] = dis.readByte();
		msgId |= (tmp & 0X00FF) << 8;
		totalBytesReceived++;

		tmp = receivedBuffer[nbReceived++] = dis.readByte();
		msgId |= (tmp & 0X00FF) << 16;
		totalBytesReceived++;

		rawData = readRawData(lengthToRead);

		crcLow = receivedBuffer[nbReceived++] = dis.readByte();
		totalBytesReceived++;
		crcHigh = receivedBuffer[nbReceived++] = dis.readByte();
		totalBytesReceived++;

		int crc = MAVLinkCRC.crc_calculate_decode(receivedBuffer, lengthToRead);
		if (IMAVLinkCRC.MAVLINK_EXTRA_CRC) {
			// CRC-EXTRA for Mavlink 1.0
			try {
				crc = MAVLinkCRC.crc_accumulate((byte) IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[msgId], crc);
			} catch(Exception e) { }
		}

		byte crcl = (byte) (crc & 0x00FF);
		byte crch = (byte) ((crc >> 8) & 0x00FF);
//		if ((crcl == crcLow) && (crch == crcHigh) ) {
			msg = MAVLinkMessageFactory.getMessage(msgId, sysId, componentId, rawData);
			if (msg != null) {
				msg.packet = packet;
				if (!checkPacket(sysId, packet)) {
					badSequence += 1;
				// System.err.println(id+" SEQ: MSG="+msgId+" "+bytesToHex(receivedBuffer,lengthToRead+12));
				}
				packets.addElement(msg);
				nbMessagesReceived++;
				validData=true;
			}
			else {
				System.err.println("ERROR creating message  Id=" + msgId);
				validData = false;
			}
//		}
//		else {
//			badCRC += 1;
////			 System.err.println(id+" CRC: MSG="+msgId+" "+bytesToHex(receivedBuffer,lengthToRead+12));
////			validData = false;
//		}
		// restart buffer
		lastPacket[sysId] = packet;
		nbReceived = 0;

		return validData;
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
		if (lastPacket[sysId] == -1) {
			// it is the first message read
			lastPacket[sysId] = packet;
			check = true;
		}
		else if (lastPacket[sysId] < packet) {
			if (packet - lastPacket[sysId] == 1) {
				// No message lost
				check = true;
			}
		}
		else
			// We have reached the max number (255) and restart to 0
			if (packet + 256 - lastPacket[sysId] == 1) {
				// No message lost
				check = true;
			}
		return check;
	}

	/**
	 * Read Payload bytes
	 *
	 * @param nb
	 *            Nb bytes to read
	 * @return Payload bytes
	 * @throws IOException
	 */
	private byte[] buffer = new byte[256];
	protected byte[] readRawData(int nb) throws IOException {
		Arrays.fill(buffer,(byte)0);
		int index = 0;
		/*
		 * while (dis.available() < nb) { ; }
		 */
		for (int i = 0; i < nb; i++) {
			receivedBuffer[nbReceived] = dis.readByte();
			totalBytesReceived++;
			buffer[index++] = receivedBuffer[nbReceived++];
		}
		return buffer;
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

}
