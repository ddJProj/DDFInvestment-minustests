package com.ddfinv.core.domain;

import java.time.LocalDateTime;

import org.apache.tomcat.util.http.parser.Upgrade;
import org.springframework.cglib.core.Local;

import com.ddfinv.core.domain.enums.UpgradeRequestStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class GuestUpgradeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;

    private LocalDateTime requestDate;

    @Enumerated(EnumType.STRING)
    private UpgradeRequestStatus status;

    @Column(length = 1000)
    private String details;


    // TODO: add getters/setters

    public Long getId(){
        return this.id;

    }

    public void set(Long id){
        this.id = id;
    }

    public UserAccount getUserAccount(){
        return this.userAccount;
    }

    public void setUserAccount(UserAccount userAccount){
        this.userAccount = userAccount;
    }
    public LocalDateTime getRequestDate(){
        return this.requestDate;

    }


    public void setRequestDate(LocalDateTime requestDate){
        this.requestDate = requestDate;
    }

    public UpgradeRequestStatus getStatus(){
        return this.status;
    }

    public void setStatus(UpgradeRequestStatus status){
        this.status = status;

    }

    public String getDetails(){
        return this.details;

    }

    public void setDetails(String details){
        this.details = details;

    }
}
