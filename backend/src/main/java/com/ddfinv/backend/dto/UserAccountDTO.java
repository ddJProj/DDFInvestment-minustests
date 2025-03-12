package com.ddfinv.backend.dto;

import java.util.Set;

import com.ddfinv.core.entity.enums.Role;

import lombok.Data;

/**
 * Maps application entities to DTOs
 */
@Data
 public class UserAccountDTO {

// TODO: DEFINE UserAccount data transfer object
    private long id;

    /**
     * The email address used for an account. Must be unique and not blank.
     */
    private String email;

    /**
     *  used when updating a password or during initial UserAccount creation
     */
    private String password;

    /**
     * The first name of the user
     */
    private String firstName;

    /**
     * The last name of the user
     */
    private String lastName;
    
    /**
     * The role that this UserAccount instance holds
     */
    private Role role;


    private Set<String> permissionName;

}
