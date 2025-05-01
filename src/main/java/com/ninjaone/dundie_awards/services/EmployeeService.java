package com.ninjaone.dundie_awards.services;

import com.ninjaone.dundie_awards.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Optional;

public interface EmployeeService {
    Employee createEmployee(Employee employee);

    Optional<Employee> updateEmployee(Long id, Employee employee);

    Map<String, Boolean> deleteEmployee(Long id);

    Optional<Employee> getEmployeeById(Long id);

    Page<Employee> getAllEmployees(Pageable pageable);
}
