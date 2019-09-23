package store.configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@ComponentScan({ "store.configuration.service" })
@PropertySource({ "classpath:mail.properties" })
public class MailConfiguration {
	@Autowired
	private Environment env;

	@Bean
	public JavaMailSender getMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(env.getProperty("application.mail.host"));
		mailSender.setPort(env.getProperty("application.mail.port", Integer.class));
		mailSender.setUsername(env.getProperty("application.mail.username"));
		mailSender.setPassword(env.getProperty("application.mail.password"));
		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.starttls.enable", env.getProperty("application.mail.smtp.starttls.enable"));
		javaMailProperties.put("mail.smtp.auth", env.getProperty("application.mail.smtp.auth"));
		javaMailProperties.put("mail.transport.protocol", env.getProperty("application.mail.transport.protocol"));
		mailSender.setJavaMailProperties(javaMailProperties);
		return mailSender;
	}

	@Bean
	public ResourceBundleMessageSource messageSource() {
		final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames("messages");
		return messageSource;
	}

}
