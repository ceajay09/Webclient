package com.example.myproject.repository;

//import org.springframework.data.jpa.repository.JpaRepository;
import com.example.myproject.model.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends MongoRepository<Session, String> {
	
	public Session findByID(Integer ID);
	
	public Session findByToken(String token);

}
