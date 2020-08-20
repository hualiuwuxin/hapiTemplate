package com.hapi.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.hapi.common.exception.ParameterErrorException;


/**
 * 时间格式化工具
 * 
 * @author ZHANGYUKUNUP
 *
 */
public class DateUtils {
	private static final List<String> formarts = new ArrayList<>(4);
	static {
		formarts.add("yyyy-MM");
		formarts.add("yyyy-MM-dd");
		formarts.add("yyyy-MM-dd HH:mm");
		formarts.add("yyyy-MM-dd HH:mm:ss");
		formarts.add("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		formarts.add("yyyy-MM-dd'T'HH:mm:ss'Z'");
	}

	/**
	 * 格式化日期
	 * 
	 * @param dateStr String 字符型日期
	 * @param format  String 格式
	 * @return Date 日期
	 */
	private static Date parseDate(String dateStr, String format) {
		Date date = null;
		try {
			DateFormat dateFormat = new SimpleDateFormat(format);
			date = dateFormat.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ParameterErrorException("日期解析错误");
		}
		return date;
	}

	/**
	 * 把字符串类型的时间转换成 Date对象 支持的 格式如下 
	 * 1 yyyy-MM 
	 * 2 yyyy-MM-dd 
	 * 3 yyyy-MM-dd HH:mm 
	 * 4 yyyy-MM-dd HH:mm:ss 
	 * 5 字数字符串的时间毫秒数
	 * 6 yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
	 * @param dateStr
	 * @return
	 */
	public static Date parse(String dateStr) {
		if (dateStr == null) {
			return null;
		}
		String value = dateStr.trim();
		if ("".equals(value)) {
			return null;
		}

		if (value.matches("^\\d{4}-\\d{1,2}$")) {
			return parseDate(value, formarts.get(0));
		} else if (value.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
			return parseDate(value, formarts.get(1));
		} else if (value.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
			return parseDate(value, formarts.get(2));
		} else if (value.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
			return parseDate(value, formarts.get(3));
		} else if (value.matches("^\\d{1,19}$")) {
			return new Date(Long.parseLong(value));
		} else if (value.matches("^\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{1,2}:\\d{1,2}.\\d{1,3}Z$")) {
			return parseDate(value, formarts.get(4));
		} else if (value.matches("^\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{1,2}:\\d{1,2}Z$")) {
			return parseDate(value, formarts.get(5));
		} else {
			throw new ParameterErrorException("不支持的时间格式");
		}

	}

	/**
	 * 格式化 时间(默认按照最常见的时间格式化输出)
	 * 
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		return format(date, 3);
	}

	/**
	 * 格式化时间
	 * 
	 * @param date
	 * @param i    用第几种 格式化方式
	 * @return
	 */
	public static String format(Date date, int i) {
		if (date == null) {
			return null;
		}

		if (i > formarts.size() - 1) {
			return null;
		}

		DateFormat dateFormat = new SimpleDateFormat(formarts.get(i));
		return dateFormat.format(date);
	}

	/**
	 * 取 指定日期当天第一秒的时间(00:00:00)
	 * 
	 * @param date
	 * @return
	 */
	public static Date getBeginTime(Date date) {
		return parse(format(date, 1) + " 00:00:00");
	}

	/**
	 * 取指定日期当天最后最后一秒的时间（23:59:59）
	 * 
	 * @param date
	 * @return
	 */
	public static Date getEndTime(Date date) {
		return parse(format(date, 1) + " 23:59:59");
	}
	
	/**
	  *    取 后面 几天的 当前时间
	 * @param date
	 * @param dayNum 天数
	 * @return
	 */
	public static Date nextDayNow(Date date, int dayNum) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		calendar.add(  Calendar.DAY_OF_MONTH , dayNum );
		return calendar.getTime();
	}
	
	/**
	 * 去后面的几个小时的时间
	 * @param date
	 * @param hourNum
	 * @return
	 */
	public static Date nextHourNow(Date date, int hourNum) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		calendar.add(  Calendar.HOUR_OF_DAY , hourNum );
		return calendar.getTime();
	}
	
	
	
	
	/**
	 * 修改一个时间的  时分秒部分
	 * @param date   时间
	 * @param time  12:00:00 的  时分秒格式
	 * @return  如果格式错误 不能装换 就返回 null
	 */
	public static Date modifyTime(Date date, String time) {
		if( date == null ||  StringUtils.isEmpty( time ) ) {
			return null;
		}
		if( !time.matches( "^\\d{1,2}:\\d{1,2}:\\d{1,2}$" ) ) {
			return null;
		}
		
		return DateUtils.parse( DateUtils.format( new Date(),1 ) + " " + time  );
	}
	
	
	
	/**
	 * 得到年月日
	 * 下标 的0,1,2 对应年月日
	 * @param date
	 * @return
	 */
	public static int[] getYMD(Date date) {
		String[] ymd = format(date, 1).split("-");
		return new int[]{ Integer.valueOf( ymd[0] ),Integer.valueOf( ymd[1] ) ,Integer.valueOf( ymd[2] )    };
	}


	
	/**
	 * 得到指定月份的最后一天
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date getLastDayOfMonth(int year,int month){
	        Calendar cal = Calendar.getInstance();
	        //设置年份
	        cal.set(Calendar.YEAR,year);
	        //设置月份
	        cal.set(Calendar.MONTH, month-1);
	        //获取某月最大天数
	        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	        //设置日历中月份的最大天数
	        cal.set(Calendar.DAY_OF_MONTH, lastDay);
	          
	        return cal.getTime();
	}
	
	/**
	 * 得到指定月份的第一天
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date getFirstDayOfMonth(int year,int month){
		Calendar cal = Calendar.getInstance();
		//设置年份
		cal.set(Calendar.YEAR,year);
		//设置月份
		cal.set(Calendar.MONTH, month-1);
		//获取某月最大天数
		//设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, 1);
		
		return cal.getTime();
	}
	
	
	
	/**
	 * 得到星期几
	 * @param date
	 * @return
	 */
	public static int getWeek(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		int week= cal.get(Calendar.DAY_OF_WEEK) - 1;
		if( week == 0 ) {
			week = 7;
		}
		
		return week;
	}
	
	
	
	public static void main(String[] args) {
		
		System.out.println( getWeek( new Date() ) );
		
	}
	
	/**
	 * 得到星期几
	 * @param date
	 * @return
	 */
	public static String getWeekStr(Date date){
		int i = getWeek(date);
		
		switch ( i ) {
			case 1:
				return "星期一";
			case 2:
				return "星期二";
			case 3:
				return "星期三";
			case 4:
				return "星期四";
			case 5:
				return "星期五";
			case 6:
				return "星期六";
			case 7:
				return "星期日";
			default:
				return "星期八??";
		}
		
	}
	
	
	
	/**
	 * 通过年月日得到格式化的时间
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static String format(int year,int month,int day) {
		return year+"-"+ (month<10?"0"+month:month)  +"-"+(day<10?"0"+day:day);
	}
	
}
