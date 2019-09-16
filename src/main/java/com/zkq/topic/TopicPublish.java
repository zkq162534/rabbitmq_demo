package com.zkq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zkq.enums.ExchangeTypesEnum;
import com.zkq.util.ConnectionUtil;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * @Author zhangkaiqiang
 * @Date 2019/9/16  18:43
 * @Description
 *
 * 1.topic类型的交换器
 * (1)routing_key和binding key必须是使用.号分割的单词列表
 * (2)binding key可以使用*和#,其中*用于匹配一个单词,#用于匹配0个或者多个单词
 */
public class TopicPublish {

	private final static String EXCHANGE_NAME="topic";

	public static void send(){
		try(Connection connection= ConnectionUtil.getConnection();
			Channel channel=connection.createChannel()){

			// 声明exchange
			channel.exchangeDeclare(EXCHANGE_NAME, ExchangeTypesEnum.TOPIC.getName());

			// 待发送的消息的集合
			List<Map<String,String>> list=new LinkedList<>();

			// 待发送的消息--键为消息，值为routing key
			Map<String,String> quickOrangeRabbit=new HashMap<>();
			quickOrangeRabbit.put("quick.orange.rabbit","quick.orange.rabbit");

			Map<String,String> lazyOrangeElephant=new HashMap<>();
			lazyOrangeElephant.put("lazy.orange.elephant","lazy.orange.elephant");

			Map<String,String> quickOrangeFox=new HashMap<>();
			quickOrangeFox.put("quick.orange.fox","quick.orange.fox");

			Map<String,String> lazyBrownFox=new HashMap<>();
			lazyBrownFox.put("lazy.brown.fox","lazy.brown.fox");

			Map<String,String> lazyPinkRabbit=new HashMap<>();
			lazyPinkRabbit.put("lazy.pink.rabbit","lazy.pink.rabbit");

			Map<String,String> quickBrownFox=new HashMap<>();
			quickBrownFox.put("quick.brown.fox","quick.brown.fox");

			list.add(quickOrangeRabbit);
			list.add(lazyOrangeElephant);
			list.add(quickOrangeFox);
			list.add(lazyBrownFox);
			list.add(lazyPinkRabbit);
			list.add(quickBrownFox);


			list.forEach((item)->{
				Iterator<String> iterator = item.keySet().iterator();
				String next = iterator.next();
				// 交换器名称  路由规则   null   消息数组
				try {
					channel.basicPublish(EXCHANGE_NAME,item.get(next),null,next.getBytes());
					System.out.println("[X] send routing key is " +item.get(next)+" , message is "+next);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		send();
	}
}
