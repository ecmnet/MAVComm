package com.comino.mav.mavlink.plugins;

import com.comino.msp.execution.control.listener.IMAVLinkListener;
import com.comino.msp.model.DataModel;

public abstract class MAVLinkPluginBase implements IMAVLinkListener {

	private Class<?> 		clazz = null;
	protected DataModel 		model = null;

	public MAVLinkPluginBase(Class<?> clazz) {
		this.clazz = clazz;
	}

	public void setDataModel(DataModel model) {
		this.model = model;
	}

	public Class<?> getMessageClass() {
		return clazz;
	}

}
