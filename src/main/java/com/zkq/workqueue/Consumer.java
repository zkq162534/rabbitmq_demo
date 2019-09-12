package com.zkq.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.zkq.util.ConnectionUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;

/**
 * @Author zhangkaiqiang
 * @Date 2019/9/11  14:52
 * @Description 简单队列模式中的消费者
 */
@Slf4j
public class Consumer {

	/**
	 * 队列名称
	 */
	private final static String QUEUE_NAME = "TASK_QUEUE";

	/**
	 * 消息是否自动确认
	 */
	private static boolean autoAck=false;

	/**
	 * 每次向消费者分发的消息的数量
	 */
	private final static int prefetchCount=1;

	public static void work(){
		// 不要使用try-with -resources,让消费者一直保持运行
		try {
			Connection connection = ConnectionUtil.getConnection();
			Channel channel = connection.createChannel();

			/**
			 * 声明(创建)一个队列
			 *queueDeclare​(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String,​Object> arguments) throws IOException
			 * queue为队列的名称
			 * durable为true表示声明一个持久队列（队列将在服务器重启后继续存在）
			 * exclusive为true表示声明一个独占队列（仅限于此连接）
			 * autoDelete为true表示声明一个自动删除队列（服务器将在不再使用时将其删除）
			 * arguments表示队列的其他属性（构造参数）
			 */
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			System.out.println(" [*] Waiting for messages. To exit press CTRL+C");


			// mq默认收到消息立即分发出去,没有确认各个工作者未返回确认的消息数量，通过下面的代码告诉RabbitMQ 我每次值处理一条消息，你要等我处理完了再分给我下一个
			channel.basicQos(prefetchCount);

			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), "UTF-8");
				dowork(message);

				// 当消费者处理完消息后进行消息确认，通知MQ这个消息已经处理完
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			};

			/**
			 *
			 * 异步的监听队列，当收到消息时会调用DeliverCallback接口
			 * String basicConsume​(String queue, boolean autoAck, DeliverCallback deliverCallback, CancelCallback cancelCallback) throws IOException
			 * queue表示那个队列
			 * autoAck表示是否自动确认
			 *deliverCallback表示接收到消息时要调用的接口
			 * cancelCallback表示消费者取消时的回调
			 */
			channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, consumerTag -> { });
		} catch (IOException e) {
			log.debug("can not get message from mq,the detailed reasons is :"+e.toString());
		}
	}

	public static void dowork(String task){
		try {
			System.out.println("**** deal task begin :" + task);

			//假装task比较耗时，通过sleep（）来模拟需要消耗的时间
			if (Objects.equals(task,"sleep")) {
				Thread.sleep(1000 * 30);
			} else {
				Thread.sleep(1000);
			}
			System.out.println("**** deal task finish :" + task);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		work();
	}
}
