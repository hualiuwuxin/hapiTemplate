package com.hapi.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.hapi.common.exception.ParameterErrorException;

/**
 * 集合工具
 * @author ZHANGYUKUN
 *
 */
public class CollectionUtil {
	
	
	/**
	 * 在一个集合里面寻找 指定  唯一标志的 的元素 并返回，没有返回null
	 * @param collection 集合
	 * @param id 目标唯一标志
	 * @param idName 唯一标志名
	 * @return
	 */
	public static <T> T find( Collection<T> collection , Long id , String idName) {
		if( collection == null || id == null || idName == null ) {
			return null;
		}
		
		for(T item  : collection ) {
			if( item == null ) {
				return null;
			}
			
			try {
				Field field = item.getClass().getDeclaredField(idName);
				field.setAccessible(true);
				Object value = field.get( item );
				
				if( id.equals( value ) ) {
					return item;
				}
			} catch (Exception  e) {
				e.printStackTrace();
			}
			
		}
		return null;
	}
	
	public void  t1() {
		
	}
	
	
	public void  t2() {
		
		
		
		
		
		
		
		
		
		
	}
	
	
	
	/**
	 * 在一个集合里面寻找 指定  唯一标志的 的元素 并返回，没有返回null(默认的唯一主键名为"id")
	 * @param collection 集合
	 * @param id 目标唯一标志
	 * @return
	 */
	public static <T> T find( Collection<T> collection , Long id ) {
		return find( collection,id,"id" );
	}
	
	/**
	 * 在指定集合中提取出某个字段的值
	 * @param collection 集合
	 * @param fieldName 字段名
	 * @param cls 字段类型
	 * @return
	 */
	public static <T> List<T> extractFieldValue( Collection<?> collection , String fieldName , Class<T>  cls) {
		if( collection == null ||  fieldName == null || cls == null) {
			return null;
		}
		
		
		List<T> list = new ArrayList<>();
		for(Object item  : collection ) {
			if( item == null ) {
				continue;
			}
			
			list.add( getFiledValue(item,fieldName,cls ));
		}
		
		return list;
	}
	
	/**
	 * 在指定数组中提取出某个字段的值
	 * @param array 数组
	 * @param fieldName 字段名
	 * @param cls 字段类型
	 * @return
	 */
	public static <T> List<T> extractFieldValue( Object[] array , String fieldName , Class<T>  cls) {
		if( array == null || fieldName == null ||  cls == null) {
			return null;
		}
		
		List<T> list = new ArrayList<>();
		for(Object item  : array ) {
			if( item == null ) {
				continue;
			}
			
			list.add( getFiledValue(item,fieldName,cls ));
		}
		
		return list;
	}
	
	/**
	 * 获取某个元素的某个字段的值（如果不是期望的类型，name货抛出异常）
	 * 
	 * @param item
	 * @param fieldName
	 * @param cls
	 * @return
	 */
	private static <T> T getFiledValue(Object item , String fieldName,Class<T> cls ){
		
		try {
			Field field = item.getClass().getDeclaredField( fieldName );
			field.setAccessible(true);
			if( !field.getType().equals( cls ) ) {
				throw new ParameterErrorException("集合里面不是："  + cls + "类型");
			}
			
			@SuppressWarnings("unchecked")
			T value = (T) field.get( item );
			
			return value;
		} catch (Exception  e) {
			e.printStackTrace();
		}
		return null;
	}
	

}
