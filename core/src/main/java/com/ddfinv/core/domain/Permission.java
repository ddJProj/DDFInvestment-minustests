package com.ddfinv.core.domain;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Collate;
import org.hibernate.annotations.ManyToAny;

import com.ddfinv.core.domain.enums.Permissions;
import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "permission")
public class Permission {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private Permissions permissionType;

    private String description;

    @ManyToMany(mappedBy = "permissions")
    private Set<UserAccount> userAccounts = new HashSet<>();

    public Permission(){}

    public Permission(Permissions permissionType, String description){
        this.permissionType = permissionType;
        this.description = description;
    }


    public Long getId(){
        return this.id;
    }

    public Permissions getPermissionType(){
        return this.permissionType;
    }
    
    public void setPermissionType(Permissions permissionType){
        this.permissionType = permissionType;
    }

    public Set<UserAccount> getUserAccounts(){
        return this.userAccounts;
    }

    public void setUserAccounts(Set<UserAccount> userAccounts){
        this.userAccounts = userAccounts;
    }

    public String getDescription(){
        return this.description;
    }
    
    public void setPermissionType(String description){
        this.description = description;
    }



    // TODO : 
    // finish adding getters and setters  
}
