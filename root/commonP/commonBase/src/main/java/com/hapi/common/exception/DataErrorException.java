package com.hapi.common.exception;

public class DataErrorException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8698938085517477827L;

	public DataErrorException(String msg) {
        super(msg);
    }
}
