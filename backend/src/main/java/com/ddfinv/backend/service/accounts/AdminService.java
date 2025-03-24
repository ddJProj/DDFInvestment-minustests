package com.ddfinv.backend.service.accounts;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddfinv.backend.dto.DTOMapper;
import com.ddfinv.backend.repository.ClientRepository;
import com.ddfinv.backend.repository.EmployeeRepository;
import com.ddfinv.core.domain.enums.Role;
import com.ddfinv.core.repository.UserAccountRepository;

@Service
public class AdminService {


// FIXME: ADD METHODS FOR CHANGING ACCOUNT ROLES
    //TODO: finish adding admin service related logic

    private final UserAccountRepository userAccountRepository;
    private final UserAccountService userAccountService;
    private final DTOMapper dtoMapper;
    private final EmployeeRepository employeeRepository;
    private final ClientRepository clientRepository; 

    @Autowired
    public AdminService (UserAccountRepository userAccountRepository, UserAccountService userAccountService, DTOMapper dtoMapper, EmployeeRepository employeeRepository, ClientRepository clientRepository){
        this.userAccountRepository = userAccountRepository;
        this.employeeRepository = employeeRepository;
        this.clientRepository = clientRepository;
        this.userAccountService= userAccountService;
        this.dtoMapper = dtoMapper;
    }

    public Map<String, Object> getSystemInfo(){
        Map<String, Object> sysInfo = new HashMap<>();
        
        sysInfo.put("totalUserAccounts", userAccountRepository.count());
        sysInfo.put("totalAdminAccounts", userAccountRepository.countByRole(Role.admin));
        sysInfo.put("totalEmployeeAccounts", userAccountRepository.countByRole(Role.employee));
        sysInfo.put("totalClientAccounts", userAccountRepository.countByRole(Role.client));
        sysInfo.put("totalGuestAccounts", userAccountRepository.countByRole(Role.guest));

        // for consistency count client and employee repos:

        sysInfo.put("countEmployeeRepo", employeeRepository.count());
        sysInfo.put("countClientRepo", clientRepository.count());

        return sysInfo;

    }

    


}
