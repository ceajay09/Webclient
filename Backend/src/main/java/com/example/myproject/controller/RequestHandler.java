package com.example.myproject.controller;

import com.example.myproject.controller.RabbitMQ.ReceiveFromQueue;
import com.example.myproject.model.Account;
import com.example.myproject.repository.*;

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

    @Value("${spring.data.mongodb.uri}")
    private String mongoConnection;

    private final String ACCOUNTS_COLLECTION = "accounts";
    private boolean alreadyExecuted;

    public RequestHandler(AccountRepository accountRepository,
                          ReceiveFromQueue receiveFromQueue,
                          MongoDbService mongoDbService) {
        this.accountRepository = accountRepository;
        this.receiveFromQueue = receiveFromQueue;
        this.mongoDbService = mongoDbService;
    }

    // Erstellen eines neuen Benutzers
    @PostMapping(path = "/api/register", produces = "application/json")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> request) {
//        this.retrieveAllAccountsAndSaveToRepository(accountRepository);

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

            String accountID = this.accountRepository.findByEmail(account.getEmail()).getID();
            this.saveAccountToDatabase(accountID);

            // Integer id = account.getID();
            logger.info(account.getEmail() + " created / " + "ID = " + accountID);
//            System.out.println(account.getEmail() + " created / " + "ID = " + accountID);

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
//        this.retrieveAllAccountsAndSaveToRepository(accountRepository);
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

                String token = Token.createToken(account.getEmail());

                // String token = this.createToken(account.getID().toString());

                JSONObject responseObject = new JSONObject();
                responseObject.put("status", "success");
                responseObject.put("message", "Login correct");
                responseObject.put("token", token);
                // responseObject.put("company", account.getCompany());
                // responseObject.put("email", account.getEmail());
                // responseObject.put("firstName", account.getFirstName());
                // responseObject.put("ID", account.getID());
                // responseObject.put("lastName", account.getLastName());
                // responseObject.put("password", account.getPassword());
                // responseObject.put("phoneNumber", account.getPhoneNumber());
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
            logger.info("Registration response: {}", responseMessage); //TODO: warn?
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect login credentials");
        }
    }

    @PostMapping(path = "/api/logout", produces = "application/json")
    public ResponseEntity<String> logoutUser(@RequestHeader("Authorization") String token) {
        // Log the request

        logger.info("Received logout request from email={}",
                getAccountFromToken(token).getEmail());

        JSONObject responseObject = new JSONObject();
        responseObject.put("status", "success");
        responseObject.put("message", "Logout successful");
        return ResponseEntity.ok().body(responseObject.toString());
    }

    // Abrufen eines Benutzers nach ID.
    // @GetMapping("/{id}")
    @GetMapping(path = "api/account/{id}", produces = "application/json")
    public Account getAccountById(@PathVariable String id) {
        return accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // @GetMapping("/api/videoData")
    // public ResponseEntity<Map<String, String>> getVideoData() {
    // Map<String, String> videoData = new HashMap<>();
    // videoData.put("videoUrl",
    // "http://streamingProjektServer:Port/video/titelDesVideos");

    // return ResponseEntity.ok(videoData);
    // }

    private void saveAccountToDatabase(String accountID) {
        Account account = accountRepository.findByID(accountID);

        try {
            MongoClient mongoClient = mongoDbService.getMongoClient(this.mongoConnection);
            MongoDatabase database = mongoClient.getDatabase("MongoDB");
            MongoCollection<Document> collection = database.getCollection(ACCOUNTS_COLLECTION);

            // Account-Daten in ein MongoDB-Dokument umwandeln
            Document accountDoc = new Document();
            accountDoc.append("accountID", accountID)
                    .append("email", account.getEmail())
                    .append("password", account.getPassword())
                    .append("firstName", account.getFirstName())
                    .append("lastName", account.getLastName())
                    .append("phoneNumber", account.getLastName());

            // Dokument in der MongoDB speichern
            collection.insertOne(accountDoc);
            logger.info(account.getID() + " saved in Database");
            mongoClient.close();
        } catch (Exception e) {
            logger.warn("Verbindung mit MongoDB fehlgeschlagen " + e);
        }

    }

    // @RequestMapping(value = "/api/data", method = RequestMethod.GET)
    @RequestMapping(value = "/api/dashboard", method = RequestMethod.GET)
    public ResponseEntity<?> getData(HttpServletRequest request) {
        // Token aus dem Authorization-Header der Anfrage erhalten
        String token = request.getHeader("Authorization");

        // Token validieren (z.B. mit einer JWT-Bibliothek)
        if (Token.isValidToken(token)) {
            // Token ist gültig, Daten zurückgeben
            Account account = getAccountFromToken(token); // Annahme: Methode, um Account-Daten aus dem Token zu
                                                          // extrahieren
            if (account != null) {
                // Account-Daten erfolgreich abgerufen, als JSON-Response zurückgeben
                JSONObject responseObject = new JSONObject();
                responseObject.put("status", "success");
                responseObject.put("company", account.getCompany());
                responseObject.put("email", account.getEmail());
                responseObject.put("firstName", account.getFirstName());
                responseObject.put("ID", account.getID());
                responseObject.put("lastName", account.getLastName());
                responseObject.put("password", account.getPassword());
                responseObject.put("phoneNumber", account.getPhoneNumber());
                responseObject.put("account", account); // Account-Daten im Response-Objekt speichern
                logger.info(account.getID() + " transfered to Frontend");
                return ResponseEntity.ok().body(responseObject.toString());
            }
        } else {
            logger.warn("getAccountFromToken: Token invalid:" + token);
        }

        // Token ist ungültig, Fehlermeldung zurückgeben
        JSONObject responseObject = new JSONObject();
        responseObject.put("status", "error");
        responseObject.put("message", "Invalid token");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseObject.toString());
    }

    public Account getAccountFromToken(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            // Token parsen und Claims (Daten) auslesen
            Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(Token.getKey()).build()
                    .parseClaimsJws(token);
            Claims claims = jws.getBody();
            String email = claims.getSubject();

            return this.accountRepository.findByEmail(email);
        } catch (Exception e) {
            // Bei Fehler oder ungültigem Token wird eine Exception geworfen
            logger.warn("Retrieve Account with Token failed " + e);
            return null;
        }
    }

    public void setAlreadyExecuted(boolean alreadyExecuted) {
        this.alreadyExecuted = alreadyExecuted;
    }

}
