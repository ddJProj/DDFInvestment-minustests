package com.ddfinv.backend.service;

import com.ddfinv.core.domain.Permission;
import com.ddfinv.core.domain.enums.Permissions;
import com.ddfinv.core.repository.PermissionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class InitializePermissionsService {

    private final PermissionRepository permissionRepository;

    @Autowired
    public InitializePermissionsService(PermissionRepository permissionRepository){
        this.permissionRepository = permissionRepository;
    }

    @PostConstruct
    public void initializePermissions(){
        Set<Permissions> allPermissionsTypes = Permissions.getAllPermissions();

        for (Permissions permissionType : allPermissionsTypes){
            if (!permissionRepository.existsByPermissionType(permissionType)){
                Permission PermissionEntity = new Permission(permissionType, permissionType.getDescription());
                permissionRepository.save(PermissionEntity);
            }
        }
    }
}

//TODO : finish documentation