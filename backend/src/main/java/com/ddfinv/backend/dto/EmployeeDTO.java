package com.ddfinv.backend.dto;

import java.util.Set;

import lombok.Data;

@Data
public class EmployeeDTO {
    private Long id;
    private String employeeId;
    private Long userAccountId;
    private String firstName;
    private String lastName;
    private String email;
    private String locationId;
    private String title;
    private Set<String> assignedClientList;
}
