package com.zkq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.zkq.enums.ExchangeTypesEnum;
import com.zkq.util.ConnectionUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhangkaiqiang
 * @Date 2019/9/16  18:44
 * @Description TODO
 */
public class TopicSubscrible2 {

	private final static String EXCHANGE_NAME="topic";

	public static void recive(){

		try{
			 Connection connection = ConnectionUtil.getConnection();
			Channel channel=connection.createChannel();

			// 声明交换器
			channel.exchangeDeclare(EXCHANGE_NAME, ExchangeTypesEnum.TOPIC.getName());

			// 声明队列
			String queueName=channel.queueDeclare().getQueue();

			// routing key
			List<String> routingKey=new ArrayList<>();
			routingKey.add("*.*.rabbit");
			routingKey.add("lazy.#");


			// 循环routing key 集合绑定交换器和队列
			routingKey.forEach((item)->{
				try {
					channel.queueBind(queueName,EXCHANGE_NAME,item);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

			System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

			// 进行消息处理的接口
			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), "UTF-8");
				System.out.println(" [x] Received routing key is：" + delivery.getEnvelope().getRoutingKey() + "  , message is:" + message);
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			};

			// 异步监听队列,进行消息的消费
			channel.basicConsume(queueName, false, deliverCallback, consumerTag -> { });


		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		recive();
	}
}
