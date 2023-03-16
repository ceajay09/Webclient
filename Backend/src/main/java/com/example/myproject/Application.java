package com.example.myproject;

//import com.example.myproject.controller.AccountController;
import com.example.myproject.repository.Account;
import com.example.myproject.repository.AccountRepository;
import com.example.myproject.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLException;


@SpringBootApplication
public class Application {

//    @Autowired
//    private static AccountController accountController;
    @Autowired
    protected AccountRepository accountRepository;
    @Autowired
    protected SessionRepository sessionRepository;

    public static void main(String[] args) throws SQLException {

        SpringApplication.run(Application.class, args);


        // Erstellen eines neuen Accounts
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Account account = new Account();
        account.setFirstName("Max");
        account.setLastName("Mustermann");
        account.setEmail("max.mustermann@example.com");
        HttpEntity<Account> request = new HttpEntity<>(account, headers);
        ResponseEntity<String> createResponse = restTemplate.postForEntity("http://localhost:8080/api/create-account", request, String.class);
        System.out.println("Erstellung des Accounts war erfolgreich. Antwort vom Server: " + createResponse.getBody());

//                 Abrufen des erstellten Accounts
        ResponseEntity<Account> getResponse = restTemplate.getForEntity("http://localhost:8080/api/account/" + 1, Account.class);
        System.out.println("Der abgerufene Account hat folgenden Namen + ID: " + getResponse.getBody().getFirstName() + " " + getResponse.getBody().getID());











//                // Erstellen eines neuen Accounts
//                RestTemplate restTemplate = new RestTemplate();
//                HttpHeaders headers = new HttpHeaders();
//                headers.setContentType(MediaType.APPLICATION_JSON);
//                Account account = new Account();
//                account.setFirstName("Max");
//                account.setLastName("Mustermann");
//                account.setEmail("max.mustermann@example.com");
//                HttpEntity<Account> request = new HttpEntity<>(account, headers);
//                ResponseEntity<String> createResponse = restTemplate.postForEntity("http://localhost:8080/api/create-account", request, String.class);
//                System.out.println("Erstellung des Accounts war erfolgreich. Antwort vom Server: " + createResponse.getBody());
//
////                 Abrufen des erstellten Accounts
//                ResponseEntity<Account> getResponse = restTemplate.getForEntity("http://localhost:8080/" + account.getID(), Account.class);
//                System.out.println("Der abgerufene Account hat folgende Daten: " + getResponse.getBody());






//        SpringApplication.run(Application.class, args);
//
//        Connection conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
//
//        Statement stmt = conn.createStatement();
//        stmt.executeUpdate("CREATE USER newuser PASSWORD 'newpassword'");
//
//        stmt.close();
//        conn.close();
//
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        // create a new account
//        String createAccountUrl = "http://localhost:8080/account/create";
//        Account newAccount = new Account();
//        newAccount.setFirstName("Cesar");
//        newAccount.setCompany("company");
//        newAccount.setEmail("mail");
//        newAccount.setLastName("jaquiery");
//        newAccount.setPhoneNumber("061");
//
//        accountController.createAccount(newAccount);
//        restTemplate.postForObject(createAccountUrl, newAccount, Account.class);
//
//        // get all accounts
//        String getAllAccountsUrl = "http://localhost:8080/account/all";
//        Account[] allAccounts = restTemplate.getForObject(getAllAccountsUrl, Account[].class);
//
//        // print all accounts
//        for (Account account : allAccounts) {
//            System.out.println(account.toString());
        }
    }
