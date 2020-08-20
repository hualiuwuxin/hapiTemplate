package com.hapi.common.utils.verify;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  需要指定角色 注解
 * @author 
 *
 */
@Target({  ElementType.METHOD,ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRole {

	/**
	 * 数据类型
	 * @return
	 */
	String value() default "";
	

}
