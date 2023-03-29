package com.example.myproject;

//import com.example.myproject.controller.AccountController;
import com.example.myproject.repository.Account;
import com.example.myproject.repository.AccountRepository;
import com.example.myproject.repository.SessionRepository;
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

        }

        @PostConstruct
        public void testdata() throws Exception {
                Account a1 = new Account();
                a1.setCompany("Google");
                a1.setEmail("mail");
                a1.setFirstName("Cesar");
                a1.setLastName("Jaqu");
                a1.setPassword("0000");
                a1.setPhoneNumber("123");
                this.accountRepository.save(a1);
                System.out.println("testaccount erstellt, ID:" + a1.getID());
        }

}
