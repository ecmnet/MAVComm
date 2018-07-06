package com.comino.mav.mavlink;

public class MAVLinkBlockingReader extends MAVLinkReader implements Runnable {

	private MAVLinkToModelParser parser;

	public MAVLinkBlockingReader(int id, MAVLinkToModelParser parser) {
		this(id,false, parser);
	}

	public MAVLinkBlockingReader(int id, boolean noCRCCheck, MAVLinkToModelParser parser) {
		super(id,noCRCCheck);
		this.parser = parser;
		Thread t = new Thread(this);
		t.setName("MAVLinkBlockingReader");
		t.start();
	}

	@Override
	public void run() {
		synchronized(this) {
			while(true) {
				try {
					if(packets.isEmpty())
						wait();
					parser.parseMessage(getNextMessage());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
