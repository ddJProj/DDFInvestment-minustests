package com.ddfinv.core.entity;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Collate;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Permission {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    private Set<UserAccount> userAccounts = new HashSet<>();

    public Permission(){}

    public Permission(String name, String description){
        this.name = name;
        this.description = description;
    }


    // TODO : 
    // finish adding getters and setters  
}
