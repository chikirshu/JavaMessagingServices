package com.ck.jms.messagestructure;

import javax.jms.BytesMessage;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageTypesDemo {

	public static void main(String[] args) throws NamingException, InterruptedException, JMSException {

		InitialContext context = new InitialContext();
		Queue queue = (Queue) context.lookup("queue/myQueue");

		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			JMSProducer producer = jmsContext.createProducer();
			BytesMessage byteMessage = jmsContext.createBytesMessage();
			byteMessage.writeUTF("John");
			byteMessage.writeLong(123l);

			StreamMessage streamMessage = jmsContext.createStreamMessage();
			streamMessage.writeBoolean(true);
			streamMessage.writeFloat(2.5f);

			MapMessage mapMessage = jmsContext.createMapMessage();
			mapMessage.setBoolean("isCreditAvailable", true);

			ObjectMessage objectMessage = jmsContext.createObjectMessage();
			Patient patient = new Patient();
			patient.setId(123);
			patient.setName("John");
			objectMessage.setObject(patient);

			//producer.send(queue, objectMessage);
			producer.send(queue, patient);

			// BytesMessage messageReceived = (BytesMessage)
			// jmsContext.createConsumer(queue).receive(5000);// wait for 5 seconds to get
			// the
			// message from the queue,
			// otherwise timeout
			// System.out.println(messageReceived.readUTF());
			// System.out.println(messageReceived.readLong());

			// StreamMessage messgeReceived = (StreamMessage)
			// jmsContext.createConsumer(queue).receive(5000);
			// System.out.println(messgeReceived.readBoolean());
			// System.out.println(messgeReceived.readFloat());

			// MapMessage messgeReceived = (MapMessage)
			// jmsContext.createConsumer(queue).receive(5000);
			// System.out.println(messgeReceived.getBoolean("isCreditAvailable"));

			//ObjectMessage messageReceived = (ObjectMessage) jmsContext.createConsumer(queue).receive(5000);
			Patient patientReceived = jmsContext.createConsumer(queue).receiveBody(Patient.class);
			//Patient patientReceived = (Patient) messageReceived.getObject();
			System.out.println(patientReceived.getId() + "\n" + patientReceived.getName());
		}
	}

}
