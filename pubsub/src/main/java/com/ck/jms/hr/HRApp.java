package com.ck.jms.hr;

import javax.jms.JMSContext;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class HRApp {

	public static void main(String[] args) throws NamingException {
		InitialContext context = new InitialContext();
		Topic topic = (Topic) context.lookup("topic/empTopic");

		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext();) {
			Employee employee = new Employee();
			employee.setId(123);
			employee.setFirstName("John");
			employee.setLastName("Oliver");
			employee.setDesignation("Software Architect");
			employee.setEmail("john@gmail.com");
			employee.setPhone("9876543210");
			for (int i = 0; i < 10; i++) {
				jmsContext.createProducer().send(topic, employee);
			}
			System.out.println("Message Sent");

		}
	}

}
