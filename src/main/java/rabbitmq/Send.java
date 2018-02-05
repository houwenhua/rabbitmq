package rabbitmq;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Send {

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
		String exchangeName = "chatEXG";
		//队列名
		String queueName = "chatQueue";
		//routingKey
		String routingKey = "RountKey1";
		//声明交换机，第一个参数是交换机名字，第二个是四种交换机的一种，第三是是否持久化，定义为是，就是服务器重启时，队列依旧存在。
		channel.exchangeDeclare(exchangeName,"direct",false);
		//queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete,Map<String, Object> arguments)  
        //第一个参数是队列名，第二个是是否持久化，第三个表示是否只进行当前的tcp连接，第四个表示是否进行自动删除队列，第五个主要用于Headers Exchange进行消息匹配时
		channel.queueDeclare(queueName, true, false, false, null);
		//绑定队列和交换机，Queue.BindOk queueBind(String queue, String exchange, String routingKey)
		//第一个参数是队列名，第二个参数是交换机名，第三个参数routingKey是消息队列和Exchange之间绑定的路由key
		channel.queueBind(queueName, exchangeName, routingKey);
		
		String message = "hello world";
		while(true) {
			System.out.println("请输入,输入exit退出:");
			Scanner sc = new Scanner(System.in);
			message = sc.nextLine();
			//basicPublish(String exchange, String routingKey,BasicProperties props, byte[] body)
			//第一个参数交换机名称，第二个是是消息的路由Key，第三个参数props是消息包含的属性信息，第四个是消息体
			channel.basicPublish(exchangeName, routingKey, null, message.getBytes());
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
