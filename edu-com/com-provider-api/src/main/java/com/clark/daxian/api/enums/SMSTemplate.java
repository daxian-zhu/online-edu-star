package com.clark.daxian.api.enums;

/**
 * 短信模板枚举
 * @author 大仙
 *
 */
public enum SMSTemplate {
	//验证码
	SMS_141606808("{\"code\":\"%s\"}");

	private String display;

	SMSTemplate(String display) {
		this.display = display;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}
}
