package com.ddfinv.backend.service.actions;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ddfinv.backend.dto.DTOMapper;
import com.ddfinv.backend.dto.accounts.ClientDTO;
import com.ddfinv.backend.dto.actions.UpgradeRequestDTO;
import com.ddfinv.backend.exception.ApplicationException;
import com.ddfinv.backend.exception.validation.InputException;

import com.ddfinv.backend.exception.ResourceNotFoundException;
import com.ddfinv.backend.repository.ClientRepository;
import com.ddfinv.backend.repository.UpgradeRequestRepository;
import com.ddfinv.backend.service.accounts.ClientService;
import com.ddfinv.core.domain.Client;
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

    private final ClientRepository clientRepository;

    private final UpgradeRequestRepository upgradeRequestRepository;
    private final UserAccountRepository userAccountRepository;
    private final ClientService clientService;
    private final DTOMapper dtoMapper;

    public UpgradeRequestService(UpgradeRequestRepository upgradeRequestRepository, 
    UserAccountRepository userAccountRepository, ClientService clientService, DTOMapper dtoMapper, ClientRepository clientRepository){
        this.clientService = clientService;
        this.clientRepository = clientRepository;
        this.dtoMapper = dtoMapper;
        this.userAccountRepository  =userAccountRepository;
        this.upgradeRequestRepository = upgradeRequestRepository;
        
    }

    public List<UpgradeRequestDTO> getRequestsByStatus(UpgradeRequestStatus status){
        List<GuestUpgradeRequest> requests = upgradeRequestRepository.findByStatus(status);
        return requests.stream()
        .map(dtoMapper::toUpgradeRequestDTO).collect(Collectors.toList());

    }

    public List<UpgradeRequestDTO> getPendingUpgradeRequests(){
        return getRequestsByStatus(UpgradeRequestStatus.PENDING);
    }


    public List<UpgradeRequestDTO> getUserUpgradeRequestsById(Long userAccountId) throws ResourceNotFoundException {
        UserAccount userAccount = userAccountRepository.findById(userAccountId)
        .orElseThrow(() -> new ResourceNotFoundException("User account with the provided ID could not be found: "+ userAccountId));

        List<GuestUpgradeRequest> requests = upgradeRequestRepository.findByUserAccountId(userAccount.getId());

        return requests.stream().map(dtoMapper::toUpgradeRequestDTO)
        .collect(Collectors.toList());

    }

    @Transactional
    public ClientDTO approveRequest(Long requestId, ClientDTO clientDTO) throws ResourceNotFoundException, InputException{
        GuestUpgradeRequest request = upgradeRequestRepository.findById(requestId)
        .orElseThrow(() -> new ResourceNotFoundException("Upgrade request with that ID not found: " + requestId));

        if (request.getStatus() != UpgradeRequestStatus.PENDING){
            throw new InputException("Can only approve requests that are in the pending state.");
        }

        UserAccount userAccount = request.getUserAccount();;

        Optional<Client> previouslyClient = clientRepository.findByUserAccount(userAccount);
        if (previouslyClient.isPresent()){
            Client client = previouslyClient.get();

            
        }

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
