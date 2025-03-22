package com.ddfinv.backend.controller.accounts;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ddfinv.backend.dto.accounts.UserAccountDTO;
import com.ddfinv.backend.dto.actions.UpdatePasswordDTO;
import com.ddfinv.backend.exception.ResourceNotFoundException;
import com.ddfinv.backend.exception.database.IllegalOperationException;
import com.ddfinv.backend.exception.security.InvalidPasswordException;
import com.ddfinv.backend.service.PermissionHandlerService;
import com.ddfinv.backend.service.accounts.UserAccountService;
import com.ddfinv.core.domain.UserAccount;
import com.ddfinv.core.domain.enums.Permissions;
import com.ddfinv.core.domain.enums.Role;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/users")
public class UserAccountController {

    private final UserAccountService userAccountService;
    private final PermissionHandlerService permissionHandlerService;

    @Autowired
    public UserAccountController (PermissionHandlerService permissionHandlerService, UserAccountService userAccountService){
        this.permissionHandlerService =     permissionHandlerService;
        this.userAccountService = userAccountService;
    }



    /**
     * 
     * @return
     */
    @GetMapping
    public ResponseEntity<?> getAllUserAccounts() {
        if (!permissionHandlerService.currentUserHasPermission(Permissions.VIEW_ACCOUNTS)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


        List<UserAccountDTO> usersAccounts = userAccountService.getAllUserAccounts(null);
        return ResponseEntity.ok(usersAccounts);
    }

    /**
     * 
     * @param role
     * @return
     */
    @GetMapping("/by-role/{role}")
    public ResponseEntity<?> getUsersByRole(@PathVariable Role role) {
        if (permissionHandlerService.getThisUser().getRole() != Role.admin){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<UserAccountDTO> userAccounts = userAccountService.getAllUserAccounts(role);
        return ResponseEntity.ok(userAccounts);
    }

    /**
     * 
     * @param id
     * @return
     * @throws ResourceNotFoundException
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserAccountById(@PathVariable Long id) throws ResourceNotFoundException{
        UserAccountDTO userAccountDTO = userAccountService.findById(id);

        if (permissionHandlerService.getThisUser().getId().equals(id) || permissionHandlerService.currentUserHasPermission(Permissions.VIEW_ACCOUNT)){
            return ResponseEntity.ok(userAccountDTO);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }




    /**
     * 
     * @param userAccountDTO
     * @return
     */
    @PostMapping
    public ResponseEntity<?> createUserAccount(@RequestBody UserAccountDTO userAccountDTO){
        if (!permissionHandlerService.currentUserHasPermission(Permissions.CREATE_USER)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); 
        }
        UserAccountDTO newUserAccount = userAccountService.createUserAcount(userAccountDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUserAccount);
    }


    /**
     *
     * @param userAccountDTO
     * @return
     * @throws ResourceNotFoundException
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserAccount(@PathVariable Long id, @RequestBody UserAccountDTO userAccountDTO) throws ResourceNotFoundException{
        boolean isSelfAccount = permissionHandlerService.getThisUser().getId().equals(id); // checks if user is updating their own account
        
        if (isSelfAccount && permissionHandlerService.currentUserHasPermission(Permissions.EDIT_MY_DETAILS)){
            UserAccountDTO updatedUserAccount = userAccountService.updateUserAccount(id, userAccountDTO);
            return ResponseEntity.ok(updatedUserAccount);
        } else if (permissionHandlerService.currentUserHasPermission(Permissions.EDIT_USER)){
            UserAccountDTO updateUserAccount = userAccountService.updateUserAccount(id, userAccountDTO);
            return ResponseEntity.ok(updateUserAccount);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    /**
     *
     * @param id
     * @return
     * @throws ResourceNotFoundException 
     * @throws IllegalOperationException 
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserAccount(@PathVariable Long id) throws IllegalOperationException, ResourceNotFoundException{
        if (!permissionHandlerService.currentUserHasPermission(Permissions.DELETE_USER)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        userAccountService.deleteUserAccount(id);
        return ResponseEntity.noContent().build();
    }



    /**
     * TODO: Add update-my-password vs update-other-password
     *
     * @param ChangePasswordDTO
     * @param entity
     * @return
     * @throws ResourceNotFoundException
     */
    @PostMapping("/{id}/update-password")
    public ResponseEntity<?> updateUserAccountPassword(@PathVariable Long id, @RequestBody UpdatePasswordDTO updatePasswordDTO) throws ResourceNotFoundException {

        UserAccount thisUserAccount = permissionHandlerService.getThisUser();
        boolean isOwnAccount = thisUserAccount.getId().equals(id); // check to see if changing own password or other account's

        if(isOwnAccount){
            if (!permissionHandlerService.currentUserHasPermission(Permissions.UPDATE_MY_PASSWORD)){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to change that password.");
            }else {
                if (!permissionHandlerService.currentUserHasPermission(Permissions.UPDATE_OTHER_PASSWORD)){
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to change that password.");
                }
            }


        try{
            // if the user is an admin / is attempting to change the password of a different account
            if(!isOwnAccount && permissionHandlerService.currentUserHasPermission(Permissions.UPDATE_OTHER_PASSWORD)){
                userAccountService.resetUserAccountPassword(id, updatePasswordDTO.getNewPassword());
            }else{
                // the user is attempting to change the password of their account
                userAccountService.updateUserAccountPassword(id, updatePasswordDTO);
            }
            return ResponseEntity.ok().body("You successfully updated the password for this account.");
        }catch(InvalidPasswordException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
            return null;

    }

    
    @PutMapping("/{id}/role")
    public ResponseEntity<?> changeUserRole(@PathVariable Long id, @RequestParam Role updatedRole) throws IllegalOperationException, ResourceNotFoundException {
        if (permissionHandlerService.getThisUser().getRole() != Role.admin){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserAccountDTO updatedUserAccount  = userAccountService.changeUserRole(id, updatedRole);
        return ResponseEntity.ok(updatedUserAccount);
    }



}