package fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;

public class Producer {
    private final static  String EXCHANGE_NAME = "logs";
    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try(Connection connection=factory.newConnection();
            Channel channel = connection.createChannel())
        {
            channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
            String message="";
            while(!message.equals("-1"))
        {   System.out.println("Enter your message");
            Scanner myObj = new Scanner(System.in);  // Create a Scanner object
            message = myObj.nextLine();
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
            System.out.println("[localhost] sent '" + message + "' ");
        }
        }
    }
}
