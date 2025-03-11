package com.ddfinv.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddfinv.backend.service.InvestmentService;
import com.ddfinv.backend.service.PermissionHandlerService;

@RestController
@RequestMapping("api/investments")
public class InvestmentController {

    private final InvestmentService investmentService;
    private final PermissionHandlerService permissionHandlerService;

    
}
