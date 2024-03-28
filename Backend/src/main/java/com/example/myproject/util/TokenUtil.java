package com.example.myproject.util;


import java.security.Key;
import java.util.Date;

import com.example.myproject.model.Account;
import com.example.myproject.repository.AccountRepository;
import com.example.myproject.repository.Token;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.util.List;
import java.util.ArrayList;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.EntryMessage;
import org.apache.logging.log4j.message.FlowMessageFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.util.MessageSupplier;
import org.apache.logging.log4j.util.Supplier;
import org.springframework.stereotype.Component;

@Component
public class TokenUtil {

    private static final Logger logger = LogManager.getLogger(TokenUtil.class);
    private static final Key key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    private final AccountRepository accountRepository;

    public TokenUtil(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public static String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
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
}
