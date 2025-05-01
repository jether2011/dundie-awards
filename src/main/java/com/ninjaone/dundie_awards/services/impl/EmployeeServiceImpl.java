package com.ninjaone.dundie_awards.services.impl;

import com.ninjaone.dundie_awards.MessageBroker;
import com.ninjaone.dundie_awards.model.Employee;
import com.ninjaone.dundie_awards.model.Organization;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import com.ninjaone.dundie_awards.services.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {
    private static final String EMPLOYEE_UPDATED = "Employee updated";
    private static final String EMPLOYEE_DELETED = "Employee deleted";
    private static final String EMPLOYEE_CREATED = "Employee created";
    private static final String DELETED_KEY = "deleted";

    private final EmployeeRepository employeeRepository;
    private final MessageBroker messageBroker;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, MessageBroker messageBroker) {
        this.employeeRepository = employeeRepository;
        this.messageBroker = messageBroker;
    }

    @Transactional
    @Override
    public Employee createEmployee(Employee employee) {
        var created = employeeRepository.save(employee);
        messageBroker.sendMessage(EMPLOYEE_CREATED);
        return created;
    }

    @Transactional
    @Override
    public Optional<Employee> updateEmployee(Long id, Employee employee) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isEmpty()) {
            return Optional.empty();
        }

        var organization = new Organization();
        organization.setId(employee.getOrganizationId());

        Employee toUpdate = optionalEmployee.get();
        toUpdate.setFirstName(employee.getFirstName());
        toUpdate.setLastName(employee.getLastName());
        toUpdate.setOrganization(organization);

        Employee updatedEmployee = employeeRepository.save(toUpdate);
        messageBroker.sendMessage(EMPLOYEE_UPDATED);

        return Optional.of(updatedEmployee);
    }

    @Transactional
    @Override
    public Map<String, Boolean> deleteEmployee(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isEmpty()) {
            return Map.of(DELETED_KEY, false);
        }

        Employee employee = optionalEmployee.get();
        employeeRepository.delete(employee);
        messageBroker.sendMessage(EMPLOYEE_DELETED);

        return Map.of(DELETED_KEY, true);
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Page<Employee> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }
}
