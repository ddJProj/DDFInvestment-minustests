package com.ddfinv.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ddfinv.core.entity.Client;
import com.ddfinv.core.entity.Employee;
import com.ddfinv.core.entity.UserAccount;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{
    
    Optional<Client> findByClientId(String clientId);
    Optional<Client> findByUserAccount(UserAccount userAccount);

    Optional<Client> findByAssignedEmployee(Employee employee);

    boolean existsByEmployeeId(String employeeId);


}
