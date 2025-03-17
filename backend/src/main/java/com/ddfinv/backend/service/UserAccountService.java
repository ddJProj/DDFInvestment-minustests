package com.ddfinv.backend.service;

import com.ddfinv.core.domain.Permission;
import com.ddfinv.core.domain.UserAccount;
import com.ddfinv.core.domain.enums.Role;
import com.ddfinv.core.repository.PermissionRepository;
import com.ddfinv.core.repository.UserAccountRepository;
import com.ddfinv.core.service.RolePermissionService;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;

import com.ddfinv.backend.dto.DTOMapper;
import com.ddfinv.backend.dto.UserAccountDTO;
import com.ddfinv.backend.exception.ResourceNotFoundException;
import com.ddfinv.backend.service.auth.AuthenticationService;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionService rolePermissionService;
    private final AuthenticationService authService;
    private final DTOMapper dtoMapper;

    @Autowired
    public UserAccountService (UserAccountRepository userAccountRepository, PermissionRepository permissionRepository, AuthenticationService authService, DTOMapper dtoMapper){
        this.userAccountRepository = userAccountRepository;
        this.permissionRepository = permissionRepository;
        this.authService = authService;
        this.dtoMapper = dtoMapper;
        this.rolePermissionService = new RolePermissionService();
    }

    @Transactional
    public UserAccountDTO createUserAcount(UserAccountDTO dto){
        UserAccount user = dtoMapper.toEntity(dto);
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()){
            user.setHashedPassword(authService.hashPassword(dto.getPassword()));
        }
        UserAccount storedAccount = userAccountRepository.save(user);

        // tern to ensure defaul role/permissions are assigned to new user
        assignUserRole(storedAccount, dto.getRole() != null ? dto.getRole() : Role.guest);

        storedAccount = userAccountRepository.save(storedAccount);

        return dtoMapper.toDTO(storedAccount);
    }




    /**
     * old version pre DTO implementation 
     * @param email
     * @param password
     * @param firstName
     * @param lastName
     * @return
     */
    @Transactional
    public UserAccount createNewUserAccount(String email, String password, String firstName, String lastName){
        UserAccountDTO dto = new UserAccountDTO();

        dto.setEmail(email);
        dto.setPassword(password);
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setRole(Role.guest);

        return dtoMapper.toEntity(createUserAcount(dto));
    }

    /**
     * 
     * @param userAccount
     * @param role
     */
    @Transactional
    public void assignUserRole(UserAccount userAccount, Role role){
        userAccount.setRole(role);

        // get the full possible list of permissions
        Set<Permission> completePermissions = Set.copyOf(permissionRepository.findAll());
        
        // get the default set of permissions for the specific role
        Set<Permission> basePermisions = rolePermissionService.getBasePermissionForRole(role, completePermissions);

        // set the list of permissions for the account to base for it's role
        userAccount.setPermissions(basePermisions);
    }

    /**
     * 
     * @param userAccount
     * @param role
     * @return
     */
    @Transactional
    public UserAccount modifyUserAccountRole(UserAccount userAccount, Role role){
        assignUserRole(userAccount, role);
        return userAccount;
    }

    /**
     * 
     * @param email
     * @return
     */
    public UserAccountDTO findById(Long id){
        return userAccountRepository.findById(id).map(dtoMapper::toDTO).orElseThrow(
            () -> new ResourceNotFoundException("A UserAccount with this ID was not found: " + id));
        
    }


    /**
     * 
     * @param email
     * @return
     */
    public UserAccount findByEmail(String email){
        return userAccountRepository.findByEmail(email)
            .orElse(null);
    }

    /**
     * 
     * @param userAccount
     * @return
     */
    public UserAccount saveUserAccount(UserAccount userAccount){
        return userAccountRepository.save(userAccount);
    }

    /**
     * Method for updating an existing UserAccount entity
     * 
     * @param id
     * @param dto
     * @return UserAccountDTO - newly mapped UserAccount entity to DTO
     */
    @Transactional
    public UserAccountDTO updateUserAccount(Long id, UserAccountDTO dto){
        UserAccount storedUserAccount = userAccountRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("UserAccount with this ID value was not found: " + id));

        if (dto.getEmail() != null) storedUserAccount.setEmail(dto.getEmail());
        if (dto.getFirstName() != null) storedUserAccount.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) storedUserAccount.setLastName(dto.getLastName());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            storedUserAccount.setHashedPassword(authService.hashPassword(dto.getPassword()));
        }
        if (dto.getRole() != null && storedUserAccount.getRole() != dto.getRole()){
            assignUserRole(storedUserAccount, dto.getRole());
        }

        UserAccount updatedUserAccount = userAccountRepository.save(storedUserAccount);
        return dtoMapper.toDTO(updatedUserAccount);

    }

    public List<UserAccountDTO> getAllUserAccounts(Role targetRole){
        List<UserAccount> userList;

        if (targetRole != null){
            userList = userAccountRepository.findByRole(targetRole);
        }else{
            userList = userAccountRepository.findAll();
        }

        return userList.stream().map(dtoMapper::toDTO).collect(Collectors.toList());
    }
}
//TODO : finish documentation