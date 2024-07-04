//package com.naveen.Mail_javax.Service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//import javax.mail.*;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMultipart;
//import javax.mail.search.SubjectTerm;
//import java.io.IOException;
//import java.util.Properties;
//
//@Service
//public class EmailService {
//
//    @Autowired
//    private JavaMailSender javaMailSender;
//
//    public String fetchEmailBody(String subject) throws MessagingException, IOException {
//        Properties properties = new Properties();
//        properties.put("mail.imap.ssl.enable", "true");
//        properties.put("mail.imap.auth.mechanisms", "PLAIN");
//
//        Session session = Session.getInstance(properties);
//        Store store = session.getStore("imaps");
//        store.connect("imap.gmail.com", "demosygenity@gmail.com", "Manju@1439"); // replace with your Gmail credentials
//
//        Folder inbox = store.getFolder("INBOX");
//        inbox.open(Folder.READ_ONLY);
//
//        Message[] messages = inbox.search(new SubjectTerm(subject));
//        if (messages.length == 0) {
//            throw new MessagingException("No message found with subject: " + subject);
//        }
//
//        // Assuming we fetch the first message body
//        String body = getTextFromMessage(messages[0]);
//
//        inbox.close(true);
//        store.close();
//
//        return body;
//    }
//
//    private String getTextFromMessage(Message message) throws MessagingException, IOException {
//        String result = "";
//        if (message.isMimeType("text/plain")) {
//            result = message.getContent().toString();
//        } else if (message.isMimeType("multipart/*")) {
//            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
//            result = getTextFromMimeMultipart(mimeMultipart);
//        }
//        return result;
//    }
//
//    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
//        StringBuilder result = new StringBuilder();
//        int count = mimeMultipart.getCount();
//        for (int i = 0; i < count; i++) {
//            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
//            if (bodyPart.isMimeType("text/plain")) {
//                result.append("\n").append(bodyPart.getContent());
//                break; // without break, it will loop through all the body parts of message
//            } else if (bodyPart.isMimeType("text/html")) {
//                String html = (String) bodyPart.getContent();
//                result.append("\n").append(org.jsoup.Jsoup.parse(html).text());
//            } else if (bodyPart.getContent() instanceof MimeMultipart){
//                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
//            }
//        }
//        return result.toString();
//    }
//}
package com.naveen.Mail.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SubjectTerm;
import java.io.IOException;
import java.util.Properties;

@Service
public class EmailService {

  @Autowired
  private JavaMailSender javaMailSender;

  public String fetchEmailBody(String subject) throws MessagingException, IOException {
    Properties properties = new Properties();
    properties.put("mail.imap.ssl.enable", "true");
    properties.put("mail.imap.auth.mechanisms", "PLAIN");

    Session session = Session.getInstance(properties);
    Store store = session.getStore("imaps");
    store.connect("outlook.office365.com", "example@gmail.com", "password");

    Folder inbox = store.getFolder("INBOX");
    inbox.open(Folder.READ_ONLY);

    Message[] messages = inbox.search(new SubjectTerm(subject));
    if (messages.length == 0) {
      throw new MessagingException("No message found with subject: " + subject);
    }

    // Assuming we fetch the first message body
    String body = getTextFromMessage(messages[0]);

    inbox.close(true);
    store.close();

    return body;
  }

  private String getTextFromMessage(Message message) throws MessagingException, IOException {
    String result = "";
    if (message.isMimeType("text/plain")) {
      result = message.getContent().toString();
    } else if (message.isMimeType("multipart/*")) {
      MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
      result = getTextFromMimeMultipart(mimeMultipart);
    }
    return result;
  }

  private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
    StringBuilder result = new StringBuilder();
    int count = mimeMultipart.getCount();
    for (int i = 0; i < count; i++) {
      BodyPart bodyPart = mimeMultipart.getBodyPart(i);
      if (bodyPart.isMimeType("text/plain")) {
        result.append("\n").append(bodyPart.getContent());
        break; // without break, it will loop through all the body parts of message
      } else if (bodyPart.isMimeType("text/html")) {
        String html = (String) bodyPart.getContent();
        result.append("\n").append(org.jsoup.Jsoup.parse(html).text());
      } else if (bodyPart.getContent() instanceof MimeMultipart){
        result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
      }
    }
    return result.toString();
  }
}

