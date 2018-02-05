package rabbitmq;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

public class SendAndReceive {

	public static void main(String[] args) throws IOException, TimeoutException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
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
		
		//获得随机队列名称
		String queueName = channel.queueDeclare().getQueue();
		//绑定到路由上
		channel.queueBind(queueName, exchangeName, "");
		
		String message = "hello world";
		System.out.println("请输入,输入exit退出:");
		while(true) {
			//输入exit退出
			if(message.equals("exit")){
				break;
			}
			Scanner sc = new Scanner(System.in);
			message = sc.nextLine();
			//basicPublish(String exchange, String routingKey,BasicProperties props, byte[] body)
			//第一个参数交换机名称，第二个是是消息的路由Key，第三个参数props是消息包含的属性信息，第四个是消息体
			channel.basicPublish(exchangeName, "", null, message.getBytes());
			
			DefaultConsumer consumer = new DefaultConsumer(channel) {
				//获取到达的消息
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
				String msg = new String(body, "UTF-8");
				System.out.println(" 接受到的消息： '" + msg + "'");
				}
			};
			channel.basicConsume(queueName, true,consumer);
		}

	}
	
	private void receive() {
		
	}

}
