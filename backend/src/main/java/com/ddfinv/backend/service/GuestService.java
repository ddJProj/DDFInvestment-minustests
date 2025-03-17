package com.ddfinv.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.ddfinv.backend.dto.DTOMapper;
import com.ddfinv.backend.dto.GuestDTO;
import com.ddfinv.core.repository.UserAccountRepository;

public class GuestService {
    private final UserAccountService userAccountService;
    private final UserAccountRepository userAccountRepository;
    private final DTOMapper dtoMapper;



    @Autowired
    public GuestService(UserAccountRepository userAccountRepository, UserAccountService userAccountService, DTOMapper dtoMapper){
        this.userAccountRepository = userAccountRepository;
        this.userAccountService = userAccountService;
        this.dtoMapper = dtoMapper;
    }

    @Transactional
    public GuestDTO createNewGuest(GuestDTO dto){
        
        return dto;
        
    }

}
