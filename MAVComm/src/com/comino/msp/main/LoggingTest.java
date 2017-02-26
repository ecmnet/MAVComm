package com.comino.msp.main;

import java.io.IOException;

import org.mavlink.IMAVLinkCRC;
import org.mavlink.MAVLinkReader;
import org.mavlink.messages.MAVLinkMessage;
import org.mavlink.messages.lquac.msg_logging_data_acked;

public class LoggingTest {

	public static void main(String[] args) {

		System.out.println((int)IMAVLinkCRC.MAVLINK_MESSAGE_CRCS[266]);

		MAVLinkReader reader = new MAVLinkReader(1);

		msg_logging_data_acked msg1  = new msg_logging_data_acked(1,1);
		msg1.sequence = 1;
		msg1.first_message_offset = 34;

		MAVLinkMessage rec = null;

		try {
			System.out.println(" M:"+msg1);
			byte[] b = msg1.encode();
			rec = reader.getNextMessage(b, b.length);
			System.out.println("R:"+rec);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			byte[] b = rec.encode();
			MAVLinkMessage rec2 = reader.getNextMessage(b, b.length);
			System.out.println("E:"+rec2);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
