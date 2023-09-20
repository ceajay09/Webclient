package demo.src.main.java.com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {

	public Video findByID(Integer id);

}
