package com.example.employeeapi.service;

import com.example.employeeapi.exception.DuplicateResourceException;
import com.example.employeeapi.exception.ResourceNotFoundException;
import com.example.employeeapi.model.Employee;
import com.example.employeeapi.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    
    private final EmployeeRepository employeeRepository;
    
    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
    
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }
    
    public Employee createEmployee(Employee employee) {
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new DuplicateResourceException("Employee already exists with email: " + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }
    
    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = getEmployeeById(id);
        
        // Check if email is being changed and if new email already exists
        if (!employee.getEmail().equals(employeeDetails.getEmail()) && 
            employeeRepository.existsByEmail(employeeDetails.getEmail())) {
            throw new DuplicateResourceException("Employee already exists with email: " + employeeDetails.getEmail());
        }
        
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmail(employeeDetails.getEmail());
        employee.setDepartment(employeeDetails.getDepartment());
        employee.setSalary(employeeDetails.getSalary());
        
        return employeeRepository.save(employee);
    }
    
    public void deleteEmployee(Long id) {
        Employee employee = getEmployeeById(id);
        employeeRepository.delete(employee);
    }
}
