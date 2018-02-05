package rabbitmq;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {

	public static void main(String[] args) throws IOException, TimeoutException {
		//创建工厂
		ConnectionFactory cf = new ConnectionFactory();
		//使用默认端口
		cf.setHost("localhost");
		//创建连接
		Connection connection = cf.newConnection();
		//声明消息通道
		Channel channel = connection.createChannel();
		
		//交换机名字
		String exchangeName = "testEXG";	
		//声明路由类型
		channel.exchangeDeclare(exchangeName,"fanout");
		
		String message = "hello world";
		while(true) {
			System.out.println("请输入,输入exit退出:");
			Scanner sc = new Scanner(System.in);
			message = sc.nextLine();
			//basicPublish(String exchange, String routingKey,BasicProperties props, byte[] body)
			//第一个参数交换机名称，第二个是是消息的路由Key，第三个参数props是消息包含的属性信息，第四个是消息体
			channel.basicPublish(exchangeName, "", null, message.getBytes());
			//输入exit退出
			if(message.equals("exit")){
				break;
			}
		}
		//关闭管道和连接
		channel.close();
		connection.close();

	}

}
