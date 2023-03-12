package com.example.myproject;

import com.example.myproject.repository.AccountRepository;
import com.example.myproject.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;

@SpringBootApplication
public class Application {

    @Autowired
    protected AccountRepository accountRepository;
    @Autowired
    protected SessionRepository sessionRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void testData() throws NumberFormatException, IOException{
        this.sessionRepository.deleteAll();



    }



}
