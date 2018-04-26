package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.modal.User;

public interface UserRepository extends CrudRepository<User, Integer> {

	public User findByUsernameAndPassword(String username, String Password);
	
}
