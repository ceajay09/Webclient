package com.example.myproject.model;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.ConnectionString;
import com.mongodb.MongoException;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBConnection {

    public static MongoClient getMongoClient(String mongoConnection) {
        MongoClient mongoClient = null;

        String connectionString = mongoConnection + "@mywebapp.pjxdzvf.mongodb.net/?retryWrites=true&w=majority";

        // String connectionString =
        // "mongodb+srv://cjaquiery:uyMOfIs6BtUwxWRr@mywebapp.pjxdzvf.mongodb.net/?retryWrites=true&w=majority";

        ConnectionString connString = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .build();
        // Create a new client and connect to the server
        try {
            // Send a ping to confirm a successful connection
            mongoClient = MongoClients.create(settings);
            MongoDatabase database = mongoClient.getDatabase("MongoDB");
            database.runCommand(new Document("ping", 1));
            System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return mongoClient;
    }
}