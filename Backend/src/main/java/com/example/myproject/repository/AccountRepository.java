package com.example.myproject.repository;

//import org.springframework.data.jpa.repository.JpaRepository;
import com.example.myproject.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

	public Account findByEmail(String email);

	public Account findByID(String id);
}
