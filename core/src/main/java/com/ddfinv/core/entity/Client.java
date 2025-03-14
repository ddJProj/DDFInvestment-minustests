package com.ddfinv.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "client")
public class Client {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // id value for business related logic
    @Column(unique = true)
    private String clientId;

    @OneToOne
    @JoinColumn(name = "user_id" ,unique = true)
    private UserAccount userAccount;

    @ManyToOne
    @JoinColumn(name = "assigned_employee_id", unique = true)
    private Employee assignedEmployee;

    // TODO: add any other attributes needed here

    public Client(){
    }

    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id= id;
    }


    
    public String getClientId(){
        return this.clientId;
    }

    public void setClientId(String clientId){
        this.clientId= clientId;
    }


    public UserAccount getUserAccount(){
        return this.userAccount;
    }

    public void setUserAccount(UserAccount userAccount){
        this.userAccount = userAccount;
    }


    public Employee getAssignedEmployee(){
        return this.assignedEmployee;
    }

    public void setAssignedEmployee(Employee assignedEmployee){
        this.assignedEmployee = assignedEmployee;
    }


}
