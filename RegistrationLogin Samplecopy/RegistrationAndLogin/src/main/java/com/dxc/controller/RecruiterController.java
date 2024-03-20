package com.dxc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins="http://localhost:4200")
@RequestMapping("/api/v1/recruiter")
public class RecruiterController {

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_RECRUITER')")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hi Recruiter");
    }
}
