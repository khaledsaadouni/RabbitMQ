package Lab2;

import com.rabbitmq.client.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class User2 {
    private final static String EXCHANGE_NAME = "p1";
    private final static String EXCHANGE_NAME2 = "priority";

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        channel.exchangeDeclare(EXCHANGE_NAME2, "fanout");
        String queue_name = channel.queueDeclare().getQueue();
        channel.queueBind(queue_name, EXCHANGE_NAME, "");

        String queue_name2 = channel.queueDeclare().getQueue();
        channel.queueBind(queue_name2, EXCHANGE_NAME2, "");
//
        JLabel l1, l2;
        String msg = "", msg2 = "";
        JFrame f = new JFrame();

        l2 = new JLabel();
        l2.setBounds(280,220,200,30);

        f.setTitle("User2");
        l1 = new JLabel("Paragraph 1");
        l1.setBounds(280, 30, 200, 30);
        JTextArea text = new JTextArea(msg);
        text.setBounds(100, 70, 500, 150);

        JButton btn = new JButton("modifier");
        btn.setBounds(650, 110, 200, 30);
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = text.getText();
                try {
                    channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println("[user2] sent '" + message + "' ");
                String txt="done";
                try {
                    channel.basicPublish(EXCHANGE_NAME2, "", null, txt.getBytes());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        text.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String txt = "user2";
                try {
                    channel.basicPublish(EXCHANGE_NAME2, "", null, txt.getBytes());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        f.add(l1);
        f.add(l2);
        f.add(text);
        f.add(btn);
        f.setSize(850, 300);
        f.setLayout(null);
        f.setVisible(true);
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            text.setText(message);
        };
        channel.basicConsume(queue_name, true, "", deliverCallback, consumerTag -> {
        });

        DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(message);
            if(message.equals("user1") || message.equals("user3") ){
                btn.setEnabled(false);
                l2.setText(message + " is typing");
            }else if(message.equals("done")){
                btn.setEnabled(true);
                l2.setText("");
            }

        };
        channel.basicConsume(queue_name2, true, "", deliverCallback2, consumerTag -> {
        });
    }

    ;

}

