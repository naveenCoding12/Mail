package com.naveen.Mail.Controller;

import com.naveen.Mail.Service.GmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class GmailController {

  private final GmailService gmailService;

  public GmailController(GmailService gmailService) {
    this.gmailService = gmailService;
  }

  @GetMapping("/getGmailData")
  public HashMap<String, String> getGmailData(@RequestParam String query) {
    return gmailService.getGmailData(query);
  }
}
