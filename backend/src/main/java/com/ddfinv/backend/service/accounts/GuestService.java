package com.ddfinv.backend.service.accounts;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddfinv.backend.dto.DTOMapper;
import com.ddfinv.backend.dto.accounts.GuestDTO;
import com.ddfinv.backend.dto.accounts.UserAccountDTO;
import com.ddfinv.backend.dto.actions.UpgradeRequestDTO;
import com.ddfinv.core.domain.GuestUpgradeRequest;
import com.ddfinv.core.domain.UserAccount;
import com.ddfinv.core.domain.enums.Role;
import com.ddfinv.core.domain.enums.UpgradeRequestStatus;
import com.ddfinv.core.exception.ApplicationException;
import com.ddfinv.core.exception.ApplicationException.InputException;
import com.ddfinv.backend.exception.ResourceNotFoundException;
import com.ddfinv.backend.repository.UpgradeRequestRepository;
import com.ddfinv.core.repository.UserAccountRepository;

    //TODO: finish adding guest service related logic
@Service
public class GuestService {
    private final UserAccountRepository userAccountRepository;
    private final UpgradeRequestRepository upgradeRequestRepository;
    private final DTOMapper dtoMapper;


    @Autowired
    public GuestService(UserAccountRepository userAccountRepository, DTOMapper dtoMapper, UpgradeRequestRepository upgradeRequestRepository){
        this.userAccountRepository = userAccountRepository;
        this.upgradeRequestRepository = upgradeRequestRepository;
        this.dtoMapper = dtoMapper;
    }

    @Transactional
    public boolean requestClientUpgrade(UpgradeRequestDTO upgradeDTO) throws ApplicationException, ResourceNotFoundException {
    
        UserAccount userAccount = userAccountRepository.findById(upgradeDTO.getUserAccountId())
        .orElseThrow(() -> new ResourceNotFoundException("User account with the provided ID could not be found: "+ upgradeDTO.getUserAccountId()));

        if (userAccount.getRole() != Role.guest){
            throw new ApplicationException.InputException
            ("Only guest Role accounts may request their role status be upgraded to Client status.");
        }

        // get list of existing guest Upgrade requests
        List<GuestUpgradeRequest> existingRequests = upgradeRequestRepository.findByUserAccountId(userAccount.getId());

        // check if the guest account already has an existing request in the system
        boolean hasExistingRequest = existingRequests.stream().anyMatch(request -> request.getStatus() == UpgradeRequestStatus.PENDING);

        if (hasExistingRequest){
            throw new ApplicationException.InputException("There is already an existing upgrade request for this guest account.");
        }

        GuestUpgradeRequest upgradeRequest = new GuestUpgradeRequest();
        upgradeRequest.setUserAccount(userAccount);
        upgradeRequest.setRequestDate(LocalDateTime.now());
        upgradeRequest.setStatus(UpgradeRequestStatus.PENDING);
        upgradeRequest.setDetails(upgradeDTO.getDetails());

        upgradeRequestRepository.save(upgradeRequest);

        // TODO: add a mechanism to notify admins / employees that a request is available?

        return true;
    }

}
