package com.ddfinv.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.ddfinv.backend.dto.DTOMapper;
import com.ddfinv.backend.dto.EmployeeDTO;
import com.ddfinv.backend.repository.EmployeeRepository;
import com.ddfinv.core.domain.Employee;
import com.ddfinv.core.domain.UserAccount;
import com.ddfinv.core.domain.enums.Role;
import com.ddfinv.core.repository.UserAccountRepository;

public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserAccountRepository userAccountRepository;
    private final UserAccountService userAccountService;
    private final DTOMapper dtoMapper;


    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, UserAccountRepository userAccountRepository, UserAccountService userAccountService, DTOMapper dtoMapper){
        this.employeeRepository = employeeRepository;
        this.userAccountRepository = userAccountRepository;
        this.userAccountService = userAccountService;
        this.dtoMapper = dtoMapper;
    }

    @Transactional
    public EmployeeDTO createNewEmployee(EmployeeDTO dto){
        UserAccount userAccount =userAccountRepository.findById(dto.getUserAccountId()) 
        .orElseThrow(()-> new RuntimeException("A UserAccount with that ID could not be found."));

        userAccountService.assignUserRole(userAccount, Role.employee);

        Employee newEmployee = new Employee();
        newEmployee.setUserAccount(userAccount);
        newEmployee.setEmployeeId(createNewEmployeeId(newEmployee));
        newEmployee.setLocationId(dto.getLocationId());
        newEmployee.setTitle(dto.getTitle());

        Employee storedEmployee = employeeRepository.save(newEmployee);
        return dtoMapper.toEmployeeDTO(storedEmployee);
    }

    public String createNewEmployeeId(Employee employee){
        return "" + employee.getLocationId() + employee.getId() + "";
    }


    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll()
        .stream().map(dtoMapper::toEmployeeDTO)
        .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeByEmployeeId(String employeeId){
        Employee employee = employeeRepository.findByEmployeeId(employeeId)
        .orElseThrow(()-> new RuntimeException("An Employee with that Employee ID could not be found."));

        return dtoMapper.toEmployeeDTO(employee);
    }

    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(Long id){
        Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("An Employee that ID could not be found."));
        return dtoMapper.toEmployeeDTO(employee);
    }

    @Transactional(readOnly = true)
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO dto){
        Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("An Employee with that ID could not be found."));

        if (dto.getLocationId() != null){
            employee.setLocationId(dto.getLocationId());
        }
        if (dto.getTitle() != null){
            employee.setTitle(dto.getTitle());
        }

        Employee storedEmployee = employeeRepository.save(employee);
        return dtoMapper.toEmployeeDTO(storedEmployee);
    }
}
