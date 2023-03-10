package direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Consumer {
    private final static String QUEUE_NAME = "test2";

    public static void main(String[] args) throws Exception {
        ConnectionFactory facotry = new ConnectionFactory();
        facotry.setHost("localhost");
        Connection connection = facotry.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(message);
        };
        channel.basicConsume(QUEUE_NAME, true, "", deliverCallback, consumerTag -> {
        });
    }
}