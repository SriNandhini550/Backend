package com.dxc.controller;

import com.dxc.dto.JwtAuthenticationResponse;
import com.dxc.dto.RefreshTokenRequest;
import com.dxc.dto.SignUpRequest;
import com.dxc.dto.SigninRequest;
import com.dxc.exception.InvalidCredentialsException;
import com.dxc.exception.UserExistsException;
import com.dxc.exception.UserNotFoundException;
import com.dxc.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins="http://localhost:4200")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest signUpRequest) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.signup(signUpRequest));
        } catch (UserExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninRequest signinRequest) {
        try {
            JwtAuthenticationResponse response = authenticationService.signin(signinRequest);
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
    


//    @PostMapping("/refresh")
//    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
//        try {
//            JwtAuthenticationResponse response = authenticationService.refreshToken(refreshTokenRequest);
//            return ResponseEntity.ok(response);
//        } catch (UserNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
//        }
//    }
}
