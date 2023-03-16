package com.example.myproject.controller;

import com.example.myproject.repository.Account;
import com.example.myproject.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin("http://localhost:3000/")
public class RequestHandler {


        @Autowired
        private AccountRepository accountRepository;

        //    Erstellen eines neuen Benutzers
        @PostMapping(path="api/create-account", produces="application/json")
        public String createAccount (@RequestBody Account account) {
//            Account account = new Account();
//            account.setAdmin(false);
//            account.setCompany(this.createAccountRequest.get(token).getCompany());
//            account.setEmail(this.createAccountRequest.get(token).getEmail());
//            account.setFirstName(this.createAccountRequest.get(token).getFname());
//            account.setLastName(this.createAccountRequest.get(token).getLname());
//            account.setPassword(this.createAccountRequest.get(token).getPassword());
//            account.setPhoneNumber(this.createAccountRequest.get(token).getPhoneNumber());
            this.accountRepository.save(account);
            Integer id = account.getID();
            return account+" created";
        }

        //    Abrufen eines Benutzers nach ID.
//        @GetMapping("/{id}")
        @GetMapping(path="api/account/{id}", produces="application/json")
        public Account getAccountById(@PathVariable Integer id) {
            return accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        }
    }
