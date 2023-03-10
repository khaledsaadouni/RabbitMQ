package lab1;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Task1 extends JFrame {
    private final static String QUEUE_NAME="task1";

    private JTextArea textArea;

    public Task1()  {
        setTitle("Text Area Example");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a text area and add it to the window
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        // Add a button to print the text area content to the console
        JButton button = new JButton("Send Task1");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost("localhost");
                try(
                        Connection connection = factory.newConnection();
                        Channel channel= connection.createChannel();
                        ) {
                    channel.queueDeclare(QUEUE_NAME,false,false,false,null);
                    String message = textArea.getText();
                    channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (TimeoutException ex) {
                    throw new RuntimeException(ex);
                } ;
                System.out.println("[Task1] sent : " +textArea.getText());
            }
        });
        add(button, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        new Task1();
    }
}
