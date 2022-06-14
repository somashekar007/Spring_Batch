package com.example.spring.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.example.spring.batch.model.Employee;
import com.example.spring.batch.model.EmployeeDTO;
@Component
public class EmployeeProcessor implements ItemProcessor<Employee, Employee>  {

	@Override
	public Employee process(Employee item) throws Exception {
        
        Employee employee = new Employee();
        System.out.println(item.toString());

        BeanUtils.copyProperties(item, employee);
		System.out.println(employee.toString());
		return employee;
	}

}
