package com.ddfinv.backend.service.accounts;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.ddfinv.backend.dto.DTOMapper;
import com.ddfinv.backend.dto.accounts.ClientDTO;
import com.ddfinv.backend.dto.actions.AssignClientDTO;
import com.ddfinv.backend.repository.ClientRepository;
import com.ddfinv.backend.repository.EmployeeRepository;
import com.ddfinv.core.domain.Client;
import com.ddfinv.core.domain.Employee;
import com.ddfinv.core.domain.UserAccount;
import com.ddfinv.core.domain.enums.Role;
import com.ddfinv.core.repository.UserAccountRepository;

// import jakarta.transaction.Transactional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final UserAccountRepository userAccountRepository;
    private final UserAccountService userAccountService;
    private final DTOMapper dtoMapper;


    @Autowired
    public ClientService(ClientRepository clientRepository, EmployeeRepository employeeRepository, UserAccountRepository userAccountRepository, UserAccountService userAccountService, DTOMapper dtoMapper){
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
        this.userAccountRepository = userAccountRepository;
        this.userAccountService = userAccountService;
        this.dtoMapper = dtoMapper;
    }

    @Transactional
    public ClientDTO createClient(ClientDTO dto){
        UserAccount userAccount = userAccountRepository.findById(dto.getUserAccountId()).orElseThrow(() -> new RuntimeException("A UserAccount with that ID was not found."));

        userAccountService.assignUserRole(userAccount, Role.client);
        
        Employee assignedEmployee = null;
        if (dto.getAssignedEmployeeId() != null) {
            assignedEmployee = employeeRepository.findByEmployeeId(dto.getAssignedEmployeeId()).orElseThrow(() -> new RuntimeException("An assigned employee with that ID was not found."));
        } else {
            // else set the default employee to the first employee in the repository 
            assignedEmployee = employeeRepository.findById(1L)
            .orElse(employeeRepository.findAll().stream().findFirst().orElse(null));
        }

        Client newClient = new Client();
        newClient.setUserAccount(userAccount);
        newClient.setAssignedEmployee(assignedEmployee);

        Client saveClient = clientRepository.save(newClient);
        saveClient.setClientId(createNewClientId(saveClient)); // FIXME: add better method to create new clientID ie. add location tag to the Client.id value

        Client storedClient = clientRepository.save(saveClient);
        return dtoMapper.toClientDTO(storedClient);

    }



    public String createNewClientId(Client client){
        String locationId = "HOMEBASE";

        if (client.getAssignedEmployee() != null){
            locationId = "" + client.getAssignedEmployee().getLocationId() + "";
        }

        return locationId + "-" + client.getId() + "";
    }

    @Transactional
    public ClientDTO assignClientToEmployee(AssignClientDTO assignmentDTO){
        Client client = clientRepository.findByClientId(
            assignmentDTO.getClientId()).orElseThrow(() -> new RuntimeException("A Client with that ID could not be found."));
    
        Employee employee = employeeRepository.findByEmployeeId(
            assignmentDTO.getEmployeeId()).orElseThrow(() -> new RuntimeException("An Employee with that ID could not be found."));
            
        client.setAssignedEmployee(employee);
        Client storedClient = clientRepository.save(client);
        return dtoMapper.toClientDTO(storedClient);
    }

    @Transactional(readOnly = true)
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll()
        .stream().map(dtoMapper::toClientDTO)
        .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClientDTO> getClientsByEmployeeId(String employeeId){
        Employee employee = employeeRepository.findByEmployeeId(employeeId)
        .orElseThrow(()-> new RuntimeException("An employee with that ID could not be found."));

        return clientRepository.findByAssignedEmployee(employee).stream().map(dtoMapper::toClientDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClientDTO getClientById(Long id){
        Client client = clientRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("A Client with that ID could not be found."));
        return dtoMapper.toClientDTO(client);
    }

    @Transactional(readOnly = true)
    public ClientDTO getClientByClientId(String clientId){
        Client client = clientRepository.findByClientId(clientId)
        .orElseThrow(() -> new RuntimeException("A Client with that Client ID could not be found."));
        return dtoMapper.toClientDTO(client);
    }


    @Transactional(readOnly = true)
    public ClientDTO updateClient(Long id, ClientDTO dto){
        Client client = clientRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("A Client with that ID could not be found."));

        if (dto.getAssignedEmployeeId() != null){
            Employee assignedEmployee = employeeRepository.findByEmployeeId(dto.getAssignedEmployeeId())
            .orElseThrow(() -> new RuntimeException("An Representative with that Employee ID could not be found."));
            client.setAssignedEmployee(assignedEmployee);
        }

        Client storedClient = clientRepository.save(client);
        return dtoMapper.toClientDTO(storedClient);
    }

}
