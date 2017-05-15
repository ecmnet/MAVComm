package com.comino.msp.slam.storage;

import com.comino.msp.model.segment.GPS;
import com.comino.msp.model.segment.Grid;

public class GridContainer {
	public Grid     grid;
	public GPS      home;
	public long     created_tms;

	// TODO:more attributes: e.g. count grids_merged as confidence level

}
