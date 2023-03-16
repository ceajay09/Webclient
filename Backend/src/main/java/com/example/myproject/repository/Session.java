package com.example.myproject.repository;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Session {

		public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
		@Id
		private Integer ID;
		private String token;
	
	
}
