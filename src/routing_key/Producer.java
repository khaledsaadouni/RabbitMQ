package routing_key;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;

public class Producer {

    private final static String EXCHANGE_NAME = "log";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME,"direct");
            String message="";
            String sevirity="";
            while (!message.equals("-1")){
                System.out.println("Enter your message");
                Scanner myObj = new Scanner(System.in);
                message = myObj.nextLine();
                System.out.println("Enter the sevirty");
                sevirity = myObj.nextLine();
                channel.basicPublish(EXCHANGE_NAME, sevirity, null, message.getBytes());
            }
        } ;
    }
}
