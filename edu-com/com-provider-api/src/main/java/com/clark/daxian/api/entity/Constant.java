package com.clark.daxian.api.entity;

/**
 * 常量类
 * @author 大仙
 */
public class Constant {

	/**
	 * 成功
	 */
	public static final String SUCCESS_MSG = "success";
	/**
	 * 错误
	 */
    public static final String ERROR_MSG = "failed";
	/**
	 * 正确业务码
	 */
    public static final Integer SUCCESS_CODE = 0;

	/**
	 * 错误业务码
	 */
    public static final Integer FAIL_CODE = 500;
	/**
	 * 权限头
	 */
	public static final String AUTHORIZATION = "Authorization";

	/**
	 * 初始密码
	 */
	public static final String INIT_PWD = "clark";
	/**
	 * 用户信息
	 */
	public static final String USER_INFO = "user_info";

	/**
	 * redis存储前缀
	 */
	public static final String PERMISSIONS = "permissions:";
	/**
	 * 所有的权限
	 */
	public static final String ALL = "all";
	/**
	 * redis存储前缀
	 */
	public static final String KEYPER = "websocket:";


}