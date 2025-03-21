package com.ddfinv.backend.controller.accounts;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddfinv.backend.dto.accounts.EmployeeDTO;
import com.ddfinv.backend.dto.accounts.UserAccountDTO;
import com.ddfinv.backend.service.PermissionHandlerService;
import com.ddfinv.backend.service.accounts.EmployeeService;
import com.ddfinv.backend.service.accounts.UserAccountService;
import com.ddfinv.core.domain.enums.Permissions;
import com.ddfinv.core.domain.enums.Role;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserAccountService userAccountService;
    private final EmployeeService employeeService;
    private final PermissionHandlerService permissionHandlerService;

    @Autowired
    public AdminController(UserAccountService userAccountService, EmployeeService employeeService, PermissionHandlerService permissionHandlerService){
        this.userAccountService = userAccountService;
        this.employeeService = employeeService;
        this.permissionHandlerService = permissionHandlerService;
    }

    /**
     * 
     * @param role
     * @return
     */
    @GetMapping("/users/by-role/{role}")
    public ResponseEntity<?> getUsersByRolee(@PathVariable Role role) {
        if (permissionHandlerService.getThisUser().getRole() != Role.admin){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<UserAccountDTO> userAccounts = userAccountService.getAllUserAccounts(role);
        return ResponseEntity.ok(userAccounts);
    }
    
    @PutMapping("/users/{id}/{role}")
    public ResponseEntity<?> changeUserRole(@PathVariable Long id, @RequestParam Role updatedRole) {
        if (permissionHandlerService.getThisUser().getRole() != Role.admin){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserAccountDTO updatedUserAccount  = userAccountService.changeUserRole(id, updatedRole);
        return ResponseEntity.ok(updatedUserAccount);
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
