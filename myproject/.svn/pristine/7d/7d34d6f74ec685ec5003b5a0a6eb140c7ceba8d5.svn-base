package com.dongao.core.common.utils;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间相关函数
 * 
 * @date 2013-10-24 上午11:00:44
 * @author dongao
 * @version 1
 */
public final class DateUtils {
	public static final String YYYYMMDD = "yyyy-MM-dd";
	public static final String HHMMSS = "HH:mm:ss";
	public static final String YYYYMMDDHHMMSS = YYYYMMDD + " " + HHMMSS;

	/**
	 * 1分钟有多少秒
	 */
	public static final Long SECONDS_IN_MINUTE = 60L;
	
	/**
	 * 1小时有多少分钟
	 */
	public static final Long MINUTES_IN_HOUR = 60L;
	
	/**
	 * 1天有多少小时
	 */
	public static final Long HOURS_IN_DAY = 24L;
	
	/**
	 * 1周有多少天
	 */
	public static final Long DAYS_IN_WEEK = 7L; 
	
	/**
	 * 1年有多少天
	 */
	public static final Long DAYS_IN_YEAR = 365L;
	
	/**
	 * 1月有多少天
	 */
	public static final Long DAYS_IN_MONTH = 30L;

	/**
	 * 1年有多少秒
	 */
	public static final Long SECONDS_IN_YEAR = SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY * DAYS_IN_YEAR;
	
	/**
	 * 1个月有多少秒
	 */
	public static final Long SECONDS_IN_MONTH = SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY * DAYS_IN_MONTH;
	
	/**
	 * 1周有多少秒
	 */
	public static final Long SECONDS_IN_WEEK = SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY * DAYS_IN_WEEK;
	
	/**
	 * 1天有多少秒
	 */
	public static final int SECONDS_IN_DAY = SECONDS_IN_MINUTE.intValue() * MINUTES_IN_HOUR.intValue() * HOURS_IN_DAY.intValue();
	
	/**
	 * 1小时有多少seconds
	 */
	public static final long SECONDS_IN_HOUR = SECONDS_IN_MINUTE * MINUTES_IN_HOUR;

	/**
	 * 1秒钟有多少milliseconds
	 */
	public static final long MILLISECONDS_IN_SECOND = 1000;
	
	/**
	 * 1分钟有多少milliseconds
	 */
	public static final long MILLISECONDS_IN_MINUTE = SECONDS_IN_MINUTE * MILLISECONDS_IN_SECOND;
	
	/**
	 * 1小时有多少milliseconds
	 */
	public static final long MILLISECONDS_IN_HOUR = MINUTES_IN_HOUR * MILLISECONDS_IN_MINUTE;
	
	/**
	 * 1天有多少milliseconds
	 */
	public static final long MILLISECONDS_IN_DAY = HOURS_IN_DAY * MILLISECONDS_IN_HOUR;
	
	/**
	 * 1周有多少milliseconds
	 */
	public static final long MILLISECONDS_IN_WEEK = DAYS_IN_WEEK * MILLISECONDS_IN_DAY;

	private DateUtils() {
	}

	/**
	 * 在FORM上按照指定日期范围搜索时，开始日期只能到日期，而数据库里还包含时刻，本方法把“从XX天”转换为“从XX时刻”
	 * 
	 * @param date
	 * @return String
	 */
	public static String getQueryDateFrom(final String date) {
		return new StringBuffer(date).append(" 00:00:00").toString();
	}

	/**
	 * 在FORM上按照指定日期范围搜索时，结束日期只能到日期，而数据库里还包含时刻，本方法把“到XX天”转换为“到XX时刻”
	 * 
	 * @param date
	 * @return String
	 */
	public static String getQueryDateTill(final String date) {
		return new StringBuffer(date).append(" 23:59:59").toString();
	}

	public static Timestamp getTimestamp() {
		return (new Timestamp(System.currentTimeMillis()));
	}

	public static Timestamp getTimestamp(final Date date) {
		return (new Timestamp(date.getTime()));
	}

	/**
	 * 返回本年
	 * 
	 * @return int
	 */
	public static int getThisYear() {
		Calendar calendarNow = Calendar.getInstance();
		return calendarNow.get(Calendar.YEAR);
	}

	/**
	 * 返回今天，格式yyyy-MM-dd
	 * 
	 * @return String
	 */
	public static String todayDateStr() {
		return showDate(new Date());
	}

	/**
	 * 分钟转小时
	 * 
	 * @param minutes
	 * @return String
	 */
	public static String minutesToHours(final Integer minutes) {
		double ret = (int) minutes / 60;
		ret = ret + (minutes % 60) / 60.0;

		DecimalFormat df = new DecimalFormat("###.0");

		return df.format(ret);
	}

	/**
	 * 获取明天日期
	 * 
	 * @return String
	 */
	public static String tommorowDateStr() {
		Date tommorow = addDay(new Date(), 1);
		return showDate(tommorow);
	}

