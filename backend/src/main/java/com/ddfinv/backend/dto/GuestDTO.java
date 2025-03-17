package com.ddfinv.backend.dto;

import java.util.Set;

import lombok.Data;

public class GuestDTO {
    
    @Data
    public class EmployeeDTO {
        private Long id;
        private String guestId;
        private Long userAccountId;
        private String firstName;
        private String lastName;
        private String email;
    }
}
