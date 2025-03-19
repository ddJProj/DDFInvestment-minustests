package com.ddfinv.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ddfinv.core.domain.Permission;
import com.ddfinv.core.domain.enums.Permissions;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>{

    /**
     * Locate/retrieve a permission by it's name
     * 
     * @param name
     * @return Optional permission, returned if found
     */
    Optional<Permission> findByPermissionType(Permissions permissionType);

    /**
     * Determines if a permission already exists 
     * 
     * @param name
     * @return boolean true if match found, else false
     */
    boolean existsByPermissionType(Permissions permissionType);

}
