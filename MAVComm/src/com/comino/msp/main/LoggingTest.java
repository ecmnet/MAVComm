package com.comino.msp.main;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.mavlink.IMAVLinkCRC;
import org.mavlink.MAVLinkReader;
import org.mavlink.io.LittleEndianDataInputStream;
import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.messages.lquac.msg_altitude;
import org.mavlink.messages.lquac.msg_logging_data_acked;

public class LoggingTest {

	public static void main(String[] args) {

		System.out.println((int)IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[266]);

		MAVLinkReader reader = new MAVLinkReader(1);

		msg_logging_data_acked msg1  = new msg_logging_data_acked(1,1);
		msg1.sequence = 1;
		msg1.first_message_offset = 34;

		try {
			System.out.println(msg1);
			byte[] b = msg1.encode();
			MAVLinkMessage rec = reader.getNextMessage(b, b.length);
			System.out.println(rec);
		} catch (IOException e) {
			e.printStackTrace();
		}



		msg_altitude msg2  = new msg_altitude(1,1);
		try {
			System.out.println(msg2);
			byte[] b = msg2.encode();
			MAVLinkMessage rec = reader.getNextMessage(b, b.length);
			System.out.println(rec);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
