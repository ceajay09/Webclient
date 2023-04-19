package com.example.myproject.repository;

import java.security.Key;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.List;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.myproject.model.MongoDBConnection;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Token {

	private final static Key key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
	private List<String> blacklist = new ArrayList<>();

	public static String createToken(String accountID) {
		Date expirationDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
		String token = Jwts.builder()
				.setSubject(accountID)
				.setExpiration(expirationDate)
				.signWith(key)
				.compact();

		// saveAccountIDToDatabase(token, accountID);
		return token;
	}

	// private void saveAccountIDToDatabase(String token, String accountID) {
	// // TODO
	// Account account = accountRepository.findByID(Integer.parseInt(accountID));
	// account.getCompany();
	// account.getEmail();
	// account.getFirstName();
	// account.getLastName();
	// account.getPhoneNumber();

	// MongoClient mongoClient = MongoDBConnection.getMongoClient();
	// MongoDatabase database = mongoClient.getDatabase("MongoDB");
	// MongoCollection<Document> collection =
	// database.getCollection(ACCOUNTS_COLLECTION);

	// // Account-Daten in ein MongoDB-Dokument umwandeln
	// Document accountDoc = new Document();
	// accountDoc.append("accountID", accountID)
	// .append("email", account.getEmail())
	// .append("firstName", account.getFirstName())
	// .append("lastName", account.getLastName())
	// .append("phoneNumber", account.getLastName())
	// .append("token", token);

	// // Dokument in der MongoDB speichern
	// collection.insertOne(accountDoc);
	// }

	public List<String> addTokenToDeletedList(String token) {
		blacklist.add(token);
		return blacklist;
	}

	public boolean checkTokenToDeletedList(String token) {
		return blacklist.contains(token);
	}

}