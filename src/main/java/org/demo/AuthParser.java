package org.demo;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;
import java.io.ByteArrayInputStream;

/**
 * A simple class to pull out the authentication tokens from Radian6 Authentication XML response.
 * Currently we just want to get the first <token> and <userKey> values
 */
public class AuthParser {

	private Document document;
	public AuthParser(String xml) throws Exception {
		ByteArrayInputStream xml_stream = new ByteArrayInputStream(xml.getBytes());
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(false);
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setErrorHandler(new MyErrorHandler());
		document = builder.parse(xml_stream);
	}
	
	public String getToken() {
		String result = "";
		NodeList nodes = document.getElementsByTagName("token");
		if (nodes.getLength() > 0) {
			result = nodes.item(0).getFirstChild().getNodeValue();
		}
		return result;
	}
	
	public String getUserKey() {
		String result = "";
		NodeList nodes = document.getElementsByTagName("userKey");
		if (nodes.getLength() > 0) {
			result = nodes.item(0).getFirstChild().getNodeValue();
		}
		return result;
	}
	
	private class MyErrorHandler implements ErrorHandler {
		public void error(SAXParseException ex) throws SAXException {
			throw ex;
		}
		public void fatalError(SAXParseException ex) throws SAXException {
			throw ex;
		}
		public void warning(SAXParseException ex) throws SAXException {
			throw ex;
		}
	}

}