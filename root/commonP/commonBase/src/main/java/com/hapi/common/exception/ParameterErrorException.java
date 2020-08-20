package com.hapi.common.exception;

public class ParameterErrorException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8067060477822217162L;

	public ParameterErrorException(String msg) {
        super(msg);
    }
}
