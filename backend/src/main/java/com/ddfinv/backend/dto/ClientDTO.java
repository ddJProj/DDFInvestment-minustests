package com.ddfinv.backend.dto;

import lombok.Data;

@Data
public class ClientDTO {
    private Long id;
    private String clientId;
    private Long userAccountId;
    private String firstName;
    private String lastName;
    private String email;
    private String assignedEmployeeId;
    private String assignedEmployeeName;    
}
