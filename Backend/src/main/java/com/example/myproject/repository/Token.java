package com.example.myproject.repository;

import java.security.Key;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.List;
import java.util.ArrayList;

public class Token {

	private final static Key key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
	private static List<String> blacklist = new ArrayList<>();

	public static String createToken(String accountID) {
		Date expirationDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
		String token = Jwts.builder()
				.setSubject(accountID)
				.setExpiration(expirationDate)
				.signWith(key)
				.compact();
		return token;
	}

	public static List<String> addTokenToDeletedList(String token) {
		blacklist.add(token);
		return blacklist;
	}

	public static boolean checkTokenToDeletedList(String token) {
		return blacklist.contains(token);
	}

}