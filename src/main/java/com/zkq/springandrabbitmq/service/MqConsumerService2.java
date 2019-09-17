package com.zkq.springandrabbitmq.service;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * @Author zhangkaiqiang
 * @Date 2019/9/17  16:04
 * @Description TODO
 */
public class MqConsumerService2 implements MessageListener {
	@Override
	public void onMessage(Message message) {
		System.out.println(this.getClass().toString()+"消费者消费："+ new String(message.getBody()));
	}

	@Override
	public void containerAckMode(AcknowledgeMode mode) {

	}
}
