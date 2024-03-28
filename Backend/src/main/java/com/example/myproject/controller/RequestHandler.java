package com.example.myproject.controller;

import com.example.myproject.controller.RabbitMQ.ReceiveFromQueue;
import com.example.myproject.model.Account;
import com.example.myproject.repository.*;

import com.example.myproject.service.AccountService;
import com.example.myproject.util.TokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
//import javax.annotation.PostConstruct;
//import javax.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

// @RequestMapping("api/")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@PropertySource("classpath:application.properties")
public class RequestHandler {

    private static final Logger logger = LogManager.getLogger(RequestHandler.class);
    private final AccountRepository accountRepository;
    private final ReceiveFromQueue receiveFromQueue;
    private final MongoDbService mongoDbService;
    private final AccountService accountService;
    private final TokenUtil tokenUtil;

    public RequestHandler(AccountRepository accountRepository,
                          ReceiveFromQueue receiveFromQueue,
                          MongoDbService mongoDbService,
                          AccountService accountService,
                          TokenUtil tokenUtil) {
        this.accountRepository = accountRepository;
        this.receiveFromQueue = receiveFromQueue;
        this.mongoDbService = mongoDbService;
        this.accountService = accountService;
        this.tokenUtil = tokenUtil;
    }

    // Erstellen eines neuen Benutzers
    @PostMapping(path = "/api/register", produces = "application/json")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> request) {
        return accountService.registerUser(request);
    }

    @PostMapping(path = "/api/login", produces = "application/json")
    public ResponseEntity<String> loginUser(@RequestBody Map<String, String> request) {
        return accountService.loginUser(request);
    }

    @PostMapping(path = "/api/logout", produces = "application/json")
    public ResponseEntity<String> logoutUser(@RequestHeader("Authorization") String token) {
        return accountService.logoutUser(token);
    }


    @GetMapping(path = "api/account/{id}", produces = "application/json")
    public Account getAccountById(@PathVariable String id) {
        return accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/api/dashboard", method = RequestMethod.GET)
    public ResponseEntity<?> getAccountInfoFromToken(HttpServletRequest request) {
        return accountService.getAccountInfoFromToken(request);
    }
}
