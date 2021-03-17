package com.ck.jms.messagestructure;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class RequestReplyDemo {

	public static void main(String[] args) throws NamingException, JMSException {

		InitialContext context = new InitialContext();
		Queue queue = (Queue) context.lookup("queue/requestQueue");
		//Queue replyQueue = (Queue) context.lookup("queue/replyQueue");

		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {

			JMSProducer producer = jmsContext.createProducer();
			TextMessage message = jmsContext.createTextMessage("Arise, Awake and not stop until the goal is reached.");
			TemporaryQueue replyQueue = jmsContext.createTemporaryQueue();
			message.setJMSReplyTo(replyQueue);
			//producer.send(queue, "Arise, Awake and not stop until the goal is reached.");
			producer.send(queue, message);
			System.out.println(message.getJMSMessageID());
			
			Map<String, TextMessage> requestMessages = new HashMap<>();
			requestMessages.put(message.getJMSMessageID(), message);

			JMSConsumer consumer = jmsContext.createConsumer(queue);
			//String messageReceived = consumer.receiveBody(String.class);
			//System.out.println(messageReceived);
			TextMessage messageReceived = (TextMessage) consumer.receive();
			System.out.println(messageReceived.getText());
			

			JMSProducer replyProducer = jmsContext.createProducer();
			TextMessage replyMessage = jmsContext.createTextMessage("You are awesome!!!");
			replyMessage.setJMSCorrelationID(messageReceived.getJMSMessageID());
			
			//replyProducer.send(replyQueue, "You are awesome!!!");
			replyProducer.send(messageReceived.getJMSReplyTo(), replyMessage);

			JMSConsumer replyConsumer = jmsContext.createConsumer(replyQueue);
			//System.out.println(replyConsumer.receiveBody(String.class));
			TextMessage replyReceived = (TextMessage) replyConsumer.receive();
			System.out.println(replyReceived.getJMSCorrelationID());
			System.out.println(requestMessages.get(replyReceived.getJMSCorrelationID()).getText());
		}
	}

}
