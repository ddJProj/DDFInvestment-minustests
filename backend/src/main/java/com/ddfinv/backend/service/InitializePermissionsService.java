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
            new Permission("VIEW_ACCOUNT", "View the details of an account."),
            new Permission("EDIT_ACCOUNT", "Edit the details of an account."),
            new Permission("VIEW_CLIENT", "View the details of a specific client."),
            new Permission("EDIT_CLIENT", "Edit the details of a specific client."),
            new Permission("VIEW_INVESTMENT", "View the details of a speecific investment."),
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