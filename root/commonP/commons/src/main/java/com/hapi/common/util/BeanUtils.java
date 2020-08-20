package com.hapi.common.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hapi.common.exception.ParameterErrorException;
import com.hapi.common.utils.verify.DataType;
import com.hapi.common.utils.verify.Verify;

/**
 * bean的工具类
 *
 * @author ZHANGYUKUN
 */
public class BeanUtils {


    /**
     * 复制一个对象到另一个对象，忽略null值字段
     *
     * @param source
     * @param target
     * @param ignoreNull
     */
    public static void copyProperties(Object source, Object target, Boolean ignoreNull) {
        if (target == null) {
            return;
        }

        if (source == null) {
            return;
        }

        if (!ignoreNull) {
            org.springframework.beans.BeanUtils.copyProperties(source, target);
        } else {
            String[] ignoreFiled = getNullField(source);
            org.springframework.beans.BeanUtils.copyProperties(source, target, ignoreFiled);
        }

    }

    public static void copyProperties(Object source, Object target) {
        copyProperties(source, target, false);
    }

    /**
     * 创建并复制一个对象
     *
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T> T copyNew(Object source, Class<T> targetCls) {
        if (source == null) {
            return null;
        }

        T rt;
        try {
            rt = targetCls.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        org.springframework.beans.BeanUtils.copyProperties(source, rt);
        return rt;
    }

    /**
     * 复制信息到outList
     *
     * @param list
     * @param cls
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> copyToOutList(List<?> list, Class<T> cls) {
        if (list == null) {
            return null;
        }
        List<T> rtList = null;
        try {
            rtList = list.getClass().newInstance();
            for (Object item : list) {
                T rtItem = cls.newInstance();
                BeanUtils.copyProperties(item, rtItem, false);
                rtList.add(rtItem);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rtList;
    }

	/**
	 * 复制信息到outList，带page信息
	 * @param list
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Page<T> copyToOutListPage(IPage<?> list, Class<T> cls) {
		if (list == null || list.getRecords()==null || list.getRecords().isEmpty()) {
			return new Page<T>();
		}
		Page<T> outList = new Page<T>();
        copyPageList(list, outList);
		try {
		    List<T> outTempList = list.getRecords().getClass().newInstance();
			for (Object item : list.getRecords()) {
				T outItem = cls.newInstance();
				BeanUtils.copyProperties(item, outItem, false);
                outTempList.add(outItem);
			}
            outList.setRecords(outTempList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return outList;
	}

    /**
     * 复制分页信息
     *
     * @param source
     * @param target
     */
    private static void copyPageList(Object source, Object target) {
        String[] ignoreFiled = new String[]{"records"};
        org.springframework.beans.BeanUtils.copyProperties(source, target, ignoreFiled);
    }

    /**
     * 得到 值为null 的字段 （只找当前类，没找父类，因为我们的实体暂时没有继承关系）
     *
     * @param source
     * @return
     */
    public static String[] getNullField(Object source) {
        List<String> fieldList = new ArrayList<>();
        Field[] fields = source.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.get(source) == null) {
                    fieldList.add(field.getName());
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        return fieldList.toArray(new String[]{});
    }

    /**
     * 得到定义的所有字段（返回数组）
     *
     * @return
     */
    public static String[] getDeclareField(Class<?> cls) {
        return getDeclareFieldAsList(cls).toArray(new String[]{});
    }


    /**
     * 得到定义的所有字段(返回list)
     *
     * @return
     */
    public static List<String> getDeclareFieldAsList(Class<?> cls) {
        List<String> fieldList = new ArrayList<>();
        Field[] fields = cls.getDeclaredFields();

        for (Field field : fields) {
            fieldList.add(field.getName());
        }

        return fieldList;
    }
    
    /**
     * 	忽略值为空字符串的字段 （ 处理前端 带上 name=name&name2=&name4=&name3=   这种  带了  key 但是 不传值得问题  ）
     * @param in
     */
	public static void ignoreNullStrField(Object in) {
		if( in == null ) {
			return ;
		}
		
		Class<?> cls = in.getClass();
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			//获取字段值
			Object fieldValue = null;
			try {
				field.setAccessible(true);
				fieldValue = field.get(in);
				
				
				if( fieldValue instanceof String ) {
					if( "".equals( fieldValue )  ) {
						field.set( in , null);
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
    
    
    
    
	/**
	 * 检查 in对象,如果不符合规则就抛出异常
	 */
	@SuppressWarnings("unchecked")
	public static void checkIn(Object in) {
		if( in == null ) {
			return ;
		}
		
		Class<?> cls = in.getClass();
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			
			//获取字段值
			Object fieldValue = null;
			try {
				field.setAccessible(true);
				fieldValue = field.get(in);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
			//核对 必填
			Class<? extends Annotation> apiParamCls;
			try {
				apiParamCls = (Class<? extends Annotation>) Class.forName("io.swagger.annotations.ApiModelProperty");
			} catch (ClassNotFoundException e) {
				return ;
			}
			if (field.isAnnotationPresent( apiParamCls )) {
				Annotation apiParam = field.getAnnotation( apiParamCls );

				
				Boolean requiredValue = false;
				try {
					Method method = apiParamCls.getMethod("required");
					requiredValue = (Boolean) method.invoke( apiParam );
					
					method = apiParamCls.getMethod("allowableValues");
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if( requiredValue && fieldValue == null  ) {
					throw new ParameterErrorException("字段:" + field.getName() +"必填");
				}
				
			}
			
			
			//核对格式
			if (field.isAnnotationPresent( Verify.class )) {
				Annotation verify = field.getAnnotation( Verify.class );
				DataType dataType = null;
				try {
					
					Method dataTypeM = Verify.class.getMethod("dataType");
					dataType = (DataType) dataTypeM.invoke( verify );
					
				} catch (Exception e) {
					e.printStackTrace();
				}
					
				
				if( dataType.equals( DataType.email ) ) {
					if( !fieldValue.toString().matches( "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$" ) ) {
						throw new ParameterErrorException("字段:" + field.getName() +"需要符合是一个合法的邮件地址" );
					}
				}
				
				//正则
				if( dataType.equals( DataType.regular ) ) {
					
					String regular = null;
					try {
						Method regularM = Verify.class.getMethod("regular");
						regular = (String) regularM.invoke( verify );
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					if( StringUtils.isEmpty( regular ) ) {
						return;
					}
					
					if( !fieldValue.toString().matches( regular ) ) {
						throw new ParameterErrorException("字段:" + field.getName() +"需要符合正则:" + regular );
					}
				}
				
			}
		}
	}
	
	
	
	
	/**
	 * 对象 装换成 有格式的 Map( 打印，导出专用 )
	 * @param data
	 * @return
	 */
	public static Map<String, String> toFormatMap(Object obj) {
		Map<String, String> map = new HashMap<String, String>();
		Method[] methods = obj.getClass().getMethods();

		for (Method method : methods) {
			try {
				int mod = method.getModifiers();
				if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
					continue;
				}
				if (!method.getName().startsWith("get")) {
					continue;
				}
				Object value = method.invoke(obj);

				if (value != null) {
					String name = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
					
					if( value instanceof Enum && value.getClass().getPackage().getName().contains("com.chuanyi.ecard") ) {
						Method emM =  value.getClass().getMethod("getValue");
						map.put(name, emM.invoke( value )+"" );
					}else if( value instanceof Date ){
						map.put(name,  DateUtils.format( (Date)value , 3) );
					}else {
						map.put(name, value.toString());
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		return map;
	}



}
