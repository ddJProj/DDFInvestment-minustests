package com.ddfinv.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ddfinv.backend.dto.ClientDTO;
import com.ddfinv.backend.dto.DTOMapper;
import com.ddfinv.backend.dto.UpgradeRequestDTO;
import com.ddfinv.backend.exception.ApplicationException;
import com.ddfinv.backend.exception.validation.InputException;

import com.ddfinv.backend.exception.ResourceNotFoundException;
import com.ddfinv.backend.repository.UpgradeRequestRepository;
import com.ddfinv.backend.service.accounts.ClientService;
import com.ddfinv.core.domain.GuestUpgradeRequest;
import com.ddfinv.core.domain.UserAccount;
import com.ddfinv.core.domain.enums.UpgradeRequestStatus;
import com.ddfinv.core.repository.UserAccountRepository;

import jakarta.transaction.Transactional;

/**
 * This class is used by system users with elevated permissions to grant or reject
 * account role upgrade requests.
 */
@Service
public class UpgradeRequestService {

    private final UpgradeRequestRepository upgradeRequestRepository;
    private final UserAccountRepository userAccountRepository;
    private final ClientService clientService;
    private final DTOMapper dtoMapper;

    public UpgradeRequestService(UpgradeRequestRepository upgradeRequestRepository, 
    UserAccountRepository userAccountRepository, ClientService clientService, DTOMapper dtoMapper){
        this.clientService = clientService;
        this.dtoMapper = dtoMapper;
        this.userAccountRepository  =userAccountRepository;
        this.upgradeRequestRepository = upgradeRequestRepository;
        
    }

    public List<UpgradeRequestDTO> getRequestsByStatus(UpgradeRequestStatus status){
        List<GuestUpgradeRequest> requests = upgradeRequestRepository.findByStatus(status);
        return requests.stream()
        .map(dtoMapper::toUpgradeRequestDTO).collect(Collectors.toList());

    }

    public List<UpgradeRequestDTO> getExistinUpgradeRequests(){
        return getRequestsByStatus(UpgradeRequestStatus.PENDING);
    }

    @Transactional
    public ClientDTO approveRequest(Long requestId, ClientDTO clientDTO) throws ResourceNotFoundException, InputException{
        GuestUpgradeRequest request = upgradeRequestRepository.findById(requestId)
        .orElseThrow(() -> new ResourceNotFoundException("Upgrade request with that ID not found: " + requestId));

        if (request.getStatus() != UpgradeRequestStatus.PENDING){
            throw new InputException("Can only approve requests that are in the pending state.");
        }

        UserAccount userAccount = request.getUserAccount();;

        clientDTO.setUserAccountId(userAccount.getId());

        ClientDTO newClient = clientService.createClient(clientDTO);

        request.setStatus(UpgradeRequestStatus.APPROVED);
        upgradeRequestRepository.save(request);
        
        return newClient;
    }



    @Transactional
    public boolean rejectRequest(Long requestId, String rejectionReason) throws ResourceNotFoundException, InputException{
        GuestUpgradeRequest request = upgradeRequestRepository.findById(requestId)
        .orElseThrow(() -> new ResourceNotFoundException("Upgrade request with that ID not found: " + requestId));

        if (request.getStatus() != UpgradeRequestStatus.PENDING){
            throw new InputException("Can only reject requests that are in the pending state.");
        }

 
        request.setStatus(UpgradeRequestStatus.REJECTED);
        request.setDetails("Reason for Rejection: " + rejectionReason);

        upgradeRequestRepository.save(request);
        
        return true;
    }

}
