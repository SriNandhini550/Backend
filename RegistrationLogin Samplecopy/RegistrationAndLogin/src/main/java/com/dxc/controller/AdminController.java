package com.dxc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins="http://localhost:4200")
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
	
@GetMapping
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public ResponseEntity<String> sayHello(){
	return ResponseEntity.ok("Hi Admin");
}

}
