package com.ddfinv.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ddfinv.core.entity.UserAccount;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    /**
     * Locate/retrieve a UserAccount by it's email 
     * 
     * @param email
     * @return Optional UserAccount, returned if found
     */
    Optional<UserAccount> findByEmail(String email);

    /**
     * Determines if a UserAccount with matching email exists 
     * 
     * @param email
     * @return boolean true if match found, else false
     */
    boolean existsByEmail(String email);

}
