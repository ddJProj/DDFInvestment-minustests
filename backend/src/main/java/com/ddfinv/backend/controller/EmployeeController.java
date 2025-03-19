package com.ddfinv.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddfinv.backend.dto.EmployeeDTO;
import com.ddfinv.backend.service.EmployeeService;
import com.ddfinv.backend.service.PermissionHandlerService;
import com.ddfinv.core.domain.enums.Permissions;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final PermissionHandlerService permissionHandlerService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, PermissionHandlerService permissionHandlerService){
        this.employeeService = employeeService;
        this.permissionHandlerService = permissionHandlerService;
    }

    /**
     * 
     *
     * @param employeeDTO
     * @return
     */
    @PostMapping
    public ResponseEntity<?> createNewEmployeEntity(@RequestBody EmployeeDTO employeeDTO){
        if (!permissionHandlerService.currentUserHasPermission(Permissions.CREATE_EMPLOYEE)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        EmployeeDTO newEmployee = employeeService.createNewEmployee(employeeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newEmployee);
    }

    /**
     * 
     * @return
     */
    @GetMapping
    public ResponseEntity<?> getAllEmployees(){
        if(!permissionHandlerService.currentUserHasPermission(Permissions.VIEW_EMPLOYEES)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<EmployeeDTO> employeeList = employeeService.getAllEmployees();
        return ResponseEntity.ok(employeeList);
    }

    /**
     * 
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id){
        if (!permissionHandlerService.currentUserHasPermission(Permissions.VIEW_EMPLOYEE)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        EmployeeDTO retrievedEmployee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(retrievedEmployee);
    }

    /**
     * 
     * @param employeeId
     * @return
     */
    @GetMapping("/employee-id/{employeeId}")
    public ResponseEntity<?> getEmployeeByEmployeeId(@PathVariable String employeeId){
        if (!permissionHandlerService.currentUserHasPermission(Permissions.VIEW_EMPLOYEE)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        EmployeeDTO retrievedEmployee = employeeService.getEmployeeByEmployeeId(employeeId);
        return ResponseEntity.ok(retrievedEmployee);
    }

    /**
     * 
     * @param id
     * @param employeeDTO
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployeEntity(@PathVariable Long id, @RequestBody EmployeeDTO employeeDTO){
        if (!permissionHandlerService.currentUserHasPermission(Permissions.EDIT_EMPLOYEE)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        EmployeeDTO updatedEmployee = employeeService.updateEmployee(id, employeeDTO);
        return ResponseEntity.ok(updatedEmployee);
    }

}
