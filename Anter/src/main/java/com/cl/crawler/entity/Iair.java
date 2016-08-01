package com.cl.crawler.entity;

/**
 * 机票查询参数接口
 * 
 * @author chenlei
 *
 */
public interface Iair {

	/** 需要登录：0 */
	public final static int LOGONG_YES = 0;
	/** 不需要登录：1 */
	public final static int LOGONG_NO = 1;

	/** 国内："0" */
	public final static String DOMESTIC = "0";
	/** 国际/港澳台："1" */
	public final static String INTERNATIONAL = "1";
	/** 通用："2" */
	public final static String COMMON = "2";

	/** 乘客类型：成人 */
	public final static String adult = "A";
	/** 乘客类型：儿童 */
	public final static String child = "C";
	/** 乘客类型：婴儿 */
	public final static String infant = "I";

	/** 航程类型：单程 */
	public final static String singleTrip = "S";
	/** 航程类型：往返 */
	public final static String returnTrip = "R";

}
