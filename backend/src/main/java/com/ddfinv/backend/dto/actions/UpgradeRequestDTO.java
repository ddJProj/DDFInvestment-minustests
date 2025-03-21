package com.ddfinv.backend.dto.actions;

import java.time.LocalDateTime;

import org.springframework.cglib.core.Local;

import com.ddfinv.core.domain.enums.UpgradeRequestStatus;

import lombok.Data;

@Data
public class UpgradeRequestDTO {
    // utilized when creating requests as a guest:
    private Long userAccountId;
    private String details;


    // utilized when managing requests as an elevated permission user:
    private Long id;
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private LocalDateTime requestDate;
    private UpgradeRequestStatus status;
}
