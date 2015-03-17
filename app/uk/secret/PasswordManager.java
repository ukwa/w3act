package uk.secret;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class PasswordManager {
	
	private String secretServerEndpoint;
	private String username;
	private String password;
	private String token;
	
	public PasswordManager(String secretServerEndpoint, String username, String password) throws Exception {
		this.secretServerEndpoint = secretServerEndpoint;
		this.username = username;
		this.password = password;
		token = authenticate();
	}
	private String authenticate() throws Exception {
		return getNodeTextOfURI(secretServerEndpoint + "/Authenticate?username=" + username + "&password=" + password,
				"/AuthenticateResult/Token");
	}
	private String getNodeTextOfURI(String uri, String xPath) throws Exception {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(uri);
		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xpath.compile(xPath);
		
		Object result = expr.evaluate(doc, XPathConstants.NODESET);
		NodeList nodes = (NodeList) result;
		return nodes.item(0).getTextContent();
	}
	public String addSecret(String secretTypeId, String secretName, List<String> secretFieldIds,
			List<String> secretItemValues, String folderId) throws Exception {
		return getNodeTextOfURI(secretServerEndpoint + "/AddSecret?token=" + token +
				"&secretTypeId=..." + secretTypeId +
				"&secretName=..." + secretName +
				"&secretFieldIds=..." + secretFieldIds.get(0) +
				"&secretItemValues=..." + secretItemValues.get(0) +
				"&folderId=..." + folderId,
				"/AddSecretResult/Secret/Id");
	}
	public String getSecret() {
		return null;
		
	}
}
