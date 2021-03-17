package com.ck.javaee.servlets;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ck.javaee.jms.MyMessageProducer;

@WebServlet(urlPatterns = "/")
public class MyServlet extends HttpServlet {

	private static final long serialVersionUID = 4433737655771301312L;

	@EJB
	MyMessageProducer producer;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String message = "Hello message from JavaEE Server using JmS!!!";
		producer.sendMessage(message);
		resp.getWriter().write("Published the message " + message);
	}
}
