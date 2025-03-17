package com.ddfinv.backend.service;

import com.ddfinv.core.repository.PermissionRepository;
import com.ddfinv.core.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Service
public class InitializePermissionsService {

    private final PermissionRepository permissionRepository;

    @Autowired
    public InitializePermissionsService(PermissionRepository permissionRepository){
        this.permissionRepository = permissionRepository;
    }

    @PostConstruct
    public void initializePermissions(){

        List<Permission> basePermissions = Arrays.asList(

            new Permission("CREATE_EMPLOYEE", "Create Employee account types."),
            new Permission("VIEW_EMPLOYEE", "View an Employee account's details."),
            new Permission("VIEW_EMPLOYEES", "View the details of all Employee accounts"),
            new Permission("EDIT_EMPLOYEE", "Edit the details of an Employee account."),
            new Permission("VIEW_ACCOUNT", "View the details of an account."),
            new Permission("EDIT_ACCOUNT", "Edit the details of an account."),
            new Permission("CREATE_CLIENT", "Create Client account types."),
            new Permission("VIEW_CLIENT", "View the details of a specific Client."),
            new Permission("VIEW_CLIENTS", "View the details of all Client accounts."),
            new Permission("EDIT_CLIENT", "Edit the details of a specific Client."),
            new Permission("ASSIGN_CLIENT", "Assign a client to an Employee."),
            new Permission("VIEW_INVESTMENT", "View the details of a speecific investment."),
            new Permission("EDIT_INVESTMENT", "Edit the details of a speecific investment."),
            new Permission("NEW_INVESTMENT", "Define the details of a new investment."),
            new Permission("VIEW_PUBLIC_INFO", "View publically accessible information.")
        );

        for (Permission permission : basePermissions){
            if (!permissionRepository.existsByName(permission.getName())){
                permissionRepository.save(permission);
            }
        }
    }
}

//TODO : finish documentation