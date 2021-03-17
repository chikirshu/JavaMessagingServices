package com.ck.springjms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ck.springjms.senders.MessageSender;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringjmsApplicationTests {
	
	@Autowired
	MessageSender sender;

	@Test
	public void testSendAndReceive() {
		sender.send("Hello Spring JMS!!!");
	}

}
