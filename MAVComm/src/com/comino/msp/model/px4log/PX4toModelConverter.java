package com.comino.msp.model.px4log;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comino.msp.model.DataModel;
import com.comino.msp.model.MSTYPE;

import me.drton.jmavlib.log.FormatErrorException;
import me.drton.jmavlib.log.px4.PX4LogReader;

public class PX4toModelConverter {

	private PX4LogReader reader;
	private List<DataModel> list;


	public PX4toModelConverter(PX4LogReader reader, List<DataModel> list) {
		this.reader = reader;
		this.list = list;
	}


	public void doConversion(DataModel current) throws FormatErrorException {

		long tms_slot = 0; long tms = 0;

		Map<String,Object> data = new HashMap<String,Object>();

		MSTYPE[] types = MSTYPE.values();
		for(int i=0; i< types.length;i++) {
			if(MSTYPE.getPX4LogName(types[i]).length()>0) {
				data.put(MSTYPE.getPX4LogName(types[i]), null);
			}
		}

		list.clear();
		current.clear();

		try {

			while(tms < reader.getSizeMicroseconds()) {
				tms = reader.readUpdate(data)-reader.getStartMicroseconds();
				if(tms > tms_slot) {
					tms_slot += 50000;
					for(int i=0; i< types.length;i++) {
						if(MSTYPE.getPX4LogName(types[i]).length()>0) {
							String px4Name = MSTYPE.getPX4LogName(types[i]);
							try {
							  MSTYPE.putValue(current, types[i], (float)data.get(px4Name));
							} catch(Exception e) {
								throw new FormatErrorException(px4Name+" was not found");
							}
						}
					}
					list.add(current.clone());
				}
			}

		} catch(IOException e) {
			System.out.println(list.size()+" entries read. Timespan is "+tms_slot/1e6f+" sec");

		}
	}



}
