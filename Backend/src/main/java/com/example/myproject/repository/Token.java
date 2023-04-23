package com.example.myproject.repository;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

public class Token {

	private final static Key key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
	private static final Logger logger = LogManager.getLogger(Token.class);
	private List<String> blacklist = new ArrayList<>();

	public static String createToken(String email) {
		Date expirationDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
		String token = Jwts.builder()
				.setSubject(email)
				.setExpiration(expirationDate)
				.signWith(key)
				.compact();
		// System.out.println("Key createToken: " + key);
		System.out.println("Token createToken: " + token);
		System.out.println("Tokencheck createToken: " + isValidToken(token));

		return token;
	}

	public static boolean isValidToken(String token) {
		try {
			if (token.startsWith("Bearer ")) {
				token = token.substring(7);
			}
			// System.out.println("Key isValidToken: " + key);
			System.out.println("Token isValidToken: " + token);
			// Token validieren und prüfen, ob die Signatur korrekt ist
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (ExpiredJwtException ex) {
			// Token abgelaufen
			System.err.println("Token ist abgelaufen: " + ex.getMessage());
			return false;
		} catch (UnsupportedJwtException ex) {
			// Ungültiges Token
			System.err.println("Ungültiges Token: " + ex.getMessage());
			return false;
		} catch (MalformedJwtException ex) {
			// Fehlerhaftes Token
			System.err.println("Fehlerhaftes Token: " + ex.getMessage());
			return false;
		} catch (SignatureException ex) {
			// Fehlerhafte Signatur
			System.err.println("Fehlerhafte Signatur: " + ex.getMessage());
			return false;
		} catch (Exception ex) {
			// Andere Fehler
			System.err.println("Fehler beim Validieren des Tokens: " + ex.getMessage());
			return false;
		}
	}

	public List<String> addTokenToDeletedList(String token) {
		blacklist.add(token);
		return blacklist;
	}

	public boolean checkTokenToDeletedList(String token) {
		return blacklist.contains(token);
	}

	public static Key getKey() {
		return key;
	}

}