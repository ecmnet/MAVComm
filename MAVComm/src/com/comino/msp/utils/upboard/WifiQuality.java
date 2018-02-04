package com.comino.msp.utils.upboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WifiQuality {

	private long tms = 0;
	private int  quality = 0;

	public void getQuality() {
		if(System.currentTimeMillis()-tms < 1000)
			return;
		try {
			String line = null;
			Process process = Runtime.getRuntime().exec("cat /proc/net/wireless");
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while((line=reader.readLine())!=null)
				if(line.contains("wlan"))
					quality = Integer.parseInt(line.substring(20, 23).trim());

			reader.close();
		} catch (IOException e) { }
		tms = System.currentTimeMillis();
	}

	public int get() {
		return quality;
	}

}
