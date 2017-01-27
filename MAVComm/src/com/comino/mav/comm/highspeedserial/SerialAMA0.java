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


package com.comino.mav.comm.highspeedserial;

import com.sun.jna.Library;
import com.sun.jna.Native;

interface DLibrary extends Library {
	DLibrary INSTANCE = (DLibrary) Native.loadLibrary("AMA0", DLibrary.class);

	int  openAMA0(int rate);
	void closeAMA0();

	void writeAMA0(ByteArrayByReference str, int length);
	int  readAMA0(ByteArrayByReference str, int length);

}

public class SerialAMA0 {

	private ByteArrayByReference buffer = new ByteArrayByReference(1000);

	public SerialAMA0() {


	}

	public void open() {
		DLibrary.INSTANCE.openAMA0(921600);
		//DLibrary.INSTANCE.openAMA0(57600);
		System.out.println("SerialAMA0 opened");
	}

	public void close() {
		DLibrary.INSTANCE.closeAMA0();
	}


	public void writeBytes(byte[] buffer) {
		DLibrary.INSTANCE.writeAMA0(new ByteArrayByReference(buffer), buffer.length);
	}

	public int getInputBufferBytesCount() {
		int count = DLibrary.INSTANCE.readAMA0(buffer, buffer.length());
		return count;
	}

	public byte[] readBytes(int n) {
		return buffer.getValue(n);

	}

	public static void main(String[] args) {

		System.out.println("SerialAMA0 Test");

		SerialAMA0 ama0 = new SerialAMA0();
		ama0.open();

		byte[] s = {  (byte)1, (byte)8  };

		ama0.writeBytes(s);
//		while(true) {
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			byte[] b =ama0.readBytes(ama0.getInputBufferBytesCount());
			System.out.print(" bytes received => ");
			for(int i=0; i<b.length;i++)
				System.out.print(" "+b[i]);
			System.out.println();
//		}


	}

}
