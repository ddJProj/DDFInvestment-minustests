package com.ddfinv.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ddfinv.core.domain.UserAccount;
import com.ddfinv.core.domain.enums.Role;

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
    
    /**
     * Locate/retrieve all UserAccounts by the provided role
     *
     * @param Role targetRole
     * @return Optional UserAccount, returned if found
     */
    List<UserAccount> findByRole(Role targetRole);
    
    /**
     * Determines if a UserAccount with matching role exists
     *
     * @param Role targetRole
     * @return boolean true if match found, else false
     */
    boolean existsByRole(Role targetRole);

    int numOfRoles(Role role);

    int countByRole(Role role);
}
