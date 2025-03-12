package com.ddfinv.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddfinv.backend.service.InvestmentService;
import com.ddfinv.backend.service.PermissionHandlerService;

@RestController
@RequestMapping("api/investments")
public class InvestmentController {

    private final InvestmentService investmentService;
    private final PermissionHandlerService permissionHandlerService;

    @Autowired
    public InvestmentController(InvestmentService investmentService, PermissionHandlerService permissionHandlerService){
        this.investmentService = investmentService;
        this.permissionHandlerService = permissionHandlerService;

    }
}
