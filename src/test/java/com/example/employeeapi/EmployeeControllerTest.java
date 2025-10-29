package com.example.employeeapi;

import com.example.employeeapi.model.Employee;
import com.example.employeeapi.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @BeforeEach
    public void setup() {
        employeeRepository.deleteAll();
    }
    
    @Test
    public void testCreateEmployee() throws Exception {
        Employee employee = new Employee("John", "Doe", "john.doe@example.com", "IT", 75000.0);
        
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.department").value("IT"))
                .andExpect(jsonPath("$.salary").value(75000.0))
                .andExpect(jsonPath("$.id").exists());
    }
    
    @Test
    public void testGetAllEmployees() throws Exception {
        Employee emp1 = new Employee("John", "Doe", "john.doe@example.com", "IT", 75000.0);
        Employee emp2 = new Employee("Jane", "Smith", "jane.smith@example.com", "HR", 65000.0);
        employeeRepository.save(emp1);
        employeeRepository.save(emp2);
        
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));
    }
    
    @Test
    public void testGetEmployeeById() throws Exception {
        Employee employee = new Employee("John", "Doe", "john.doe@example.com", "IT", 75000.0);
        Employee savedEmployee = employeeRepository.save(employee);
        
        mockMvc.perform(get("/api/employees/" + savedEmployee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }
    
    @Test
    public void testGetEmployeeByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/employees/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee not found with id: 999"));
    }
    
    @Test
    public void testUpdateEmployee() throws Exception {
        Employee employee = new Employee("John", "Doe", "john.doe@example.com", "IT", 75000.0);
        Employee savedEmployee = employeeRepository.save(employee);
        
        Employee updatedEmployee = new Employee("John", "Doe", "john.updated@example.com", "Finance", 85000.0);
        
        mockMvc.perform(put("/api/employees/" + savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john.updated@example.com"))
                .andExpect(jsonPath("$.department").value("Finance"))
                .andExpect(jsonPath("$.salary").value(85000.0));
    }
    
    @Test
    public void testDeleteEmployee() throws Exception {
        Employee employee = new Employee("John", "Doe", "john.doe@example.com", "IT", 75000.0);
        Employee savedEmployee = employeeRepository.save(employee);
        
        mockMvc.perform(delete("/api/employees/" + savedEmployee.getId()))
                .andExpect(status().isNoContent());
        
        mockMvc.perform(get("/api/employees/" + savedEmployee.getId()))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testCreateEmployeeWithValidationErrors() throws Exception {
        Employee employee = new Employee("", "", "invalid-email", "", null);
        
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors").exists());
    }
    
    @Test
    public void testCreateEmployeeWithDuplicateEmail() throws Exception {
        Employee employee1 = new Employee("John", "Doe", "john.doe@example.com", "IT", 75000.0);
        employeeRepository.save(employee1);
        
        Employee employee2 = new Employee("Jane", "Smith", "john.doe@example.com", "HR", 65000.0);
        
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee2)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Employee already exists with email: john.doe@example.com"));
    }
}
