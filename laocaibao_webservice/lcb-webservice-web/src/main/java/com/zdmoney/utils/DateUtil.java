/*
 * Copyright (c) 2015 zendaimoney.com. All Rights Reserved.
 */
package com.zdmoney.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * DateUtil
 * <p/>
 * Author: Panjy Date: 2015/6/24 11:43 Mail: panjy@zendaimoney.com $Id$
 */
public class DateUtil {

	public static final String YMDSTR = "yyyy-MM-dd";
	public static final String fullFormat = "yyyy-MM-dd HH:mm:ss";
	public static final String dateFormat="yyyyMMddhhmmss";
	public static final String TOTALFORMAT="yyyyMMddHHmmssSSS";
	public static final String fullFormat1 = "yyyy-MM-dd HH:mm";
	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String YYYYMM = "yyyyMM";
	/**
	 * 两个日期相减得天数
	 * 
	 * @param enddate
	 *            结束日期 date类型
	 * @param begindate
	 *            开始日期 date类型
	 * @return 天数
	 */
	public static int getIntervalDays(Date enddate, Date begindate) {
		long millisecond = enddate.getTime() - begindate.getTime();
		int day = (int) (millisecond / 24L / 60L / 60L / 1000L);
		return day;
	}

	/**
	 * 返回日期当天的开始时间
	 * @param date 传入日期
	 * @return	返回传入日期当天的开始时间，00:00:00
	 */
	public static Date getDateBeginTime(Date date) {
		String beginString = DateUtil
				.getDateFormatString(date, DateUtil.YMDSTR) + " 00:00:00";
		DateFormat df = new SimpleDateFormat(DateUtil.fullFormat);
		try {
			date = df.parse(beginString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static int getIntervalDays2(Date enddate, Date begindate) {
		Date tmpBegindate = getDateBeginTime(begindate);
		Date tmpEnddate	  = getDateEndTime(enddate);

		return getIntervalDays(tmpEnddate,tmpBegindate);
	}

	/**
	 * 将日期转化为指定模式的字符串
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static String timeFormat(Date time, String pattern){
		if (time == null)
			return null;

		SimpleDateFormat simpleDateFormat =
			new SimpleDateFormat(pattern);
		return simpleDateFormat.format(time);
	}

	/**
	 * 返回日期的结束时间
	 * @param date	传入日期
	 * @return	返回传入日期当天的开始时间，23:59:59
	 */
	public static Date getDateEndTime(Date date) {
		String endString = DateUtil.getDateFormatString(date, DateUtil.YMDSTR)
				+ " 23:59:59";
		DateFormat df = new SimpleDateFormat(DateUtil.fullFormat);
		try {
			date = df.parse(endString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 日期转换成字符串
	 * 
	 * @param date
	 * @param formatPattern
	 * @return
	 */
	public static String getDateFormatString(Date date, String formatPattern) {
		SimpleDateFormat format = new SimpleDateFormat(formatPattern);
		return format.format(date);
	}

	/**
	 * 判断两时间是否是同一天
	 * 
	 * @param from
	 *            第一个时间点
	 * @param to
	 *            第二个时间点
	 * @return true:是同一天,false:非同一天
	 */
	public static boolean isSameDay(Date from, Date to) {
		boolean isSameDay = false;
		DateFormat df = new SimpleDateFormat(DateUtil.YMDSTR);
		String firstDate = df.format(from);
		String secondDate = df.format(to);
		isSameDay = firstDate.equals(secondDate);
		return isSameDay;
	}

	/**
	 * 返回yyyy-MM-dd格式的字符串
	 * @param date
	 * @return
	 */
	public static String dateToString(java.util.Date date) {
		if (date == null)
			return null;

		SimpleDateFormat simpleDateFormat =
			new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(date);
	}
	/**
	 * 取得在指定时间上加减days天后的时间
	 * 
	 * @param date
	 *            指定的时间
	 * @param days
	 *            天数,正为加，负为减
	 * @return 在指定时间上加减days天后的时间
	 */
	public static Date addDays(Date date, int days) {
		Date time = null;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, days);
		time = cal.getTime();
		return time;
	}

	/**
	 * 取得两时间相差的天数
	 * 
	 * @param from
	 *            第一个时间
	 * @param to
	 *            第二个时间
	 * @return 相差的天数
	 */
	public static long getBetweenDays(Date from, Date to) {
		long days = 0;
		long dayTime = 24 * 60 * 60 * 1000;
		long fromTime = from.getTime();
		long toTime = to.getTime();
		long times = fromTime - toTime;
		days = times / dayTime;
		return days;
	}

	public static Date getBeforeMonthsDate(Date date, int count) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -count);
		return cal.getTime();
	}
	
	public static Date getDateBefore(Date d, int day) {   
        Calendar now = Calendar.getInstance();   
        now.setTime(d);   
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);   
        return now.getTime();   
    }
	
	public static String getTimestamp() {
		SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return formate.format(new Timestamp(System.currentTimeMillis()));
	}

	public static int compareStringDate(Date date, Date anotherDate) {
		String dateStr = getDateFormatString(date,"yyyyMMdd");
		String anotherDateStr =  getDateFormatString(anotherDate,"yyyyMMdd");
        return dateStr.compareTo(anotherDateStr);
	}

	/**
	 * 取得在指定时间上加减minutes后的时间
	 *
	 * @param date
	 *            指定的时间
	 * @param minutes
	 *            分钟数,正为加，负为减
	 * @return 在指定时间上减minutes后的时间
	 */
	public static Date addMinutes(Date date, int minutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minutes);
		Date time = cal.getTime();
		return time;
	}

	/**
	 * 比较日期大小
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareDate(String date1, String date2) {
		DateFormat df = new SimpleDateFormat(DateUtil.fullFormat);
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				System.out.println("dt1大于dt2");
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				System.out.println("dt1小于dt2");
				return -1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 取得在指定时间上加减days天后的时间
	 *
	 * @param date
	 *            指定的时间
	 * @param days
	 *            天数,正为加，负为减
	 * @return 在指定时间上加减days天后的时间
	 */
	public static Date plusDay(Date date, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, days);
		Date time = calendar.getTime();
		return time;
	}

