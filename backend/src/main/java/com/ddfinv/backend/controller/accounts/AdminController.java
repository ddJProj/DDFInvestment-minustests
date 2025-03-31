package com.ddfinv.backend.controller.accounts;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ddfinv.backend.dto.accounts.ClientDTO;
import com.ddfinv.backend.dto.actions.UpgradeRequestDTO;
import com.ddfinv.backend.exception.ResourceNotFoundException;
import com.ddfinv.backend.service.PermissionHandlerService;
import com.ddfinv.backend.service.accounts.EmployeeService;
import com.ddfinv.backend.service.accounts.GuestService;
import com.ddfinv.backend.service.accounts.UserAccountService;
import com.ddfinv.backend.service.actions.UpgradeRequestService;
import com.ddfinv.core.domain.enums.Permissions;
import com.ddfinv.core.domain.enums.Role;
import com.ddfinv.core.repository.UserAccountRepository;


@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UpgradeRequestService upgradeRequestService;


    private final UserAccountService userAccountService;
    private final EmployeeService employeeService;
    private final PermissionHandlerService permissionHandlerService;
    private final UserAccountRepository userAccountRepository;

    @Autowired
    public AdminController(UserAccountService userAccountService, EmployeeService employeeService, PermissionHandlerService permissionHandlerService, UserAccountRepository userAccountRepository, UpgradeRequestService upgradeRequestService){
        this.userAccountService = userAccountService;
        this.employeeService = employeeService;
        this.upgradeRequestService = upgradeRequestService;
        this.permissionHandlerService = permissionHandlerService;
        this.userAccountRepository = userAccountRepository;
    }



    @GetMapping("/system-info")
    public ResponseEntity<?> getSystemInfo() {
        // check permissions if needed
        if (!permissionHandlerService.currentUserHasPermission(Permissions.VIEW_ACCOUNTS)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        Map<String, Object> sysInfo = new HashMap<>();
        sysInfo.put("totalUserAccounts", userAccountRepository.count());
        sysInfo.put("totalAdminAccounts", userAccountRepository.countByRole(Role.admin));
        sysInfo.put("totalEmployeeAccounts", userAccountRepository.countByRole(Role.employee));
        sysInfo.put("totalClientAccounts", userAccountRepository.countByRole(Role.client));
        sysInfo.put("totalGuestAccounts", userAccountRepository.countByRole(Role.guest));
        
        return ResponseEntity.ok(sysInfo);
    }

    @GetMapping("/upgrade-requests/{userAccountId}")
    public ResponseEntity<?> getUserUpgradeRequests(@PathVariable Long userAccountId) throws ResourceNotFoundException {
        List<UpgradeRequestDTO> requests = upgradeRequestService.getUserUpgradeRequestsById(userAccountId);
        return (requests != null) ? ResponseEntity.ok(requests): ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/pending-requests")
    public ResponseEntity<?> getPendingRequests() {
        // check permissions
        if (!permissionHandlerService.currentUserHasPermission(Permissions.VIEW_ACCOUNTS)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        try {
            List<UpgradeRequestDTO> pendingRequests = upgradeRequestService.getPendingUpgradeRequests();
            return ResponseEntity.ok(pendingRequests);
        } catch (Exception e) {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @PutMapping("/upgrade-requests/{id}/reject")
    public ResponseEntity<?> rejectUpgradeRequest(
            @PathVariable Long id, 
            @RequestParam("reason") String rejectionReason) {
        // Check permissions
        if (!permissionHandlerService.currentUserHasPermission(Permissions.VIEW_ACCOUNTS)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        try {
            boolean success = upgradeRequestService.rejectRequest(id, rejectionReason);
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PutMapping("/upgrade-requests/{id}/approve")
    public ResponseEntity<?> approveUpgradeRequest(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        // Check permissions
        if (!permissionHandlerService.currentUserHasPermission(Permissions.CREATE_CLIENT)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        try {
            ClientDTO newClient = upgradeRequestService.approveRequest(id, clientDTO);
            return ResponseEntity.ok(newClient);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

/*
 * TODO: 
 * 
 * 
 * add methods for audit logs, system statistics, system config, etc.
 * as the features are designed and implemented 
 * 
 * 
 * 
 * 
 * 
 * 
 */

}
