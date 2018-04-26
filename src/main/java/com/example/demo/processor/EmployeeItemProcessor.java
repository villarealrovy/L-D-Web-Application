package com.example.demo.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.example.demo.modal.employee;

public class EmployeeItemProcessor implements ItemProcessor<employee, employee>{
	
	private static final Logger log = LoggerFactory.getLogger(EmployeeItemProcessor.class);
	
    @Override
    public employee process(final employee employee) throws Exception {

        final employee transformedPerson = new employee(employee.getUsername(), employee.getSystem_role(), employee.getFull_name(), employee.getGender(), employee.getEmail_account(), employee.getGdc_name(), employee.getDepartment(), employee.getDivision(), employee.getLOS());

        log.info("Converting (" + employee + ") into (" + transformedPerson + ")");

        return transformedPerson;
    }
}
