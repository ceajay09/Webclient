package com.example.myproject.controller;

import com.example.myproject.repository.Account;
import com.example.myproject.repository.Token;
import com.example.myproject.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.example.myproject.model.MongoDBConnection;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

// @RequestMapping("api/")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@PropertySource("classpath:application.properties")
public class RequestHandler {

    private final String ACCOUNTS_COLLECTION = "accounts";

    private static final Logger logger = LogManager.getLogger(RequestHandler.class);

    // private final Key key =
    // Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

    @Autowired
    private AccountRepository accountRepository;

    @Value("${mongo.connection.string}")
    private String mongoConnection;

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

        if (this.accountRepository.findByEmail(email) == null) {
            // Account noch nicht vorhanden..
            Account account = new Account();
            account.setEmail(email);
            account.setPassword(password);
            account.setFirstName(firstName);
            account.setLastName(lastName);
            account.setCompany(company);
            account.setPhoneNumber(phoneNumber);

            this.accountRepository.save(account);

            int accountID = this.accountRepository.findByEmail(account.getEmail()).getID();
            this.saveAccountToDatabase(accountID);

            // Integer id = account.getID();
            System.out.println(account.getEmail() + " created / " + "ID = " + accountID);

            // Log the response
            String responseMessage = account.getEmail() + " registered successfully";
            logger.info("Registration response: {}", responseMessage);

            return ResponseEntity.ok().body("{\"status\":\"success\",\"message\":\"Account registered successfully\"}");

        } else {
            // Log the response
            String responseMessage = email + " registered already";
            logger.info("Registration response: {}", responseMessage);

            // Hier wird der HTTP-Statuscode "409" und die Nachricht "User already exists"
            // zurückgegeben, dass der Benutzer bereits registriert ist und ein Konflikt
            // aufgetreten ist.
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Account already exists");

        }
    }

    @PostMapping(path = "/api/login", produces = "application/json")
    public ResponseEntity<String> loginUser(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        // Log the request
        logger.info("Received registration request: email={}",
                email);

        Account account = this.accountRepository.findByEmail(email);

        if (account != null) {
            if (account.getPassword().equals(password)) {
                // Passwort korrekt und Account vorhanden

                String responseMessage = account.getEmail() + " Correct login credentials";
                logger.info("Registration response: {}", responseMessage);

                String token = Token.createToken(account.getID().toString());

                // String token = this.createToken(account.getID().toString());

                JSONObject responseObject = new JSONObject();
                responseObject.put("status", "success");
                responseObject.put("message", "Login correct");
                responseObject.put("token", token);
                return ResponseEntity.ok().body(responseObject.toString());

                // return ResponseEntity.ok()
                // .body("{\"status\":\"success\",\"message\":\"Login correct\"}");
            } else {

                String responseMessage = email + " Incorrect login credentials";
                logger.info("Registration response: {}", responseMessage);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect login credentials");
            }
        } else {
            String responseMessage = email + " Incorrect login credentials";
            logger.info("Registration response: {}", responseMessage);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect login credentials");
        }
    }

    @PostMapping(path = "/api/logout", produces = "application/json")
    public ResponseEntity<String> logoutUser(@RequestHeader("Authorization") String token) {
        // Log the request
        logger.info("Received logout request TODO: email={}",
                token);

        JSONObject responseObject = new JSONObject();
        responseObject.put("status", "success");
        responseObject.put("message", "Logout successful");
        return ResponseEntity.ok().body(responseObject.toString());
    }

    // Abrufen eines Benutzers nach ID.
    // @GetMapping("/{id}")
    @GetMapping(path = "api/account/{id}", produces = "application/json")
    public Account getAccountById(@PathVariable Integer id) {
        return accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private void saveAccountToDatabase(Integer accountID) {
        Account account = accountRepository.findByID(accountID);
        account.getCompany();
        account.getEmail();
        account.getFirstName();
        account.getLastName();
        account.getPhoneNumber();

        MongoClient mongoClient = MongoDBConnection.getMongoClient(this.mongoConnection);
        MongoDatabase database = mongoClient.getDatabase("MongoDB");
        MongoCollection<Document> collection = database.getCollection(ACCOUNTS_COLLECTION);

        // Account-Daten in ein MongoDB-Dokument umwandeln
        Document accountDoc = new Document();
        accountDoc.append("accountID", accountID)
                .append("email", account.getEmail())
                .append("firstName", account.getFirstName())
                .append("lastName", account.getLastName())
                .append("phoneNumber", account.getLastName());

        // Dokument in der MongoDB speichern
        collection.insertOne(accountDoc);
        logger.info("Account saved in Database");
    }

}
