package com.ddfinv.core.entity;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Collate;
import org.hibernate.annotations.ManyToAny;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

public class Permission {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @ManyToMany(mappedBy = "permissions")
    private Set<UserAccount> userAccounts = new HashSet<>();

    public Permission(){}

    public Permission(String name, String description){
        this.name = name;
        this.description = description;
    }


    public Long getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }
    
    public void setName(String newName){
        this.name = newName;
    }

    public Set<UserAccount> getUserAccounts(){
        return this.userAccounts;
    }

    public void setUserAccounts(Set<UserAccount> userAccounts){
        this.userAccounts = userAccounts;
    }


    // TODO : 
    // finish adding getters and setters  
}
