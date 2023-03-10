package fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Consumer2 {
    private final static String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        ConnectionFactory facotry = new ConnectionFactory();
        facotry.setHost("localhost");
        Connection connection = facotry.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String queue_name = channel.queueDeclare().getQueue();
        channel.queueBind(queue_name, EXCHANGE_NAME, "");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(message);
        };
        channel.basicConsume(queue_name, true, "", deliverCallback, consumerTag -> {
        });
    }
}
