package com.ninjaone.dundie_awards;

import com.ninjaone.dundie_awards.model.Employee;
import com.ninjaone.dundie_awards.model.Organization;
import com.ninjaone.dundie_awards.services.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EmployeeServiceImplTest extends DatabaseConfigTest {
    @Autowired
    private EmployeeService employeeService;

    @Test
    @DisplayName("Should create employee successfully")
    void shouldCreateEmployeeSuccessfully() {
        var organization = new Organization();
        organization.setId(1L);
        organization.setName("Test Organization");
        var employee = new Employee("John", "Doe", organization);

        var createdEmployee = employeeService.createEmployee(employee);

        assertNotNull(createdEmployee);
        assertEquals(employee.getFirstName(), createdEmployee.getFirstName());
        assertEquals(employee.getLastName(), createdEmployee.getLastName());
        assertEquals(organization.getName(), createdEmployee.getOrganizationName());
        assertEquals(organization.getId(), createdEmployee.getOrganizationId());
    }
}
