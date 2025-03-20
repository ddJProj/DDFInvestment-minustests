package com.ddfinv.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddfinv.backend.dto.UserAccountDTO;
import com.ddfinv.backend.exception.ResourceNotFoundException;
import com.ddfinv.backend.service.PermissionHandlerService;
import com.ddfinv.backend.service.accounts.UserAccountService;
import com.ddfinv.core.domain.UserAccount;
import com.ddfinv.core.domain.enums.Permissions;
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
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserAccount(@PathVariable Long id){
        if (!permissionHandlerService.currentUserHasPermission(Permissions.DELETE_USER)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        userAccountService.deleteUserAccount(id);
        return ResponseEntity.noContent().build();
    }



    /**
     *
     * @param ChangePasswordDTO
     * @param entity
     * @return
     */
    @PutMapping("/update-password")
    public ResponseEntity<?> updateUserAccountPassword(@RequestBody ChangePasswordDTO ChangePasswordDTO, String entity) {
        //TODO: process PUT request
        
        return entity;
    }



}