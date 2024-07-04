package com.naveen.Mail.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import io.restassured.path.json.JsonPath;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;

@Service
public class GmailService {
  private static final String APPLICATION_NAME = "Syngenity";
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
  private static final String USER_ID = "me";
  private static final List<String> SCOPES = Collections.singletonList(GmailScopes.MAIL_GOOGLE_COM);
  private static final String CREDENTIALS_FILE_PATH = "C:\\Users\\cenimeni\\Documents\\Mail_poc\\credentials.json";
  private static final String TOKENS_DIRECTORY_PATH = "C:\\Users\\cenimeni\\Documents\\Mail_poc";

  private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
    InputStream in = new FileInputStream(new File(CREDENTIALS_FILE_PATH));
    if (in == null) {
      throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
    }
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
      HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
      .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
      .setAccessType("offline")
      .build();
    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
    return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
  }

  private static Gmail getService() throws IOException, GeneralSecurityException {
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
      .setApplicationName(APPLICATION_NAME)
      .build();
  }

  public HashMap<String, String> getGmailData(String query) {
    try {
      Gmail service = getService();
      ListMessagesResponse response = service.users().messages().list(USER_ID).setQ(query).execute();
      List<Message> messages = response.getMessages();
      if (messages == null || messages.isEmpty()) {
        return null;
      }
      Message message = service.users().messages().get(USER_ID, messages.get(0).getId()).execute();
      JsonPath jp = new JsonPath(message.toString());
      String subject = jp.getString("payload.headers.find { it.name == 'Subject' }.value");
      String body = new String(Base64.getDecoder().decode(jp.getString("payload.parts[0].body.data")));
      String link = null;
      String[] arr = body.split("\n");
      for (String s : arr) {
        s = s.trim();
        if (s.startsWith("http") || s.startsWith("https")) {
          link = s.trim();
        }
      }
      HashMap<String, String> hm = new HashMap<>();
      hm.put("subject", subject);
      hm.put("body", body);
      hm.put("link", link);
      return hm;
    } catch (Exception e) {
      System.out.println("Email not found....");
      throw new RuntimeException(e);
    }
  }
}
