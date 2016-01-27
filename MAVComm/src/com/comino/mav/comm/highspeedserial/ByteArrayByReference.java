package com.comino.mav.comm.highspeedserial;


import com.sun.jna.ptr.ByReference;

public class ByteArrayByReference extends ByReference {
	
    
	byte[] buffer = null;
	
	public ByteArrayByReference() {
		this(1000);
	}

    public ByteArrayByReference(int len) {
        super(len);
        buffer = new byte[len];
    }
    
    
    public ByteArrayByReference(byte[] buffer) {
        super(buffer.length);
        this.buffer = buffer;
        for(int i=0;i<buffer.length;i++)
             getPointer().setByte(i, buffer[i]);
 
    }

    public byte[] getValue(int len) {
        return getPointer().getByteArray(0, len);
    }
    
    
    public int length() {
    	return buffer.length;
    }
}

