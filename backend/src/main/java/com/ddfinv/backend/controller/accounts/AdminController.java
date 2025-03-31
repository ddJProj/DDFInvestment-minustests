package com.ddfinv.backend.controller.accounts;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddfinv.backend.service.PermissionHandlerService;
import com.ddfinv.backend.service.accounts.EmployeeService;
import com.ddfinv.backend.service.accounts.UserAccountService;
import com.ddfinv.core.domain.enums.Permissions;
import com.ddfinv.core.domain.enums.Role;
import com.ddfinv.core.repository.UserAccountRepository;


@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserAccountService userAccountService;
    private final EmployeeService employeeService;
    private final PermissionHandlerService permissionHandlerService;
    private final UserAccountRepository userAccountRepository;

    @Autowired
    public AdminController(UserAccountService userAccountService, EmployeeService employeeService, PermissionHandlerService permissionHandlerService, UserAccountRepository userAccountRepository){
        this.userAccountService = userAccountService;
        this.employeeService = employeeService;
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