	/**
	 * 指定date转换成字符串 按照yyyy-MM-dd格式
	 * 
	 * @param date
	 * @return String
	 */
	public static String showDate(final Date date) {
		return dateToStr(date, YYYYMMDD);
	}

	/**
	 * 根据date输出具体时间 按照HH:mm:ss格式
	 * 
	 * @param date
	 * @return String
	 */
	public static String showTime(final Date date) {
		return dateToStr(date, HHMMSS);
	}

	/**
	 * 根据date输出具体日期时间 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return String
	 */
	public static String showDateTime(final Date date) {
		return dateToStr(date, YYYYMMDDHHMMSS);
	}

	/**
	 * 按照指定的格式，把一个日期型转换为字符串 例如， dateToStr(new Date(),"yyyy-MM-dd hh:mm:ss");
	 * 
	 * @param date
	 * @param format
	 * @return String
	 */
	public static String dateToStr(final Date date, final String format) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat dateToStr = new SimpleDateFormat(format);
		return dateToStr.format(date);
	}

	/**
	 * 按照指定的格式，把一个字符串型转换为日期
	 * 例如，strToDate("2000-12-11-06:23:12","yyyy-MM-dd-hh:mm:ss");
	 * 
	 * @param dateStr
	 * @param format
	 * @return Date
	 */
	public static Date strToDate(final String dateStr, final String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date;
		try {
			date = sdf.parse(dateStr);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 按照指定的格式，把一个字符串型转换为日期
	 * 例如，strToDate("2000-12-11-06:23:12","yyyy-MM-dd-hh:mm:ss");
	 * 
	 * @param dateStr
	 * @param format
	 * @return Date
	 */
	public static Date strToDate(final String dateStr, final String format, Locale locale) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
		Date date;
		try {
			date = sdf.parse(dateStr);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 计算fromDateStr到toDateStr还有多少天
	 * 
	 * @param fromDateStr
	 * @param toDateStr
	 * @return long
	 */
	public static long daysLeftTo(final String fromDateStr,
			final String toDateStr) {
		if (!StringUtils.isNotEmpty(fromDateStr)
				|| !StringUtils.isNotEmpty(toDateStr)) {
			return 0;
		}
		return Long.parseLong(CalendarUtil.getTwoDay(toDateStr, fromDateStr));
	}

	/**
	 * 计算fromDate到toDate还有多少天
	 * 
	 * @param fromDate
	 * @param toDate
	 * @return long
	 */
	public static long daysLeftTo(final Date fromDate, final Date toDate) {
		if (fromDate == null || toDate == null) {
			return 0;
		}

		return (toDate.getTime() - fromDate.getTime()) / MILLISECONDS_IN_DAY;
	}

	/**
	 * 计算两个日期相差的分钟数
	 * 
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public static long minutesLeftTo(final Date fromDate, final Date toDate) {
		if (fromDate == null || toDate == null) {
			return 0;
		}
		return (toDate.getTime() - fromDate.getTime()) / MILLISECONDS_IN_MINUTE;
	}

	/**
	 * 返回测试时间距当前时间共多少天数, 精确到一天之内(取精确值)
	 * 
	 * @param toDate
	 * @return
	 */
	public static Double daysLeftTo(final Date toDate) {
		SimpleDateFormat d = new SimpleDateFormat(YYYYMMDDHHMMSS); // 格式化时间
		String nowtime = d.format(new Date()); // 按以上格式 将当前时间转换成字符串
		Double days = 0d;
		try {
			days = Long.valueOf(toDate.getTime() - d.parse(nowtime).getTime())
					.doubleValue()
					/ MILLISECONDS_IN_DAY;
		} catch (ParseException e) {
			e.getMessage();
		}
		return days;
	}

	/**
	 * 计算现在距离某个时间(from)已经过去了多少分钟
	 * 
	 * @param from
	 * @return Long
	 */
	public static Long calMinutesPassed(final Date from) {
		if (from == null) {
			return 0L;
		}
		return (new Date().getTime() - from.getTime()) / MILLISECONDS_IN_MINUTE;
	}

	/**
	 * 对输入的日期进行比较，带回相差的天数
	 * 
	 * @param s1
	 * @param s2
	 * @return int
	 */
	public static int compDate(final String s1, final String s2) {
		int day = 0;
		SimpleDateFormat sf = new SimpleDateFormat(YYYYMMDD);
		try {
			Date date1 = sf.parse(s1);
			Date date2 = sf.parse(s2);
			day = (int) ((date2.getTime() - date1.getTime()) / MILLISECONDS_IN_DAY);
		} catch (Exception e) {
			e.getMessage();
		}
		return day;
	}

	/**
	 * 计算当前时间与compareDateStr还剩余多少天，compareDateStr例子：2008-09-19
	 * 如果已经到了那一天或者已经超过了那一天，那么返回负数
	 * 
	 * @param compareDateStr
	 * @return long
	 */
	public static long daysLeftTo(final String compareDateStr) {
		return daysLeftTo(todayDateStr(), compareDateStr);
	}

	/**
	 * 今天与compareDateStr相差几天
	 * 
	 * @param compareDateStr
	 * @return long
	 */
	public static long daysAfter(final String compareDateStr) {
		return -1 * daysLeftTo(compareDateStr);
	}

	/**
	 * 判断当前时间是否在某日期之后
	 * 
	 * @param compareDateStr
	 * @return boolean
	 */
	public static boolean todayIsAfter(final String compareDateStr) {
		return !todayIsBefore(compareDateStr);
	}

	/**
	 * 判断fromDateStr时间是否在toDateStr日期之后
	 * 
	 * @param fromDateStr
	 * @param toDateStr
	 * @return boolean
	 */
	public static boolean isAfter(final String fromDateStr,
			final String toDateStr) {
		return daysLeftTo(fromDateStr, toDateStr) <= 0;
	}

	/**
	 * 判断当前时间是否在某日期之前
	 * 
	 * @param compareDateStr
	 * @return boolean
	 */
	public static boolean todayIsBefore(final String compareDateStr) {
		return daysLeftTo(compareDateStr) > 0;

	}

	/**
	 * 判断fromDateStr时间是否在toDateStr时间之前
	 * 
	 * @param fromDateStr
	 * @param toDateStr
	 * @return boolean
	 */
	public static boolean isBefore(final String fromDateStr,
			final String toDateStr) {
		return !isAfter(fromDateStr, toDateStr);
	}

	/**
	 * 判断今天是否是与指定的日期是同一天
	 * 
	 * @param compareDateStr
	 * @return boolean
	 */
	public static boolean sameDay(final String compareDateStr) {
		return daysLeftTo(compareDateStr) == 0;
	}

	/**
	 * 判断两个日期是否是同一天
	 * 
	 * @param fromDateStr
	 * @param toDateStr
	 * @return boolean
	 */
	public static boolean isSameDay(final String fromDateStr,
			final String toDateStr) {
		return daysLeftTo(fromDateStr, toDateStr) == 0;
	}

	/**
	 * 计算出date之后days天后的日期,days可以是负数
	 * 
	 * @param date
	 * @param days
	 * @return Date
	 */
	public static Date addDay(final Date date, final int days) {
		Calendar cale = Calendar.getInstance();
		cale.setTime(date);
		cale.add(Calendar.DAY_OF_YEAR, days);
		return cale.getTime();
	}

	/**
	 * 计算某个date之后(huors>0)或之前(hours<0)的Date值
	 * 
	 * @param date
	 * @param hours
	 * @return Date
	 */
	public static Date addHour(final Date date, final int hours) {
		Calendar cale = Calendar.getInstance();
		cale.setTime(date);
		cale.add(Calendar.HOUR_OF_DAY, hours);
		return cale.getTime();
	}

	/**
	 * 计算某个date之后(minutes>0)或之前(minutes<0)的Date值
	 * 
	 * @param date
	 * @param minutes
	 * @return Date
	 */
	public static Date addMinute(final Date date, final int minutes) {
		Calendar cale = Calendar.getInstance();
		cale.setTime(date);
		cale.add(Calendar.MINUTE, minutes);
		return cale.getTime();
	}

	/**
	 * 返回当天的最后时间 如 2013-11-26 23:59:59.999
	 * 
	 * @return Timestamp
	 */
	public static Timestamp getCurrentDayEnd() {
		Calendar c = Calendar.getInstance();
		Timestamp t = new Timestamp(System.currentTimeMillis());
		c.setTime(t);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return new Timestamp(c.getTimeInMillis());
	}

	/**
	 * 返回当天的开始时间 如 2013-11-26 00:00:00.0
	 * 
	 * @return Timestamp
	 */
	public static Timestamp getCurrentDayStart() {
		Calendar c = Calendar.getInstance();
		Timestamp t = new Timestamp(System.currentTimeMillis());
		c.setTime(t);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return new Timestamp(c.getTimeInMillis());
	}

	/**
	 * 返回测试时间距当前时间共多少分钟 入参date格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return Long
	 */
	public static Long minutesLeftTo(final String date) {
		SimpleDateFormat d = new SimpleDateFormat(YYYYMMDDHHMMSS);// 格式化时间
		String nowtime = d.format(new Date());// 按以上格式 将当前时间转换成字符串
		Long minutes = 0L;
		try {
			minutes = d.parse(nowtime).getTime() - d.parse(date).getTime();
			minutes = minutes / MILLISECONDS_IN_MINUTE;
		} catch (ParseException e) {
			e.getMessage();
		}
		return minutes;
	}

	/**
	 * 返回测试时间距当前时间共多少秒 入参date格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return Long
	 */
	public static Long secondsLeftTo(final String date) {
		SimpleDateFormat d = new SimpleDateFormat(YYYYMMDDHHMMSS); // 格式化时间
		String nowtime = d.format(new Date()); // 按以上格式 将当前时间转换成字符串
		Long seconds = 0L;
		try {
			seconds = (d.parse(nowtime).getTime() - d.parse(date).getTime())
					/ MILLISECONDS_IN_SECOND;
		} catch (ParseException e) {
			e.getMessage();
		}
		return seconds;
	}
}
