package routing_key;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Consumer1 {
    private final static  String EXCHANGE_NAME = "log";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");
        String queue_name=channel.queueDeclare().getQueue();
        channel.queueBind(queue_name,EXCHANGE_NAME,"info");
        DeliverCallback deliverCallback=(consumerTag,delivery)->{
            String message = new String(delivery.getBody());
            System.out.println(message);
        };
        channel.basicConsume(queue_name,true,deliverCallback,consumerTag->{});
    }
}