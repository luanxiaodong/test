package com.botech.ywzx.netty.conf;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ClientConfigFactory {
	private static final Logger log = LoggerFactory.getLogger(ClientConfigFactory.class);
	private ClientConfigFactory(){
		try {
			config=new ClientConfig();
			Properties prop = new Properties();
			prop.load(ClientConfigFactory.class.getResourceAsStream("/client-config.properties"));
			
			config.setClusterid(prop.getProperty("clusterid"));
			config.setIp(prop.getProperty("ip"));
			config.setPort(Integer.valueOf( prop.getProperty("port") ));
			
			/*
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		    domFactory.setNamespaceAware(true); // never forget this!
		    DocumentBuilder builder = domFactory.newDocumentBuilder();
		    Document doc = builder.parse(ClientConfigFactory.class.getResourceAsStream("/client-config.xml"));

		    XPathFactory factory = XPathFactory.newInstance();
		    XPath xpath = factory.newXPath();
		    
		    XPathExpression clusteridExpr = xpath.compile("/config/clusterid");
		    Node clusterid = (Node) clusteridExpr.evaluate(doc, XPathConstants.NODE);
		    config.setClusterid(clusterid.getTextContent());
		    
		    XPathExpression ipExpr = xpath.compile("/config/ip");
		    Node ip = (Node) ipExpr.evaluate(doc, XPathConstants.NODE);
		    //System.out.println(ip.getTextContent());
		    config.setIp(ip.getTextContent());
		    
		    XPathExpression portExpr = xpath.compile("/config/port");
		    Node port = (Node) portExpr.evaluate(doc, XPathConstants.NODE);
		    //System.out.println(port.getTextContent());
		    try {
		    	config.setPort(Integer.valueOf(port.getTextContent()));
			} catch (Exception e) {config.setPort(7397);}
		    */
		} catch (Exception e) {
			e.printStackTrace();
			log.error("config error",e);
		}
	}
	private static ClientConfigFactory instance=new ClientConfigFactory();
	public static ClientConfigFactory getInstance(){
		return instance;
	}
	
	private ClientConfig config;//配置
	
	public ClientConfig getConfig() {//配置
		return config;
	}

}
