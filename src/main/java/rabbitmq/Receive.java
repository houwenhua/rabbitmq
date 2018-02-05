package rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class Receive {

	//private static final Logger logger = LoggerFactory.getLogger(Receive.class);
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
		
		QueueingConsumer consumer = new QueueingConsumer(channel);
		//basicConsume(String queue, boolean autoAck, Consumer callback)
		//第一个参数是队列名，第二个是否是自动确认，自动发确认消息(Ack消息)给消息队列，消息队列会将这条消息从消息队列里删除，第三个参数就是Consumer对象，用于处理接收到的消息。
		channel.basicConsume(queueName, true, consumer);
		while(true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
            System.out.println("接收信息： '" + message + "'");
            //回复ack包，不回复，队列不删除,上面的basicConsume(QUEUE_NAME, true, consumer);第二个参数已经是是否自动确认
            //channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
		}

	}

}
