package com.comino.msp.utils.upboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CPUTemperature {

	private long tms = 0;
	private int  temperature = 0;

	public void getTemperature() {
		if(System.currentTimeMillis()-tms < 10000)
			return;
		try {
			String line = null;
			Process process = Runtime.getRuntime().exec("cat /sys/devices/platform/coretemp.0/hwmon/hwmon1/temp2_input");
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while((line=reader.readLine())!=null)
					temperature = (temperature  + Integer.parseInt(line.trim()) / 1000) / 2;
			reader.close();
		} catch (IOException e) { }
		tms = System.currentTimeMillis();
	}

	public int get() {
		return temperature;
	}

}
