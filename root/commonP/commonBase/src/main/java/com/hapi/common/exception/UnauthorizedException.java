package com.hapi.common.exception;

public class UnauthorizedException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5080834071105563000L;

	public UnauthorizedException(String msg) {
        super(msg);
    }
}
