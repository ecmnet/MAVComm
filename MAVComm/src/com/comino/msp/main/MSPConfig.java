/*
 * Copyright (c) 2016 by E.Mansfeld
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.comino.msp.main;



import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

public class MSPConfig {

	private static final String VERSION = "1.0 build ";

	private static MSPConfig config = null;
	private String fileName = null;
	private String version = null;

	private Properties prop = null;

	public static MSPConfig getInstance(String filename) {
		if(config==null)
			config = new MSPConfig(filename);
		return config;
	}

	public static MSPConfig getInstance() {
		return config;
	}

	private MSPConfig(String filename) {
		this.fileName = filename;
		this.prop = new Properties();
		System.out.println();
		System.out.println("Initializing ("+filename+")...");
		refreshProperties();
		this.version = VERSION + prop.getProperty("build","tmp");
	}


	public void updateProperty(String item, String value)  {
		this.prop.setProperty(item, value);
	}

	public void flushToDisk() throws IOException {
		URL url = getClass().getClassLoader().getResource(fileName);
		FileOutputStream file;
		file = new FileOutputStream(url.getFile());
		prop.store(file,new Date().toString());
		file.close();
	}

	public MSPConfig refreshProperties() {
		try {
			InputStream propStream = getClass().getClassLoader().getResourceAsStream(fileName);
			if(propStream!=null) {
				prop.load(propStream);
			propStream.close();
				//		DefaultLogger.getInstance().log(prop.getProperty("description", "Default")+" configuration loaded");
			} else
				throw new IOException("Configuration file'"+fileName+"' not found in classpath");
		} catch(IOException io ) {
			System.err.println(io.getMessage());
		}
		return this;
	}

	// TODO: 2.8: Avoid permanent calls (status)
	public String getVersion() {
		return version;
	}

	public Enumeration<String> getPropertyNames() {
		return (Enumeration<String>) prop.propertyNames();
	}

	public String getProperty(String key, String defaultValue) {
		return prop.getProperty(key, defaultValue);
	}

	public String getProperty(String key) {
		return prop.getProperty(key, "-");
	}

	public String[] getListProperty(String key) {
		String line = prop.getProperty(key);
		if(line==null)
			return null;
		return line.trim().split("\\s*,\\s*");
	}

	public boolean getBoolProperty(String key, String defaultValue) {
		return Boolean.parseBoolean(prop.getProperty(key, defaultValue));
	}

	public int getIntProperty(String key, String defaultValue) {
		return Integer.parseInt(prop.getProperty(key, defaultValue));
	}

	public float getFloatProperty(String key, String defaultValue) {
		return Float.parseFloat(prop.getProperty(key, defaultValue));
	}


}
