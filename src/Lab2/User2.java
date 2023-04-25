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
    private final static String EXCHANGE_NAME3 = "p2";
    private final static String EXCHANGE_NAME4 = "priority2";
    private final static String EXCHANGE_NAME5 = "p3";
    private final static String EXCHANGE_NAME6 = "priority3";

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        channel.exchangeDeclare(EXCHANGE_NAME2, "fanout");
        channel.exchangeDeclare(EXCHANGE_NAME3, "fanout");
        channel.exchangeDeclare(EXCHANGE_NAME4, "fanout");
        channel.exchangeDeclare(EXCHANGE_NAME5, "fanout");
        channel.exchangeDeclare(EXCHANGE_NAME6, "fanout");
        String queue_name = channel.queueDeclare().getQueue();
        channel.queueBind(queue_name, EXCHANGE_NAME, "");

        String queue_name2 = channel.queueDeclare().getQueue();
        channel.queueBind(queue_name2, EXCHANGE_NAME2, "");
        String queue_name3 = channel.queueDeclare().getQueue();
        channel.queueBind(queue_name3, EXCHANGE_NAME3, "");

        String queue_name4 = channel.queueDeclare().getQueue();
        channel.queueBind(queue_name4, EXCHANGE_NAME4, "");
        String queue_name5 = channel.queueDeclare().getQueue();
        channel.queueBind(queue_name5, EXCHANGE_NAME5, "");

        String queue_name6 = channel.queueDeclare().getQueue();
        channel.queueBind(queue_name6, EXCHANGE_NAME6, "");
//
        JLabel l1, l2,l3,l4,l5,l6;
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



        l3 = new JLabel("Paragraph 2");
        l3.setBounds(280, 250, 200, 30);
        JTextArea text2 = new JTextArea(msg);
        text2.setBounds(100, 290, 500, 150);
        JButton btn2 = new JButton("modifier");
        btn2.setBounds(650, 350, 200, 30);
        l4 = new JLabel();
        l4.setBounds(280,440,200,30);

        l5 = new JLabel("Paragraph 3");
        l5.setBounds(280, 470, 200, 30);
        JTextArea text3 = new JTextArea(msg);
        text3.setBounds(100, 510, 500, 150);
        JButton btn3 = new JButton("modifier");
        btn3.setBounds(650, 590, 200, 30);
        l6 = new JLabel();
        l6.setBounds(280,660,200,30);



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

        btn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = text2.getText();
                try {
                    channel.basicPublish(EXCHANGE_NAME3, "", null, message.getBytes());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println("[user2] sent '" + message + "' ");
                String txt="done";
                try {
                    channel.basicPublish(EXCHANGE_NAME4, "", null, txt.getBytes());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        btn3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = text3.getText();
                try {
                    channel.basicPublish(EXCHANGE_NAME5, "", null, message.getBytes());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println("[user2] sent '" + message + "' ");
                String txt="done";
                try {
                    channel.basicPublish(EXCHANGE_NAME6, "", null, txt.getBytes());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });





        text.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(text.isEditable()){
                    String txt = "user2";

                try {
                    channel.basicPublish(EXCHANGE_NAME2, "", null, txt.getBytes());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }}
            }

        });

        text2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
            if(text2.isEditable()){
                String txt = "user2";
                try {
                    channel.basicPublish(EXCHANGE_NAME4, "", null, txt.getBytes());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }}
        });
        text3.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(text3.isEditable()){
                String txt = "user2";
                try {
                    channel.basicPublish(EXCHANGE_NAME6, "", null, txt.getBytes());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }}
            }
        });

        f.add(l1);
        f.add(l2);
        f.add(text);
        f.add(btn);
        f.add(l3);
        f.add(l4);
        f.add(text2);
        f.add(btn2);
        f.add(l5);
        f.add(l6);
        f.add(text3);
        f.add(btn3);
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
                text.setEditable(false);
                l2.setText(message + " is typing");
            }else if(message.equals("done")){
                btn.setEnabled(true);
                text.setEditable(true);
                l2.setText("");
            }

        };
        channel.basicConsume(queue_name2, true, "", deliverCallback2, consumerTag -> {
        });


        DeliverCallback deliverCallback3 = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            text2.setText(message);
        };
        channel.basicConsume(queue_name3, true, "", deliverCallback3, consumerTag -> {
        });

        DeliverCallback deliverCallback4 = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(message);
            if(message.equals("user1") || message.equals("user3") ){
                btn2.setEnabled(false);
                text2.setEditable(false);
                l4.setText(message + " is typing");
            }else if(message.equals("done")){
                btn2.setEnabled(true);
                text2.setEditable(true);
                l4.setText("");
            }

        };
        channel.basicConsume(queue_name4, true, "", deliverCallback4, consumerTag -> {
        });

        DeliverCallback deliverCallback5 = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            text3.setText(message);
        };
        channel.basicConsume(queue_name5, true, "", deliverCallback5, consumerTag -> {
        });

        DeliverCallback deliverCallback6 = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(message);
            if(message.equals("user1") || message.equals("user3") ){
                btn3.setEnabled(false);
                text3.setEditable(false);
                l6.setText(message + " is typing");
            }else if(message.equals("done")){
                btn3.setEnabled(true);
                text3.setEditable(true);
                l6.setText("");
            }

        };
        channel.basicConsume(queue_name6, true, "", deliverCallback6, consumerTag -> {
        });
    }

    ;

}

