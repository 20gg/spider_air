package com.cl.data.entity;

/**
 * 机票数据状态接口
 * 
 * @author chenlei
 *
 */
public interface IdataCode {

	/** 合法: 机票的json数据有效(且有实际数据) */
	public final static int JOSN_201 = 201;
	/** 合法: 机票的json数据有效(且没有数据) */
	public final static int JSON_401 = 401;
	/** 非法: 这不是json数据(可能被封IP了) */
	public final static int JSON_501 = 501;
	/** 非法: 这是登录提示的json数据 */
	public final static int JSON_301 = 301;
	/** 非法: 这是验证码提示的json数据 */
	public final static int JSON_302 = 302;

}
