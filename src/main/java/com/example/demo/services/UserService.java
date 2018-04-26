package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.demo.modal.User;
import com.example.demo.repository.UserRepository;

@Service
@Transactional
public class UserService {

	
	private final UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository=userRepository;
	}
	
	public void saveMyUser(User user) {
		userRepository.save(user);
	}
	
	public List<User> showAllUsers(){
		List<User> users = new ArrayList<User>();
		for(User user:userRepository.findAll()) {
			users.add(user);
		}
		
		return users;
	}
	
	public void deleteMyUser(int id) {
		userRepository.deleteById(id);
	}
	
	public User editUser(int id) {
		return userRepository.findById(id).orElse(null);
	}
	
	public User findByUsernameAndPassword(String username, String Password) {
		return userRepository.findByUsernameAndPassword(username, Password);
	}
}
