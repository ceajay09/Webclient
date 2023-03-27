// package com.example.myproject.controller;

// import com.example.myproject.repository.Account;
// import com.example.myproject.repository.AccountRepository;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.server.ResponseStatusException;

// @CrossOrigin(origins = "http:localhost:3000")
// @RestController
// @RequestMapping("/api")
// public class AccountController {

// @Autowired
// private AccountRepository accountRepository;

// @GetMapping("/accounts")
// public List<Account> getAccounts() {
// System.out.println(this.accountRepository.findAll());
// return this.accountRepository.findAll();
// }

// // // Erstellen eines neuen Benutzers
// // @PostMapping
// // public ResponseEntity<String> createAccount (@RequestBody Account account)
// {
// // accountRepository.save(account);
// // System.out.println("Requesthandler: account erstellt");
// // return ResponseEntity.ok("Account wurde erfolgreich erstellt.");
// // }

// // // Abrufen eines Benutzers nach ID.
// // @GetMapping("/{id}")
// // public Account getAccountById(@PathVariable Integer id) {
// // return accountRepository.findById(id)
// // .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
// // }
// }
