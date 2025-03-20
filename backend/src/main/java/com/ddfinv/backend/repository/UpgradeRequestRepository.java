package com.ddfinv.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import com.ddfinv.core.domain.GuestUpgradeRequest;
import com.ddfinv.core.domain.enums.UpgradeRequestStatus;


@Repository
public interface UpgradeRequestRepository extends JpaRepository<GuestUpgradeRequest, Long>{

    List<GuestUpgradeRequest> findByStatus(UpgradeRequestStatus status);

    List<GuestUpgradeRequest> findByUserAccountId(Long userAccountId);
    
}
