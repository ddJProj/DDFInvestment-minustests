package com.ddfinv.backend.dto;

import lombok.Data;

/**
 * Maps application entities to DTOs
 */
@Data
public class PermissionDTO {
    private Long id;
    
    private String name;

    private String description;
}
