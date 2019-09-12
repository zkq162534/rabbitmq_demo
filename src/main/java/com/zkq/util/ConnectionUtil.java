package com.zkq.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

/**
 * @Author zhangkaiqiang
 * @Date 2019/9/11  14:11
 * @Description 连接RabbitMQ的工具类
 */
@Slf4j
public class ConnectionUtil {

	/**
	 * ip地址
	 */
	private final static String URL;
	/**
	 * 端口号
	 */
	private  final static String PORT;

	private  final static String USER_NAME;

	private  final static String PASSWORD;

	/**
	 * 连接那个主机
	 */
	private  final static String HOST;


	// 加载配置文件中的配置
	static{
		Properties properties = new Properties();
		try {
			properties.load(ConnectionUtil.class.getResourceAsStream("/rabbitmq.properties"));
		} catch (IOException e) {
			log.debug("can not load properties,the detailed reasons is :"+e.toString());
		}
		URL=properties.getProperty("rabbitmq.url");
		PORT=properties.getProperty("rabbitmq.port");
		HOST=properties.getProperty("rabbitmq.host");
		USER_NAME=properties.getProperty("rabbitmq.username");
		PASSWORD=properties.getProperty("rabbitmq.password");
	}

	public static Connection getConnection(){

		//定义连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		//设置服务地址
		factory.setHost(URL);
		//端口
		factory.setPort(Integer.valueOf(PORT));
		//设置账号信息，用户名、密码、vhost
		factory.setVirtualHost(HOST);
		factory.setUsername(USER_NAME);
		factory.setPassword(PASSWORD);
		// 通过工程获取连接
		Connection connection = null;
		try {
			connection = factory.newConnection();
		} catch (IOException e) {
			log.debug("can not get connection,the detailed reasons is :"+e.toString());
		} catch (TimeoutException e) {
			log.debug("get connection timeout,the detailed reasons is :"+e.toString());
		}
		return connection;
	}



	public static void main(String[] args) throws IOException {
		Connection con=getConnection();
	}

}
