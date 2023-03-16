package com.example.myproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<com.example.myproject.repository.Session, Integer>{
	
	public com.example.myproject.repository.Session findByID(Integer ID);
	
	public com.example.myproject.repository.Session findByToken(String token);

}
