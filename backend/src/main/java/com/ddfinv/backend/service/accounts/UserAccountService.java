package com.ddfinv.backend.service.accounts;

import com.ddfinv.core.domain.Permission;
import com.ddfinv.core.domain.UserAccount;
import com.ddfinv.core.domain.enums.Role;
import com.ddfinv.core.exception.ApplicationException;
import com.ddfinv.core.repository.PermissionRepository;
import com.ddfinv.core.repository.UserAccountRepository;
import com.ddfinv.core.service.RolePermissionService;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.config.oauth2.client.ClientRegistrationsBeanDefinitionParser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ddfinv.backend.dto.DTOMapper;
import com.ddfinv.backend.dto.accounts.UserAccountDTO;
import com.ddfinv.backend.dto.actions.UpdatePasswordDTO;
import com.ddfinv.backend.exception.ResourceNotFoundException;
import com.ddfinv.backend.exception.database.IllegalOperationException;
import com.ddfinv.backend.exception.security.InvalidPasswordException;
import com.ddfinv.backend.repository.ClientRepository;
import com.ddfinv.backend.repository.EmployeeRepository;
import com.ddfinv.backend.service.auth.AuthenticationService;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionService rolePermissionService;
    private final AuthenticationService authService;
    private final DTOMapper dtoMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final ClientRepository clientRepository; 

    @Autowired
    public UserAccountService (UserAccountRepository userAccountRepository, PermissionRepository permissionRepository, AuthenticationService authService, DTOMapper dtoMapper, PasswordEncoder passwordEncoder, EmployeeRepository employeeRepository, ClientRepository clientRepository){
        this.userAccountRepository = userAccountRepository;
        this.employeeRepository = employeeRepository;
        this.clientRepository = clientRepository;
        this.permissionRepository = permissionRepository;
        this.authService = authService;
        this.dtoMapper = dtoMapper;
        this.passwordEncoder = passwordEncoder;
        this.rolePermissionService = new RolePermissionService();
    }

    @Transactional
    public UserAccountDTO createUserAcount(UserAccountDTO dto){
        UserAccount user = dtoMapper.toEntity(dto);
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()){
            user.setHashedPassword(passwordEncoder.encode(dto.getPassword())); 
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
     * @throws ResourceNotFoundException 
     */
    public UserAccountDTO findById(Long id) throws ResourceNotFoundException{
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
     * @throws ResourceNotFoundException 
     */
    @Transactional
    public UserAccountDTO updateUserAccount(Long id, UserAccountDTO dto) throws ResourceNotFoundException{
        UserAccount storedUserAccount = userAccountRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("UserAccount with this ID value was not found: " + id));

        if (dto.getEmail() != null) storedUserAccount.setEmail(dto.getEmail());
        if (dto.getFirstName() != null) storedUserAccount.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) storedUserAccount.setLastName(dto.getLastName());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            storedUserAccount.setHashedPassword(passwordEncoder.encode(dto.getPassword()));
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


    @Transactional
    public void updateUserAccountPassword(Long userId, UpdatePasswordDTO updatePasswordDTO) throws ResourceNotFoundException, InvalidPasswordException{
        UserAccount userAccount = userAccountRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("A User account with that ID was not found: " + userId));

        if (!passwordEncoder.matches(updatePasswordDTO.getCurrentPassword(), userAccount.getHashedPassword())){
            throw new InvalidPasswordException("The password entered did not match the stored value.");
        }
        if (!updatePasswordDTO.getNewPassword().equals(updatePasswordDTO.getPasswordConfirmation())){
            throw new InvalidPasswordException("The new password and confirmation password did not match.");
        }
        userAccount.setHashedPassword(passwordEncoder.encode(updatePasswordDTO.getNewPassword()));
        userAccountRepository.save(userAccount);

    }

    @Transactional
    public void resetUserAccountPassword(Long userId, String newPassword) throws ResourceNotFoundException, InvalidPasswordException{
        UserAccount userAccount = userAccountRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("A User account with that ID was not found: " + userId));

        
        try{
            authService.validatePassword(newPassword);
        }catch(InvalidPasswordException e){
            throw e;
        }

        userAccount.setHashedPassword(passwordEncoder.encode(newPassword));
        userAccountRepository.save(userAccount);

        // ADD Some form of logging? store with db entry
        // LocalDateTime dateTime = LocalDateTime.now();
        // log.info("The password for {} was reset on : {}, by the UserAccount: {}", userId, dateTime, userAccount.getId());
    }





//TODO: FINISH IMPLEMENTING NEW FEATURES BELOW:
    @Transactional
    public void deleteUserAccount(Long id) throws ResourceNotFoundException, IllegalOperationException {
        UserAccount userAccount = userAccountRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User with the provided id value could not be found: " + id));

        if (userAccount.getRole() == Role.admin && userAccountRepository.numOfRoles(Role.admin) <= 1){
            throw new IllegalOperationException("You cannot remove the only admin UserAccount from the system.");
        }

        // reset the account's permissions
        userAccount.setPermissions(new HashSet<>());

        if (userAccount.getEmployee() != null){
            employeeRepository.delete(userAccount.getEmployee());
        }

        if (userAccount.getClient() != null){
            clientRepository.delete(userAccount.getClient());
        }

        userAccountRepository.delete(userAccount);

        // ADD Some form of logging?
        // log.info("The UserAccount: {}, email: {}, Role: {} was successfully deleted from the system.", userId, userAccount.getEmail(), userAccount.getRole());

    }

    @Transactional
    public UserAccountDTO changeUserRole(Long id, Role updatedRole) throws ResourceNotFoundException, IllegalOperationException {
        // TODO Auto-generated method stub
        UserAccount userAccount = userAccountRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User with the provided id value could not be found: " + id));

        if (userAccount.getRole() == Role.admin && updatedRole != Role.admin && userAccountRepository.countByRole(Role.admin) <= 1){

            throw new IllegalOperationException("You cannot remove the only admin UserAccount from the system.");
        }
        assignUserRole(userAccount, updatedRole);
    
        UserAccount updatedUserAccount = userAccountRepository.save(userAccount);
        
        return dtoMapper.toDTO(updatedUserAccount);
    }

}

//TODO : finish documentation