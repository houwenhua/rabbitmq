package rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class Consumer {

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
		
		//声明路由类型和名称
		channel.exchangeDeclare(exchangeName, "fanout");
		//获得随机队列名称
		String queueName = channel.queueDeclare().getQueue();
		//绑定到路由上
		channel.queueBind(queueName, exchangeName, "");
		
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(queueName, true,consumer);
		while(true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
            System.out.println("接收信息： '" + message + "'");
		}

	}

}
