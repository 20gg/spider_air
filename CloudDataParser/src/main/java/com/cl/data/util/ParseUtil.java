package com.cl.data.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 解析器工具类
 * 
 * @author chenlei
 *
 */
public class ParseUtil {

	private ParseUtil() {
	}

	
	
	/**
	 * 日期字符串转Date
	 * 
	 * @param datestr
	 * @param regex
	 * @return
	 * @throws ParseException
	 */
	public static Date getDate(final String datestr, final String regex) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat(regex);
		return sdf.parse(datestr);

	}

	/**
	 * Date转日期字符串
	 * 
	 * @param date
	 * @param regex
	 * @return
	 */
	public static String getDateStr(final Date date, final String regex) {
		SimpleDateFormat sdf = new SimpleDateFormat(regex);
		return sdf.format(date);
	}

}
