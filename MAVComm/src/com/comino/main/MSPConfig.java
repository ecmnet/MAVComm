/****************************************************************************
 *
 *   Copyright (c) 2017 Eike Mansfeld ecm@gmx.de. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 ****************************************************************************/


package com.comino.main;



import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

public class MSPConfig {

	private static MSPConfig config = null;
	private String fileName = null;
	private String version = "tmp";

	private Properties prop = null;
	private String path;

	public static MSPConfig getInstance(String path, String filename) {
		if(config==null)
			config = new MSPConfig(path, filename);
		return config;
	}

	public static MSPConfig getInstance() {
		return config;
	}

	private MSPConfig(String path, String filename) {
		this.fileName = filename;
		this.path = path;
		this.prop = new Properties();
		System.out.println();
		System.out.println("Initializing ("+filename+", Java "+System.getProperty("java.version")+")...");
		refreshProperties();
		this.version = prop.getProperty("build","tmp");
	}

	public String getBasePath() {
		return path;
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
			InputStream propStream = new FileInputStream(path+"/"+fileName);
			if(propStream!=null) {
				prop.load(propStream);
			    propStream.close();
			}
		} catch(IOException io ) {
			System.err.println("Configuration file'"+fileName+"' not found.");
		}
		return this;
	}

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
		return (int)Float.parseFloat(prop.getProperty(key, defaultValue).trim());
	}

	public float getFloatProperty(String key, String defaultValue) {
		return Float.parseFloat(prop.getProperty(key, defaultValue).trim());
	}


}
