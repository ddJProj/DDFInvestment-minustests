package com.ddfinv.backend.dto.actions;

import lombok.Data;

@Data
public class UpdatePasswordDTO {

/**
 * 
 * TODO: 
 * add @NotBlank("messages")
 * add @Size(min = value, message = "")
 * add @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).*$", message = "")
 * 
 */

    private String currentPassword;


    private String newPassword;


    private String passwordConfirmation;

}
