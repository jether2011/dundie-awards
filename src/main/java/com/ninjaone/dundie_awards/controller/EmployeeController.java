package com.ninjaone.dundie_awards.controller;

import com.ninjaone.dundie_awards.controller.response.EmployeeResponse;
import com.ninjaone.dundie_awards.model.Employee;
import com.ninjaone.dundie_awards.services.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);
    private static final String CREATED_URI = "/api/v1/employees/%s";

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * {@code POST  /employees} : Create a new employee.
     *
     * @param employee the employee to create
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employee
     */
    @PostMapping("/employees")
    public ResponseEntity<EmployeeResponse> createEmployee(@Validated @RequestBody Employee employee) throws URISyntaxException {
        LOG.info("Starting to create employee: {}", employee);

        if (employee.getId() != 0) {
            return ResponseEntity.badRequest().body(null);
        }

        var response = EmployeeResponse.from(employeeService.createEmployee(employee));
        LOG.info("Employee created: {}", response);

        return ResponseEntity.created(new URI(CREATED_URI.formatted(response.id()))).body(response);
    }

    /**
     * {@code GET  /employees} : get all employees.
     *
     * @param pageable the pagination information
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employees in body
     */
    @GetMapping("/employees")
    public ResponseEntity<Page<EmployeeResponse>> getAllEmployees(
            @PageableDefault(page = 0, size = 30, sort = "id") final Pageable pageable
    ) {
        LOG.info("Starting to get all employees with pageable: {}", pageable);
        var response = employeeService.getAllEmployees(pageable).map(EmployeeResponse::from);

        return ResponseEntity.ok().body(response);
    }

    /**
     * {@code GET  /employees/:id} : get the "id" employee.
     *
     * @param id the id of the employee to retrieve
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employee, or with status {@code 204 (NO_CONTENT)}
     */
    @GetMapping("/employees/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
        LOG.info("Starting to get employee with id: {}", id);

        Optional<Employee> optionalEmployee = employeeService.getEmployeeById(id);

        return optionalEmployee.map(employee -> ResponseEntity.ok().body(EmployeeResponse.from(employee)))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));

    }

    /**
     * {@code PUT  /employees/:id} : Updates an existing employee.
     *
     * @param id              the id of the employee to update
     * @param employeeDetails the employee to update
     * @return the {@link ResponseEntity} with status {@code 202 (ACCEPTED)} and with body the updated employee,
     */
    @PutMapping("/employees/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {
        LOG.info("Starting to update employee with id: {}", id);

        Optional<Employee> optionalEmployee = employeeService.updateEmployee(id, employeeDetails);
        if (optionalEmployee.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        var response = EmployeeResponse.from(optionalEmployee.get());
        LOG.info("Employee updated: {}", response);

        return ResponseEntity.accepted().body(response);
    }

    /**
     * {@code DELETE  /employees/:id} : delete the "id" employee.
     *
     * @param id the id of the employee to delete
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)} or {@code 404 (NOT_FOUND)} or {@code 202 (ACCEPTED)}
     */
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable Long id) {
        LOG.info("Starting to delete employee with id: {}", id);
        return ResponseEntity.accepted().body(employeeService.deleteEmployee(id));
    }
}