	public static String getDistanceTime(Date early, Date late) {
		String date = "";
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		try {
			long time1 = early.getTime();
			long time2 = late.getTime();
			long diff ;
			if (time1<time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			day = diff / (24 * 60 * 60 * 1000);
			hour = (diff / (60 * 60 * 1000) - day * 24);
			min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
			sec = (diff/1000-day*24*60*60-hour*60*60-min*60);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return day + "天" + hour + "小时" + min + "分" + sec + "秒";
	}

	public static boolean isBetweenTwoDate(Date begin,Date end){
		boolean flag = false;
		try {
			long beginTime = getTime(begin,0).getTime();
			long endTime = getTime(end,1).getTime();
			Date curDate = new Date();
			if(curDate.getTime()>=beginTime && curDate.getTime()<=endTime){
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;

	}

	public static Date getTime(Date date,int flag)throws ParseException {
		SimpleDateFormat sdfDate = new SimpleDateFormat(YMDSTR);
		String temp = "";
		if(flag == 0){
			temp = sdfDate.format(date)+" 00:00:00";
		}
		else{
			temp = sdfDate.format(date)+" 23:59:59";
		}
		SimpleDateFormat sdfTime = new SimpleDateFormat(fullFormat);
		return sdfTime.parse(temp);
	}

	/**
	 *将字符串格式yyyyMMdd的字符串转为日期，格式"yyyy-MM-dd"
	 *
	 * @param date 日期字符串
	 * @return 返回格式化的日期
	 * @throws ParseException
	 */
	public static String strToDateFormat(String date) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(YYYYMMDD);
		formatter.setLenient(false);
		Date newDate= formatter.parse(date);
		System.out.println(".........."+newDate);
		formatter = new SimpleDateFormat(YMDSTR);
		return formatter.format(newDate);
	}
	public static Date strFormatToDate(String date,String formatFlag) {
		Date newDate=null;
		SimpleDateFormat formatter = new SimpleDateFormat(formatFlag);
		try{
			formatter.setLenient(false);
			 newDate= formatter.parse(date);
		}catch (Exception e){
			e.printStackTrace();
		}
		return newDate;
	}

	public static String getFuiouTerm(Date date, int year) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(c.YEAR, year);
		return getDateFormatString(c.getTime(), YYYYMMDD);
	}

	/**
	 * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
	 *
	 * @param nowTime 当前时间
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 * @author huangcy
	 */
	public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
		if (nowTime.getTime() == startTime.getTime()
				|| nowTime.getTime() == endTime.getTime()) {
			return true;
		}

		Calendar date = Calendar.getInstance();
		date.setTime(nowTime);

		Calendar begin = Calendar.getInstance();
		begin.setTime(startTime);

		Calendar end = Calendar.getInstance();
		end.setTime(endTime);

		if (date.after(begin) && date.before(end)) {
			return true;
		} else {
			return false;
		}
	}

	public static int getDaysBetweenTwoDate(Date startDate, Date endDate){
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(startDate);
		int startDay = calendar.get(Calendar.DAY_OF_YEAR);
		int startYear = calendar.get(Calendar.YEAR);
		calendar.setTime(endDate);
		int endYear = calendar.get(Calendar.YEAR);
		int interval = calendar.get(Calendar.DAY_OF_YEAR) - startDay;
		interval += (endYear-startYear)*365;
		return interval;
	}

	public static void main(String[] args) throws ParseException {

			Long s = System.currentTimeMillis();

			System.out.println("时间:"+(System.currentTimeMillis()-s));
	}
}