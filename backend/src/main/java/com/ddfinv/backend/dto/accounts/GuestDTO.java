package com.ddfinv.backend.dto.accounts;

import java.util.Set;

import lombok.Data;

// TODO: do I need to add more? check guest requirements 

@Data
public class GuestDTO {
    private Long id;
    private Long userAccountId;
    private String firstName;
    private String lastName;
    private String email;
}
