//package com.example.myproject.repository;
//
//import org.eclipse.jetty.server.*;
//import org.eclipse.jetty.util.ssl.SslContextFactory;
//import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
//import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
//import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//@Configuration
//public class SecurityConfig {
//
//	@Bean
//	public JettyServletWebServerFactory servletContainer() {
//		JettyServletWebServerFactory jetty = new JettyServletWebServerFactory();
//		jetty.addServerCustomizers((Server server) -> {
//			HttpConfiguration http = new HttpConfiguration();
//			http.setSecurePort(8443);
//			http.setSecureScheme("https");
//			http.addCustomizer(new SecureRequestCustomizer());
//
//			SslContextFactory sslContextFactory = new SslContextFactory.Server();
//			sslContextFactory.setKeyStorePath("keystore.jks");
//			sslContextFactory.setKeyStorePassword("changeit");
//			sslContextFactory.setKeyManagerPassword("changeit");
//
//			ServerConnector connector = new ServerConnector(server,
//					new SslConnectionFactory(sslContextFactory, "http/1.1"),
//					new HttpConnectionFactory(http));
//			connector.setPort(8080);
//
//			server.setConnectors(new Connector[] { connector });
//		});
//		return jetty;
//	}
//
//
//
//
//	private SslContextFactory sslContextFactory() {
//		SslContextFactory sslContextFactory = new SslContextFactory();
//		sslContextFactory.setKeyStorePath("classpath:keystore.jks");
//		sslContextFactory.setKeyStorePassword("password");
//		sslContextFactory.setKeyManagerPassword("password");
//		return sslContextFactory;
//	}
//
//
//}
