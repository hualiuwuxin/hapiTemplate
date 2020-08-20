package com.hapi.common.exception;

/**
 * 频率 过滤器
 * @author Admin
 *
 */
public class FrequencyException extends RuntimeException   {
	private static final long serialVersionUID = -5423452810358016634L;

	public FrequencyException(String message ) {
		super( message);
	}

}
