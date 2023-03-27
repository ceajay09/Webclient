package com.example.myproject.controller;

import com.example.myproject.repository.Account;
import com.example.myproject.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// @RequestMapping("api/")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class RequestHandler {

    private static final Logger logger = LogManager.getLogger(RequestHandler.class);

    @Autowired
    private AccountRepository accountRepository;

    // Erstellen eines neuen Benutzers
    @PostMapping(path = "/api/register", produces = "application/json")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        String firstName = request.get("firstName");
        String lastName = request.get("lastName");
        String company = request.get("company");
        String phoneNumber = request.get("phoneNumber");

        // Log the request
        logger.info("Received registration request: email={}, firstName={}, lastName={}, company={}, phoneNumber={}",
                email, firstName, lastName, company, phoneNumber);

        Account account = new Account();
        account.setEmail(email);
        account.setPassword(password);
        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setCompany(company);
        account.setPhoneNumber(phoneNumber);

        this.accountRepository.save(account);
        int accountID = this.accountRepository.findByEmail(account.getEmail()).getID();

        // Integer id = account.getID();
        System.out.println(account.getEmail() + " created / " + "ID = " + accountID);

        // Log the response
        String responseMessage = "User registered successfully";
        logger.info("Registration response: {}", responseMessage);

        return ResponseEntity.ok("User registered successfully");
    }

    // Abrufen eines Benutzers nach ID.
    // @GetMapping("/{id}")
    @GetMapping(path = "api/account/{id}", produces = "application/json")
    public Account getAccountById(@PathVariable Integer id) {
        return accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
