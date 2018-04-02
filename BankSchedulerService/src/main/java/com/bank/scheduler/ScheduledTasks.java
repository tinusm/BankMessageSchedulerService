package com.bank.scheduler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() throws KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException {
        log.info("The time is now {}", dateFormat.format(new Date()));
        ConnectionFactory factory = new ConnectionFactory();
		//factory.setUri("amqp://tcltuzup:lvZGCmC2XfugGMaRKnUe_s5N-wa7FM64@white-swan.rmq.cloudamqp.com/tcltuzup");
		factory.setUri("amqp://ragenmvd:hMOodtGGRlPGlikVC9oB9GLaL-ZLqIdK@white-swan.rmq.cloudamqp.com/ragenmvd");
		/*factory.setPassword("lvZGCmC2XfugGMaRKnUe_s5N-wa7FM64");
		factory.setUsername("tcltuzup:tcltuzup");
		factory.setPort(8883);
		factory.setVirtualHost("tcltuzup");*/
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		Consumer consumer = new DefaultConsumer(channel) {
			  @Override
			  public void handleDelivery(String consumerTag, Envelope envelope,
			                             AMQP.BasicProperties properties, byte[] body)
			      throws IOException {
			    String message = new String(body, "UTF-8");
			    System.out.println(" [x] Received '" + message + "'");
			  }
			};
			channel.basicConsume("BANK.FUND.TRANSFER", true, consumer);
    }
}
