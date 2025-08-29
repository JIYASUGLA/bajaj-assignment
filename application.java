package com.example.demo;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        RestTemplate rt = new RestTemplate();

        // -------- Step 1: Register --------
        String registerUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
        Map<String,String> regBody = new HashMap<>();
        regBody.put("name", "Your Name");         // 👈 apna naam
        regBody.put("regNo", "REG12345");         // 👈 apna reg no
        regBody.put("email", "you@email.com");    // 👈 apna email

        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String,String>> req = new HttpEntity<>(regBody, h);

        ResponseEntity<Map> resp = rt.postForEntity(registerUrl, req, Map.class);
        System.out.println("Register Response: " + resp.getBody());

        String webhook = (String) resp.getBody().get("webhook");
        String accessToken = (String) resp.getBody().get("accessToken");

        System.out.println("Webhook URL = " + webhook);
        System.out.println("Access Token = " + accessToken);

        // -------- Step 2: Decide SQL question --------
        // 👇 last two digits nikalo odd/even check karne ke liye
        String regNoDigits = regBody.get("regNo").replaceAll("\\D", "");
        int lastTwo = Integer.parseInt(regNoDigits.substring(regNoDigits.length()-2));
        boolean odd = lastTwo % 2 == 1;

        if (odd) {
            System.out.println("👉 Open odd SQL question: https://drive.google.com/file/d/1IeSI6l6KoSQAFfRihIT9tEDICtoz−G/view");
        } else {
            System.out.println("👉 Open even SQL question: https://drive.google.com/file/d/143MR5cLFrlNEuHzzWJ5RHnEWuijuM9X/view");
        }

        // -------- Step 3: Put your final SQL query here --------
        // 👇 yaha apna query likho
        String finalQuery = "SELECT * FROM tableName;";

        // -------- Step 4: Submit solution --------
        String submitUrl = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";
        HttpHeaders h2 = new HttpHeaders();
        h2.setContentType(MediaType.APPLICATION_JSON);
        h2.set("Authorization", accessToken);   // kabhi "Bearer " + token bhi lagana padta hai

        Map<String,String> ans = new HashMap<>();
        ans.put("finalQuery", finalQuery);

        HttpEntity<Map<String,String>> req2 = new HttpEntity<>(ans, h2);
        ResponseEntity<String> resp2 = rt.postForEntity(submitUrl, req2, String.class);

        System.out.println("Submit Status: " + resp2.getStatusCode());
        System.out.println("Submit Response: " + resp2.getBody());
    }
}
