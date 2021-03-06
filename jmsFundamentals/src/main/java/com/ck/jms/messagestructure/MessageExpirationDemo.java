package com.ck.jms.messagestructure;

import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageExpirationDemo {

	public static void main(String[] args) throws NamingException, InterruptedException {

		InitialContext context = new InitialContext();
		Queue queue = (Queue) context.lookup("queue/myQueue");
		Queue expiryQueue = (Queue) context.lookup("queue/expiryQueue");
		
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()){
			JMSProducer producer = jmsContext.createProducer();
			producer.setTimeToLive(2000);
			producer.send(queue, "Arise, awake and not stop until the goal is reached.");
			Thread.sleep(5000); //so that the message gets expired
			
			Message messageReceived = jmsContext.createConsumer(queue).receive(5000);// wait for 5 seconds to get the message from the queue, otherwise timeout
			System.out.println(messageReceived);
			
			System.out.println(jmsContext.createConsumer(expiryQueue).receiveBody(String.class));
		}
	}

}
