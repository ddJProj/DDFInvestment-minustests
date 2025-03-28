package com.ddfinv.backend.controller.accounts;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddfinv.backend.dto.accounts.UserAccountDTO;
import com.ddfinv.backend.dto.actions.UpgradeRequestDTO;
import com.ddfinv.backend.exception.ResourceNotFoundException;
import com.ddfinv.backend.service.accounts.GuestService;
import com.ddfinv.core.exception.ApplicationException;

import jakarta.websocket.server.PathParam;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/guests")
@CrossOrigin(origins = "http://localhost:5173")
public class GuestController {

    private final GuestService guestService;

    @Autowired
    public GuestController(GuestService guestService){
        this.guestService = guestService;
        
    }

    @PostMapping("/request-client-upgrade")
    public ResponseEntity<?> requestClientUpgrade(@RequestBody UpgradeRequestDTO upgradeDTO) throws ResourceNotFoundException, ApplicationException {
            
        boolean success = guestService.requestClientUpgrade(upgradeDTO);

        return ResponseEntity.ok(success);
    }

    @GetMapping("/upgrade-requests/{userAccountId}")
    public ResponseEntity<?> getUserUpgradeRequests(@PathVariable Long userAccountId) throws ResourceNotFoundException {
        List<UpgradeRequestDTO> requests = guestService.getUserUpgradeRequests(userAccountId);
        return ResponseEntity.ok(requests);
    }
    
        
}
