package com.zkq.springandrabbitmq.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author zhangkaiqiang
 * @Date 2019/9/17  16:03
 * @Description TODO
 */
@Component
public class MqProviderServiceImpl implements MqProviderService {

	@Autowired

	private AmqpTemplate amqpTemplate;


	@Override
	public void sendData(String key, Object obj) {
		amqpTemplate.convertAndSend(key, obj);
	}
}
