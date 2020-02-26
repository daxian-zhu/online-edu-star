package com.clark.daxian.api.util;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公共工具类
 * 
 * @author 大仙
 *
 */
public class PubUtils {
	/**
	 * 基础字符
	 */
	private static final String BASE_LETTERS = "0123456789abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	/**
	 * 基础数字
	 */
	private static final String BASE_NUM_LETTERS = "0123456789";
	/**
	 * 老师内部
	 */
	public static final String TEACHER_INSIDE = "1888888";
	/**
	 * 学生内部
	 */
	public static final String STUDENT_INSIDE = "1666666";

	/**
	 * 是否内部老师
	 * @param telephone
	 * @return
	 */
	public static Boolean judgeTeacherInside(String telephone){
		if(telephone!=null){
			if(telephone.length()==11&&telephone.startsWith(TEACHER_INSIDE)){
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断学生是否内部
	 * @param telephone
	 * @return
	 */
	public static Boolean judgeStudentInside(String telephone){
		if(telephone!=null){
			if(telephone.length()==11&&telephone.startsWith(STUDENT_INSIDE)){
				return true;
			}
		}
		return false;
	}
	/**
	 * 小数四舍五入方法
	 *
	 * @param d 小数
	 * @return
	 */
	public static double doubleFormat(double d) {
		DecimalFormat df = new DecimalFormat("#.00");
		return Double.valueOf(df.format(d));
	}

	/**
	 * 特殊字符转义，实现对一些特殊字符的转义操作，防止处理的时候出错 主要的目的是防止XSS攻击
	 * 
	 * @param text
	 * @return
	 */
	public static String transfString(String text) {

		if (null != text) {
			text = text.replace(">", "&gt;");
			text = text.replace("<", "&lt;");
			text = text.replace(" ", "&nbsp;");
			text = text.replace("\"", "&quot;");
			text = text.replace("\'", "&#39;");
			text = text.replace("\\", "\\\\");
			text = text.replace("\n", "\\n");
			text = text.replace("\r", "\\r");
		}

		return text;
	}

	/**
	 * 转义还原，用于用户显示
	 * 
	 * @param text
	 * @return
	 */
	public static String transfBack(String text) {

		if (null != text) {
			text = text.replace("&gt;", ">");
			text = text.replace("&lt;", "<");
			text = text.replace("&nbsp;", " ");
			text = text.replace("&quot;", "\"");
			text = text.replace("&#39;", "\'");
			text = text.replace("\\\\", "\\");
			text = text.replace("\\n", "\n");
			text = text.replace("\\r", "\r");
		}

		return text;
	}

	/**
	 * 生产随机数
	 * @param length
	 * @return
	 */
	public static String generateRandomFullCode(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int num = (int) (Math.random() * 62);
			sb.append(BASE_LETTERS.charAt(num));
		}
		return sb.toString();
	}

	/**
	 * 获取随机值
	 * @param length
	 * @return
	 */
	public static String generateRandomNumbersCode(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int num = (int) (Math.random() * 10);
			sb.append(BASE_NUM_LETTERS.charAt(num));
		}
		return sb.toString();
	}
	/**
	 * 获取唯一Id
	 * @return
	 */
	public static String getId() {
		String uuid = UUID.randomUUID().toString();
		return Md5Utils.getMD5Uppercase(uuid);
	}

	/**
	 * 判断一个数字是否是手机号
	 *
	 * @param telephone
	 * @return
	 */
	public static boolean isTeleNum(String telephone) {
		if (StringUtils.isEmpty(telephone)) {
			return false;
		}
		Pattern p = Pattern.compile("^1[0-9]{10}$");
		Matcher m = p.matcher(telephone);
		return m.matches();
	}

	/**
	 * 小数点后保留2位有效数字
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");
		Matcher match = pattern.matcher(str);
		return match.matches();
	}
}
