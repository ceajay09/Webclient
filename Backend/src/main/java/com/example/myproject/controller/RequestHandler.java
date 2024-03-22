package com.example.myproject.controller;

import com.example.myproject.controller.RabbitMQ.ReceiveFromQueue;
import com.example.myproject.repository.Account;

import com.example.myproject.repository.AccountRepository;
import com.example.myproject.repository.Token;

import com.example.myproject.repository.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import com.mongodb.client.MongoClients;
import com.mongodb.MongoClientSettings;
import com.mongodb.ConnectionString;
import com.mongodb.MongoException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;

// @RequestMapping("api/")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@PropertySource("classpath:application.properties")
public class RequestHandler {

    private static final Logger logger = LogManager.getLogger(RequestHandler.class);

    // @Value("${sharedSecret}")
    // private static String sharedSecret;

    // private static final String SECRET_KEY = sharedSecret; // Geheimer Schlüssel
    // für die Signatur
    // private static final byte[] SECRET_KEY_BYTES =
    // SECRET_KEY.getBytes(StandardCharsets.UTF_8); // Secret Key als
    // // Byte-Array

    @Value("${mongo.connection.string}")
    private String mongoConnection;

    private final String ACCOUNTS_COLLECTION = "accounts";
    private boolean alreadyExecuted;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ReceiveFromQueue receiveFromQueue;

    // Erstellen eines neuen Benutzers
    @PostMapping(path = "/api/register", produces = "application/json")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> request) {
        this.retrieveAllAccountsAndSaveToRepository(accountRepository);

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
        this.retrieveAllAccountsAndSaveToRepository(accountRepository);
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
    public Account getAccountById(@PathVariable Integer id) {
        return accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // @GetMapping("/api/videoData")
    // public ResponseEntity<Map<String, String>> getVideoData() {
    // Map<String, String> videoData = new HashMap<>();
    // videoData.put("videoUrl",
    // "http://streamingProjektServer:Port/video/titelDesVideos");

    // return ResponseEntity.ok(videoData);
    // }

    private void saveAccountToDatabase(Integer accountID) {
        Account account = accountRepository.findByID(accountID);

        try {
            MongoClient mongoClient = this.getMongoClient(this.mongoConnection);
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

    public void retrieveAllAccountsAndSaveToRepository(AccountRepository accountRepository) {
        if (this.alreadyExecuted == false) {
            int count = 0;
            // Verbindung zur MongoDB-Datenbank herstellen
            MongoClient mongoClient = this.getMongoClient(mongoConnection);
            MongoDatabase database = mongoClient.getDatabase("MongoDB");
            MongoCollection<Document> collection = database.getCollection(ACCOUNTS_COLLECTION);

            // Alle Dokumente in der Collection abrufen
            FindIterable<Document> documents = collection.find();

            // Durch alle Dokumente iterieren und Accounts erstellen und auf dem
            // AccountRepository speichern
            for (Document doc : documents) {
                Integer accountId = doc.getInteger("accountID");
                String email = doc.getString("email");
                String password = doc.getString("password");
                String firstName = doc.getString("firstName");
                String lastName = doc.getString("lastName");
                String phoneNumber = doc.getString("phoneNumber");

                Account account = new Account();
                account.setID(accountId);
                account.setEmail(email);
                account.setPassword(password);
                account.setFirstName(firstName);
                account.setLastName(lastName);
                account.setPhoneNumber(phoneNumber);

                accountRepository.save(account);
                logger.info("ID-Account " + account.getID() + " saved in Database");
                count += 1;
            }
            logger.info(count + " Account(s) successfully retrieved from Database");

            // MongoDB-Verbindung schließen
            mongoClient.close();
            this.alreadyExecuted = true;
        } else {
            logger.info("Accounts retrieved already from Database"); // TODO: Delete or not?
        }
    }

    private MongoClient getMongoClient(String mongoConnection) {
        MongoClient mongoClient = null;
        String connectionString = mongoConnection + "@mywebapp.pjxdzvf.mongodb.net/?retryWrites=true&w=majority";

        // Create a new client and connect to the server
        try {
            ConnectionString connString = new ConnectionString(connectionString);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connString)
                    .build();
            // Send a ping to confirm a successful connection
            mongoClient = MongoClients.create(settings);
            // MongoDatabase database = mongoClient.getDatabase("MongoDB");
            // database.runCommand(new Document("ping", 1));
            // System.out.println("Pinged your deployment. You successfully connected to
            // MongoDB!");
        } catch (MongoException e) {
            logger.warn("Connection to MongoDB Failed " + e);
            e.printStackTrace();
        }
        return mongoClient;
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
