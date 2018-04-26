package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.demo.modal.Rights;
import com.example.demo.repository.RightsRepository;
import com.sample.model.User;

@Service
@Transactional

public class RightsService {

private final RightsRepository rightsRepository;
	
	public RightsService(RightsRepository rightsRepository) {
		this.rightsRepository=rightsRepository;
	}
	
	public void saveUser(Rights rights) {
		rightsRepository.save(rights);
	}
	
	public List<Rights> showAllRights(){
		List<Rights>rights = new ArrayList<Rights>();
		for(Rights right:rightsRepository.findAll()) {
			rights.add(right);
		}
		return rights;
	}
	
	public void deleteMyUser(int employeeID) {
		rightsRepository.deleteById(employeeID);
	}
	public Rights editUser(int employeeID) {
		return rightsRepository.findById(employeeID).orElse(null);
	}
 
}
