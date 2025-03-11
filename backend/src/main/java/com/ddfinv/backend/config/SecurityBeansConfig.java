package com.ddfinv.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ddfinv.core.service.EntityPermissionEvaluator;
import com.ddfinv.core.service.PermissionEvaluator;

@Configuration
public class SecurityBeansConfig {

    @Bean
    public PermissionEvaluator permissionEvaluator(){
        return new EntityPermissionEvaluator();
    }
}
