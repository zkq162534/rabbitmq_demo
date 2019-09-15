package com.zkq.publishandsubscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.zkq.enums.ExchangeTypesEnum;
import com.zkq.util.ConnectionUtil;

import java.io.IOException;

/**
 * @Author zhangkaiqiang
 * @Date 2019/9/14  16:05
 * @Description 发布订阅模式中的订阅者
 */
public class Subscribe1 {

	private final static String EXCHANGE_NAME="logs";

	private String queueName;

	public static void subscribe(){
		try{
			Connection connection= ConnectionUtil.getConnection();
			Channel channel=connection.createChannel();


			channel.exchangeDeclare(EXCHANGE_NAME, ExchangeTypesEnum.FANOUT.getName());


			/**
			 * 当没有指定参数时会产生一个非持久的,独占的,自动删除的队列，队列名称会自动生成
			 * 也就是queueDeclare​(String queue, boolean durable, boolean exclusive, boolean exclusive, Map<String,​Object> arguments) throws IOException
			 * 方法中durable默认为false,exclusive和exclusive默认为true
			 */
			String queueName=channel.queueDeclare().getQueue();

			// 将队列和交换器进行绑定
			channel.queueBind(queueName, EXCHANGE_NAME, "");

			System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), "UTF-8");
				System.out.println(" [x] Received '" + message + "'");
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			};
			channel.basicConsume(queueName, false, deliverCallback, consumerTag -> { });

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		subscribe();
	}
}
