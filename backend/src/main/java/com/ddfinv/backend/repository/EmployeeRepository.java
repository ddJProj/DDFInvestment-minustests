package com.ddfinv.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ddfinv.core.entity.Employee;
import com.ddfinv.core.entity.UserAccount;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{
    
    Optional<Employee> findByEmployeeId(String employeeId);
    Optional<Employee> findByUserAccount(UserAccount userAccount);

    boolean existsByEmployeeId(String employeeId);


}
