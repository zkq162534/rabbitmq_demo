package com.zkq.publishandsubscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zkq.enums.ExchangeTypesEnum;
import com.zkq.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author zhangkaiqiang
 * @Date 2019/9/14  16:04
 * @Description 发布订阅模式中的发布者
 *
 * 1.交换器
 * (1)RabbitMQ中消息传递模型的核心思想是生产者从来不会直接把消息传送到队列,而是把消息发送到交换器(exchanges)中
 * (2)交换器的类型:direct, topic, headers and fanout
 * (3)fanout会把消息转发给所有与该交换器绑定的队列
 *
 * 2.随机队列
 * (1)生产者与消费者必须连接的是同一个队列才能进行工作
 * (2)queueDeclare()方法不加参数时会生成一个随机名称,非永久的,独占的,自动删除的队列
 *
 * 3.绑定--绑定交换器(exchange)与队列(queue)
 *
 */
public class Publish {


	private final static String EXCHANGE_NAME="logs";


	public static void publish(){
		try(Connection connection = ConnectionUtil.getConnection();
			Channel channel=connection.createChannel();){

			/**
			 * 创建一个指定路由规则为FANOUT的exchange
			 */
			channel.exchangeDeclare(EXCHANGE_NAME, ExchangeTypesEnum.FANOUT.getName());

			// 待发送的消息
			String message="Hello World";

			// 进行消息的发送
			channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes());

			System.out.println(" [x] Sent '" + message + "'");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		publish();
	}
}
