package lab1;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import javax.swing.*;

public class Task3 extends JFrame {

    private final static String QUEUE_NAME1 = "task1";
    private final static String QUEUE_NAME2 = "task2";
    private JTextArea textArea;
    private JLabel label;

    public Task3() {
        setTitle("task 3");
        setSize(300, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        label = new JLabel("Enter some text in the console.");
        add(label);

        // Create a label with some initial text
        textArea = new JTextArea();
        add(new JScrollPane(textArea));


        setVisible(true);
    }

    public void appendText(String text) {
        textArea.append(text + "\n");
    }

    public static void main(String[] args) {
        Task3 window = new Task3();

        try {
            window.appendText("Tasks : ");

            ConnectionFactory facotry = new ConnectionFactory();
            facotry.setHost("localhost");
            Connection connection = facotry.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME1, false, false, false, null);
            channel.queueDeclare(QUEUE_NAME2, false, false, false, null);
            DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                window.appendText("Task 2 : " + message + " .");
            };
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                window.appendText("Task 1 : " + message + " .");
            };
            channel.basicConsume(QUEUE_NAME1, true, "", deliverCallback, consumerTag -> {
            });


            channel.basicConsume(QUEUE_NAME2, true, "", deliverCallback2, consumerTag2 -> {
            });

        } catch (Exception e) {
            System.out.println("Error reading from console: " + e.getMessage());
        }
    }
}
