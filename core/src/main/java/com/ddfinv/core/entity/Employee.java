package com.ddfinv.core.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String employeeId;

    @OneToOne
    @JoinColumn(name = "user_id" ,unique = true)
    private UserAccount userAccount;

    @OneToMany(mappedBy = "assigned_employee", fetch = FetchType.LAZY)
    private Set<Client> clientList = new HashSet<>();
    

    private String locationId;
    private String title;

    public Employee(){
        this.locationId = "USA";
    }

    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getEmployeeId(){
        return this.employeeId;
    }

    public void setEmployeeId(String employeeId){
        this.employeeId= employeeId;
    }
    public UserAccount getUserAccount(){
        return this.userAccount;
    }

    public void setUserAccount(UserAccount userAccount){
        this.userAccount= userAccount;
    }
    public Set<Client> getClientList(){
        return this.clientList;
    }

    public void setClientList(Set<Client> clientList){
        this.clientList = clientList;
    }
    public String getLocationId(){
        return this.locationId;
    }

    public void setLocationId(String locationId){
        this.locationId= locationId;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title= title;
    }

}
