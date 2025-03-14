package com.ddfinv.backend.dto;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.ddfinv.backend.service.auth.AuthenticationService;
import com.ddfinv.core.entity.Client;
import com.ddfinv.core.entity.Employee;
import com.ddfinv.core.entity.Permission;
import com.ddfinv.core.entity.UserAccount;

public class DTOMapper {

    private final AuthenticationService authservice;
    @Autowired
    public DTOMapper(AuthenticationService authService){
        this.authservice = authService;
    }

    /**
     * Maps a UserAccount entity to a DT Object
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

    /**
     * Maps a Employee entity to a DT Object
     * 
     * @param entity - the Employee entity to map to a DTO
     * @return EmployeeDTO dto - the newly mapped dto 
     */
    public EmployeeDTO toEmployeeDTO(Employee entity){
        if (entity==null) return null;

        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(entity.getId());
        dto.setEmployeeId(entity.getEmployeeId());
        dto.setUserAccountId(entity.getUserAccount().getId());
        dto.setFirstName(entity.getUserAccount().getFirstName());
        dto.setLastName(entity.getUserAccount().getLastName());
        dto.setEmail(entity.getUserAccount().getEmail());
        dto.setLocationId(entity.getLocationId());
        dto.setTitle(entity.getTitle());

        if(entity.getClientList() != null && !entity.getClientList().isEmpty()){
            dto.setAssignedClientList(
                entity.getClientList().stream()
                .map(Client::getClientId)
                .collect(Collectors.toSet())
            );
        }
        return dto;
    }

    /**
     * Maps a Client entity to a DT Object
     * @param entity - the Client entity to map to DTO
     * @return ClientDTO dto - the newly mapped dto 
     */
    public ClientDTO toClientDTO(Client entity){
        if (entity==null) return null;

        ClientDTO dto = new ClientDTO();
        dto.setId(entity.getId());
        dto.setClientId(entity.getClientId());
        dto.setUserAccountId(entity.getUserAccount().getId());
        dto.setFirstName(entity.getUserAccount().getFirstName());
        dto.setLastName(entity.getUserAccount().getLastName());
        dto.setEmail(entity.getUserAccount().getEmail());

        if(entity.getAssignedEmployee() != null){
            dto.setAssignedEmployeeId(entity.getAssignedEmployee().getEmployeeId());
            // create the concatenated first & last name to simplify dto
            dto.setAssignedEmployeeName(
                entity.getAssignedEmployee().getUserAccount().getFirstName() + 
                " " + entity.getAssignedEmployee().getUserAccount().getLastName()
            );
        }
        return dto;
    }
}
