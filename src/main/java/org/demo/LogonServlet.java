package org.demo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Map;
import java.util.HashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

public class LogonServlet extends HttpServlet
{

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/xml");
        response.setStatus(HttpServletResponse.SC_OK);
		try {
			doStuff(response.getWriter(), request);			
			request.getRequestDispatcher("start.html").forward(request, response);
		} catch (Exception ex) {
			response.getWriter().println("<exception>Exception calling Radian6: " + ex.toString() + "</exception>");
		}
    }
	
	protected void doStuff(PrintWriter writer, HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession(true);
		Object user_obj = request.getParameter("username");
		Object pass_obj = request.getParameter("password");
		if (user_obj == null || pass_obj == null) {
			throw new Exception("Invalid credentials");
		}
		doAuthenticate(session, user_obj.toString(), pass_obj.toString());
		//writer.print("<authentication><auth_token>");
		//writer.print(getAuthToken(session));		
		//writer.print("</auth_token><auth_appkey>");
		//writer.print(getAuthAppkey(session));		
		//writer.println("</auth_appkey></authentication>");
	}

	protected void doAuthenticate(HttpSession session, String username, String password) throws Exception {
		// Check if authentiction tokens are present in the session
		Authenticate auth = new Authenticate();
		auth.execute(username, password);
		session.setAttribute("auth_token", auth.getToken());
		session.setAttribute("auth_appkey", auth.getAppkey());
	}
	
	protected String getAuthToken(HttpSession session) {
		String result = "";
		Object o = session.getAttribute("auth_token");
		if (o != null) {
			result = o.toString();
		}
		return result;
	}
	
	protected String getAuthAppkey(HttpSession session) {
		String result = "";
		Object o = session.getAttribute("auth_appkey");
		if (o != null) {
			result = o.toString();
		}
		return result;
	}
	
	



}