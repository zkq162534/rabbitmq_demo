package com.zkq.routing;

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
 * @Date 2019/9/15  21:15
 * @Description TODO
 */
public class BindingSubscribleError {

	private final static  String EXCHANGE_NAME="direct";



	public static void recive(){


		try{
			Connection connection= ConnectionUtil.getConnection();
			Channel channel=connection.createChannel();

			// 声明交换器
			channel.exchangeDeclare(EXCHANGE_NAME, ExchangeTypesEnum.DIRECT.getName());


			//一个队列可以绑定多个binding key，但是一个消费者只能绑定一个队列
			String queueName=channel.queueDeclare().getQueue();

			// binging key
			List<String> bindKey=new ArrayList<>();
			bindKey.add("error");


			// 循环binding key,把exchange和queue使用binding key 进行绑定
			bindKey.forEach((item)->{
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
