package com.hapi.common.util;

public class SnGeneratorUtil {

    private static SnowFlake sf = new SnowFlake(2, 2);

    public static Long getId(){
        return sf.nextId();
    }
}
