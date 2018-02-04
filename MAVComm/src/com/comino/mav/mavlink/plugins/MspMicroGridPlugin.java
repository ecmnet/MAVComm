package com.comino.mav.mavlink.plugins;

import org.mavlink.messages.lquac.msg_msp_micro_grid;

public class MspMicroGridPlugin extends MAVLinkPluginBase {

	public MspMicroGridPlugin() {
		super(msg_msp_micro_grid.class);
	}

	@Override
	public void received(Object o) {

		msg_msp_micro_grid grid = (msg_msp_micro_grid) o;
		model.grid.fromArray(grid.data);
		model.grid.setIndicator(grid.cx, grid.cy, grid.cz);
		model.grid.setProperties(grid.extension, grid.resolution);
		model.grid.count = (int) grid.count;
		model.grid.status = (byte)grid.status;
		model.grid.tms = model.sys.getSynchronizedPX4Time_us();

	}
}
