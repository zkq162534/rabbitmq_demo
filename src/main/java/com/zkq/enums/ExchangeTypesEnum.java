package com.zkq.enums;

/**
 * @Author zhangkaiqiang
 * @Date 2019/9/14  16:20
 * @Description 交换器类型
 */
public enum ExchangeTypesEnum {

	/**
	 * 把消息路由到那些binding key与routing key完全匹配的Queue中
	 */
	DIRECT(1,"direct"),
	/**
	 * 根据发送的消息内容中的headers属性进行匹配
	 */
	TOPIC(2,"topic"),
	/**
	 *
	 */
	HEADERS(3,"headers"),

	/**
	 * 把所有发送到该Exchange的消息路由到所有与它绑定的Queue中
	 */
	FANOUT(4,"fanout");

	/**
	 *类型编码
	 */
	private int code;
	/**
	 * 类型名称
	 */
	private String name;

	ExchangeTypesEnum(int code,String name){
		this.code=code;
		this.name=name;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
