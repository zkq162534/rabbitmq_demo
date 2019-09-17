package com.zkq.springandrabbitmq.service;

/**
 * @Author zhangkaiqiang
 * @Date 2019/9/17  16:02
 * @Description TODO
 */
public interface MqProviderService {

	/**
	 * 发送消息
	 * @param key routing key
	 * @param obj the message wan to sen
	 */
	void sendData(String key,Object obj);
}
