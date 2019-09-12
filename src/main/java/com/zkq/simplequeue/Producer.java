package com.zkq.simplequeue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zkq.util.ConnectionUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author zhangkaiqiang
 * @Date 2019/9/11  14:43
 * @Description 简单队列模式中的生产者
 */
@Slf4j
public class Producer {

	final static String QUEUE_NAME = "hello";

	public static void send(){
		try (Connection connection = ConnectionUtil.getConnection();
			 // 从连接中创建通道
			 Channel channel = connection.createChannel();
			){

			// 声明（创建）队列
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);

			// 生产者要发送到mq的消息内容
			String message = "Hello World!";

			/**
			 * 向MQ中发送消息
			 * void basicPublish​(String exchange, String routingKey, AMQP.BasicProperties props, byte[] body) throws IOException
			 * exchange表示将消息发布到的交换器exchange
			 * routingKey表示路由密钥
			 * props表示消息的其他属性 - 路由头等
			 * body表示消息体
			 */
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
			System.out.println(" [x] Sent '" + message + "'");

		} catch (TimeoutException e) {
			log.debug("send message to mq timeout,the detailed reasons is :"+e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			log.debug("can not sen message to mq,the detailed reasons is :"+e.toString());
		}
	}

	public static void main(String[] args) {
		send();
	}
}
