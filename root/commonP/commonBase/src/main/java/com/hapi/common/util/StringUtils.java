package com.hapi.common.util;

public class StringUtils {

	public static boolean isEmpty(String str) {
		
		if( str == null ) {
			return true;
		}
		
		if( str.trim().equals("") ) {
			return true;
		}
		
		return false;
	}
	
	

}
