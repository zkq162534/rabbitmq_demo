package com.zkq.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zkq.enums.ExchangeTypesEnum;
import com.zkq.util.ConnectionUtil;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * @Author zhangkaiqiang
 * @Date 2019/9/15  20:42
 * @Description TODO
 *
 *
 * exchange路由规则---Direct
 *
 *1.direct的路由规则很简单,把消息路由到那些binding key与routing key完全匹配的Queue中
 *
 *(1)消息在发往exchange时，会指定一个routing key,routing key是用于进行路由匹配的关键字
 *(2)在进行交换器(exchange)与队列(queue)进行绑定时，可以指定binding key,用于交换器将消息发送到相应的队列中
 */
public class BindingPublish {


	private final static  String EXCHANGE_NAME="direct";

	public static void send(){
		try(Connection connection=ConnectionUtil.getConnection();
			Channel channel=connection.createChannel()){

			// 声明交换器
			channel.exchangeDeclare(EXCHANGE_NAME, ExchangeTypesEnum.DIRECT.getName());

			// 待发送的消息的集合
			List<Map<String,String>> list=new LinkedList<>();

			// 待发送的消息--键为消息，值为routing key
			Map<String,String> info=new HashMap<>();
			info.put("info","info");

			Map<String,String> error=new HashMap<>();
			error.put("error","error");

			Map<String,String> update=new HashMap<>();
			update.put("update","update");

			Map<String,String> trace=new HashMap<>();
			trace.put("trace","trace");

			list.add(info);
			list.add(error);
			list.add(update);
			list.add(trace);

			// 遍历集合进行消息的发送
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
