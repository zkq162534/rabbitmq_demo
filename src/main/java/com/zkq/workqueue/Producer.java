package com.zkq.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zkq.util.ConnectionUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author zhangkaiqiang
 * @Date 2019/9/12  15:42
 * @Description 工作队列中的生产者
 *
 *1.什么是工作队列？
 * 工作队列--用来将耗时的任务分发给多个消费者（工作者），主要解决这样的问题：处理资源密集型任务，并且还要等他完成。
 * 有了工作队列，我们就可以将具体的工作放到后面去做，将工作封装为一个消息，发送到队列中，一个工作进程就可以取出消息并完成工作
 *
 * 2.消费者的消息确认机制--非自动确认
 * (1)自动确认会发生消息丢失/消费者完成任务后确认避免消息丢失
 * (2)channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
 *
 * 3.MQ的消息分发机制--公平分发
 * (1)默认的是mq一接收到任务就分发,通过下面设定让mq每次只发生一个服务到消费者
 * (2)channel.basicQos(1);
 */
@Slf4j
public class Producer {


	/**
	 * 队列名称
	 */
	private final static String QUEUE_NAME = "TASK_QUEUE";

	/**
	 * 	需要发送的消息列表
	 */
	public static final String[] msgs = {"sleep", "task 2", "task 3", "task 4", "task 5", "task 6"};

	public static void send(){
		try (Connection connection = ConnectionUtil.getConnection();
			 // 从连接中创建通道
			 Channel channel = connection.createChannel();
		){

			// 声明（创建）队列
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);

			/**
			 * 向MQ中发送多个任务
			 * void basicPublish​(String exchange, String routingKey, AMQP.BasicProperties props, byte[] body) throws IOException
			 * exchange表示将消息发布到的交换器exchange
			 * routingKey表示路由密钥
			 * props表示消息的其他属性 - 路由头等
			 * body表示消息体
			 */
			for (int i = 0; i < msgs.length; i++) {
				channel.basicPublish("", QUEUE_NAME, null, msgs[i].getBytes());
				System.out.println(" [x] Sent '" + msgs[i] + "'");
			}

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
