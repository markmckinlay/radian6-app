package org.demo;

import java.security.MessageDigest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;


public class Authenticate {
    private static final String BASE_SERVICE_URL = "http://api.radian6.com/socialcloud/v1";
	private String token = "";
	private String appkey = "";

	public void execute(String username, String password) throws Exception {
        HttpClient httpclient = new HttpClient();
        GetMethod httpget = new GetMethod(BASE_SERVICE_URL + "/auth/authenticate");
        httpget.addRequestHeader("Accept" , "*/xml");
        httpget.addRequestHeader("auth_user", username);
        httpget.addRequestHeader("auth_pass", hashPassword(password));
        httpget.addRequestHeader("Content-Type", "text/*");
        
        try {
            httpclient.executeMethod(httpget);
			String xml_resp = httpget.getResponseBodyAsString();
			AuthParser ap = new AuthParser(xml_resp);
			token = ap.getToken();
			appkey = ap.getUserKey();
		} catch (Exception ex) {
			throw ex;
        } finally {
            httpget.releaseConnection();
        }
		
	}

	private static String hashPassword(String pass) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(pass.getBytes());
		byte[] data = md.digest();
		StringBuffer sb = new StringBuffer();
		for (int i=0;i<data.length;i++)
		{
			sb.append(Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}
	
	public String getToken() {
		return token;
	}
	
	public String getAppkey() {
		return appkey;
	}
}