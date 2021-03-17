package com.ck.jms.claimmanagement;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class claimManagement {

	public static void main(String[] args) throws NamingException, JMSException {

		InitialContext initialContext = new InitialContext();
		Queue requestQueue = (Queue) initialContext.lookup("queue/claimQueue");

		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext();) {
			JMSProducer producer = jmsContext.createProducer();
			JMSConsumer consumer = jmsContext.createConsumer(requestQueue, "hospitalId=1 AND "
					+ "claimAmount BETWEEN 1000 AND 5000 AND doctorName LIKE 'J%' AND doctorType IN ('neuro','gyno')"
					+ "OR JMSPriority BETWEEN 3 AND 6");
			ObjectMessage objectMessage = jmsContext.createObjectMessage();
			objectMessage.setIntProperty("hospitalId", 1);
			objectMessage.setDoubleProperty("claimAmount", 1000);
			objectMessage.setStringProperty("doctorName","John");
			objectMessage.setStringProperty("doctorType", "gyno");
			
			Claim claim = new Claim();
			claim.setHospitalId(1);
			claim.setClaimAmount(1000);
			claim.setDoctorName("John");
			claim.setDoctorType("Gyno");
			claim.setInsuranceProvider("Blue Cross");
			objectMessage.setObject(claim);

			producer.send(requestQueue, objectMessage);
			Claim receiveBody = consumer.receiveBody(Claim.class);
			System.out.println(receiveBody.getClaimAmount());

		}
	}

}
