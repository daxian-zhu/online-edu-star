package com.clark.daxian.api.util.date;

import org.apache.commons.lang3.math.NumberUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 日期相关工具类
 * @author 大仙
 *
 */
public class DateUtils {
	/**
	 * 年月日时分秒
	 */
	public static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 年月日
	 */
	public static final String YMD  = "yyyy-MM-dd";
	/**
	 * 时分
	 */
	public static final String HM = "HH:mm";
	/**
	 * 年月日
	 */
	public static final String YYYYMMDD = "yyyyMMdd";
	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getNowDateTime() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern(YMDHMS);
		return LocalDateTime.now().format(format);
	}

	/**
	 * 格式化对象
	 * @param fm
	 * @return
	 */
	public static String getNowDateTimeByFormat(String fm){
		DateTimeFormatter format = DateTimeFormatter.ofPattern(fm);
		return LocalDateTime.now().format(format);
	}

	/**
	 * 判断两个时间段是否有交集
	 * @param startDate1
	 * @param endDate1
	 * @param startDate2
	 * @param endDate2
	 * @return
	 */
	public static Boolean isIntersection(LocalDate startDate1, LocalDate endDate1, LocalDate startDate2, LocalDate endDate2) {
		if (startDate1.isAfter(endDate2) || startDate2.isAfter(endDate1)) {
			return false;
		}
		return true;
	}

	/**
	 * 对象转时间格式
	 * @param obj
	 * @return
	 */
	public static LocalDateTime getLoalDateTime(Object obj) {

		if(obj==null){
			return LocalDateTime.now();
		}
		if(obj instanceof Long){
			long timestamp = NumberUtils.toLong(String.valueOf(obj));
			Instant instant = Instant.ofEpochMilli(timestamp);
			ZoneId zone = ZoneId.systemDefault();
			return LocalDateTime.ofInstant(instant, zone);
		}
		if(obj instanceof String){
			DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss");
			return LocalDateTime.parse(String.valueOf(obj), df);
		}
		return LocalDateTime.now();
	}
}
