package com.ninjaone.dundie_awards.controller.response;

import com.ninjaone.dundie_awards.model.Employee;

public record EmployeeResponse(long id, String firstName, String lastName, Integer dundieAwards, Long organization) {
    public static EmployeeResponse from(final Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getDundieAwards() == null ? 0 : employee.getDundieAwards(),
                employee.getOrganizationId()
        );
    }
}
