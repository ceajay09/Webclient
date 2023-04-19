package com.example.myproject;

import com.example.myproject.model.MongoDBConnection;
//import com.example.myproject.controller.AccountController;
import com.example.myproject.repository.Account;
import com.example.myproject.repository.Token;
import com.example.myproject.repository.AccountRepository;
import com.example.myproject.repository.SessionRepository;
import com.example.myproject.repository.Token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.sql.SQLException;
import javax.annotation.PostConstruct;

@SpringBootApplication
public class Application {

        // @Autowired
        // private static AccountController accountController;
        @Autowired
        protected AccountRepository accountRepository;
        @Autowired
        protected SessionRepository sessionRepository;

        public static void main(String[] args) throws SQLException {

                SpringApplication.run(Application.class, args);
                // MongoDBConnection.getMongoClient();

        }

        @PostConstruct
        public void testdata() throws Exception {
                Account account = new Account();
                account.setCompany("Google");
                account.setEmail("admin@admin.com");
                account.setFirstName("Cesar");
                account.setLastName("Jaqu");
                account.setPassword("admin@admin.com");
                account.setPhoneNumber("123");
                this.accountRepository.save(account);
                System.out.println("\nTestaccount erstellt - ID: " + account.getID() + "\n");

        }

}
