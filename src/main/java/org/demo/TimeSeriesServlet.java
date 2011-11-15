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

public class TimeSeriesServlet extends HttpServlet
{
    private String greeting="Radian6 Authenticate";
    private static final String BASE_SERVICE_URL = "http://api.radian6.com/socialcloud/v1";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/xml");
        response.setStatus(HttpServletResponse.SC_OK);
		try {
			doStuff(response.getWriter(), request);			
		} catch (Exception ex) {
			response.getWriter().println("Exception calling Radian6: " + ex.toString());
		}
    }

	protected Map<String, String> getParams(HttpServletRequest request) {
		// Get request parameters and add sensible defaults if missing
		Map<String, String> result = new HashMap<String, String>();
		Object value = request.getParameter("startdate");
		if (value != null) {
			result.put("startdate", value.toString());
		}
		value = request.getParameter("enddate");
		if (value != null) {
			result.put("enddate", value.toString());
		}
		value = request.getParameter("mediatypes");
		if (value != null) {
			result.put("mediatypes", value.toString());
		} else {
			result.put("mediatypes", "8,12");			
		}
		value = request.getParameter("topics");
		if (value != null) {
			result.put("topics", value.toString());
		}
		value = request.getParameter("segmentation");
		if (value != null) {
			result.put("segmentation", value.toString());
		} else {
			result.put("segmentation", "2");
		}
		return result;
	}
	
	protected void doStuff(PrintWriter writer, HttpServletRequest request) throws Exception {
		doAuthenticate(request);
		Map<String, String> params = getParams(request);
        HttpClient httpclient = new HttpClient();
		StringBuffer url = new StringBuffer();
		url.append(BASE_SERVICE_URL);
		url.append("/data/timeseriesdata/");
		url.append(params.get("startdate"));
		url.append("/");
		url.append(params.get("enddate"));
		url.append("/");
		url.append(params.get("topics"));
		url.append("/");
		url.append(params.get("mediatypes"));
		url.append("/");
		url.append(params.get("segmentation"));
        GetMethod httpget = new GetMethod(url.toString());
        httpget.addRequestHeader("Accept" , "*/xml");
        httpget.addRequestHeader("auth_token", getAuthToken(request));
        httpget.addRequestHeader("auth_appkey", getAuthAppkey(request));
        httpget.addRequestHeader("Content-Type", "text/*");
        
        // If Basic Authentication required could use: 
        /*
        String authorizationHeader = "Basic " 
           + org.apache.cxf.common.util.Base64Utility.encode("username:password".getBytes());
        httpget.addRequestHeader("Authorization", authorizationHeader);
        */
        try {
            httpclient.executeMethod(httpget);
            //writer.println("Response Code: " + httpget.getStatusCode());
            //writer.println("Response: " + httpget.getResponseBodyAsString());
            writer.println(httpget.getResponseBodyAsString());
			Header[] headers = httpget.getResponseHeaders();
			for (int i=0;i<headers.length ;i++ )
			{
				//writer.println("Header: " + headers[i].toString());
			}
        } finally {
            httpget.releaseConnection();
        }
		
	}

	protected void doAuthenticate(HttpServletRequest request) throws Exception {
		// Check if authentiction tokens are present in the session
		HttpSession session = request.getSession(true);
		Object token = session.getAttribute("auth_token");
		Object appkey = session.getAttribute("auth_appkey");
		/**
		if (token != null && appkey != null) {
			return;
		}
		
		// If no authentication tokens, then issue an authentication request
		Authenticate auth = new Authenticate();
		auth.execute();
		session = request.getSession(true);
		session.setAttribute("auth_token", auth.getToken());
		session.setAttribute("auth_appkey", auth.getAppkey());
		*/
	}
	
	protected String getAuthToken(HttpServletRequest request) {
		String result = "";
		HttpSession session = request.getSession(true);
		Object o = session.getAttribute("auth_token");
		if (o != null) {
			result = o.toString();
		}
		return result;
	}
	
	protected String getAuthAppkey(HttpServletRequest request) {
		String result = "";
		HttpSession session = request.getSession(true);
		Object o = session.getAttribute("auth_appkey");
		if (o != null) {
			result = o.toString();
		}
		return result;
	}
	
	



}