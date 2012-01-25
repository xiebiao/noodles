package com.xiebiao.web.servlet;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SyncServlet")
public class SyncServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7146771549264238958L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html");
		Writer wr = resp.getWriter();
		wr.write("This is use servlet 3.0's annotation config the servlet");
		wr.close();
		resp.flushBuffer();
	}
}
