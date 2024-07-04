package com.naveen.Mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

  @Bean
  public JavaMailSenderImpl javaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost("outlook.office365.com");
    mailSender.setPort(993);
    mailSender.setUsername(""); // replace with your Outlook email address
    mailSender.setPassword(""); // replace with your Outlook password

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.store.protocol", "imaps");
    props.put("mail.imaps.ssl.trust", "*");

    return mailSender;
  }
}

//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//
//import java.util.Properties;
//
//@Configuration
//public class MailConfig {
//
//    @Bean
//    public JavaMailSenderImpl javaMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost("imap.gmail.com");
//        mailSender.setPort(993);
//        mailSender.setUsername("demosygenity@gmail.com"); // replace with your Gmail address
//        mailSender.setPassword("Manju@1439"); // replace with your Gmail password
//
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.store.protocol", "imaps");
//        props.put("mail.imaps.ssl.trust", "*");
//
//        return mailSender;
//    }
//}
