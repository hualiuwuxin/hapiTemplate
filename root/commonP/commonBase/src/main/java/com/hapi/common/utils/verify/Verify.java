package com.hapi.common.utils.verify;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 验证 注解
 * @author Admin
 *
 */
@Target({  ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Verify {

	/**
	 * 数据类型
	 * @return
	 */
	DataType dataType() default DataType.regular;
	

	
	/**
	 *  正则 （ DataType 是 regular 的时候才生效   ）
	 * @return
	 */
	String  regular() default "";
	
}
