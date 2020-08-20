package com.hapi.common.utils.verify;

/**
 *  数据类型
 */
public enum DataType {

	email(3, "邮箱"),
	regular(10, "正则");


    private int code;
    private String value;

    DataType(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

}
