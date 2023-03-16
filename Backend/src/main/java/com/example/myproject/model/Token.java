package com.example.myproject.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Token {

	@JsonProperty("token")
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}

// set Token Methode
//rand = new Random();
//		String t ="";
//		for (int i=0; i <128; i++) {
//		t+= Character.toString((char)rand.nextInt(122-97)+97);
//		}
//		System.out.println(t);
//		Session session = new Session();
//		session.setToken(t);