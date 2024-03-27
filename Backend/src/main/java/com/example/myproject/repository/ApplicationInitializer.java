package com.example.myproject.repository;

import com.example.myproject.model.Account;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInitializer {

    private final AccountRepository accountRepository;
    private final MongoDbService mongoDbService;
    private static final Logger logger = LogManager.getLogger(ApplicationInitializer.class);

    @Value("${spring.data.mongodb.uri}")
    private String mongoConnection;
    private final String ACCOUNTS_COLLECTION = "accounts";

    @Autowired
    public ApplicationInitializer(AccountRepository accountRepository, MongoDbService mongoDbService) {
        this.accountRepository = accountRepository;
        this.mongoDbService = mongoDbService;
    }

    @PostConstruct
    public void init() {
        retrieveAllAccountsAndSaveToRepository();
    }

    public void retrieveAllAccountsAndSaveToRepository() {
        int count = 0;
        // Verbindung zur MongoDB-Datenbank herstellen
        MongoClient mongoClient = mongoDbService.getMongoClient(mongoConnection);
        MongoDatabase database = mongoClient.getDatabase("MongoDB");
        MongoCollection<Document> collection = database.getCollection(ACCOUNTS_COLLECTION);

        // Alle Dokumente in der Collection abrufen
        FindIterable<Document> documents = collection.find();

        // Durch alle Dokumente iterieren und Accounts erstellen und auf dem
        // AccountRepository speichern
        for (Document doc : documents) {
            String accountId = doc.getInteger("accountID").toString(); //TODO: Zukünftig handelt es sich bei der id um einen String
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

            logger.info(count + " Account(s) successfully retrieved from Database");

            // MongoDB-Verbindung schließen
            mongoClient.close();
        }
    }
}
