package com.example.myproject.repository;

import org.springframework.beans.factory.annotation.Autowired;

public class sessionTermination implements Runnable {

@Autowired
private com.example.myproject.repository.SessionRepository sessionRepository;
private Integer id;

public sessionTermination(Integer id) {
	this.id =id;
}
	@Override
	public void run() {
		for (int i=0; i<86400; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.sessionRepository.deleteById(this.id);
		
	}

}
