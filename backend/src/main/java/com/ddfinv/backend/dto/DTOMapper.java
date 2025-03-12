package com.ddfinv.backend.dto;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.ddfinv.backend.service.auth.AuthenticationService;
import com.ddfinv.core.entity.Permission;
import com.ddfinv.core.entity.UserAccount;

public class DTOMapper {

    private final AuthenticationService authservice;
    @Autowired
    public DTOMapper(AuthenticationService authService){
        this.authservice = authService;
    }

    /**
     * 
     * @param entity - the UserAccount entity to map to DTO
     * @return UserAccountDTO dto - the newly mapped dto 
     */
    public UserAccountDTO toDTO(UserAccount entity){
        if (entity == null){
            return null;
        }
        UserAccountDTO dto = new UserAccountDTO();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setRole(entity.getRole());

        if (entity.getPermissions()!= null){
            // retrieve just the string name of each permission for use in the DTO. (dont need full entity)
            // create a new set of just the permission names
            dto.setPermissionName(entity.getPermissions().stream().map(Permission::getName).collect(Collectors.toSet()));
        }

        return dto;
    }

    /**
     * 
     * @param dto - object to be mapped to a UserAccount entity
     * @return UserAccount entity - the newly mapped UserAccount instance
     */
    public UserAccount toEntity(UserAccountDTO dto){
        if (dto == null){
            return null;
        }

        UserAccount entity = new UserAccount();

        return entity;
    }

}
