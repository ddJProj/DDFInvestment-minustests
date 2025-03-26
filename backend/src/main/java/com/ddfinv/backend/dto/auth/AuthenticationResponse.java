package com.ddfinv.backend.dto.auth;

import java.util.Set;

import com.ddfinv.core.domain.enums.Permissions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private String role; // return the role of a user
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    // Set of Permissions associated with this UserAccount
    private Set<String> permissions;

}
