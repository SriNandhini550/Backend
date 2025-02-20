package com.dxc.controller;

import com.dxc.dto.ForgotPasswordDto;
import com.dxc.dto.JwtAuthenticationResponse;
import com.dxc.dto.PasswordResetDto;
import com.dxc.dto.SignUpRequest;
import com.dxc.dto.SigninRequest;
import com.dxc.exception.UserNotFoundException;
import com.dxc.model.User;
import com.dxc.service.AuthenticationService;
import com.dxc.service.EmailService;
import com.dxc.service.JWTServiceImpl;
import com.dxc.service.UserService;
import com.dxc.validator.ForgotPasswordDtoValidator;
import com.dxc.validator.PasswordResetDtoValidator;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.mail.SimpleMailMessage;
import java.util.Locale;
import java.util.UUID;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationController {

	
    private final AuthenticationService authenticationService;

    @Autowired
    private JWTServiceImpl jwtServiceImpl;
    
    @Autowired
    private JavaMailSender javaMailSender; 

    @Autowired
    private ForgotPasswordDtoValidator forgotPasswordDtoValidator;

    @Autowired
    private PasswordResetDtoValidator passwordResetDtoValidator;
    
    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
  

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(authenticationService.signup(signUpRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninRequest signinRequest) {
        JwtAuthenticationResponse response = authenticationService.signin(signinRequest);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordDto forgotPasswordDto,
                                                 BindingResult bindingResult) {
        forgotPasswordDtoValidator.validate(forgotPasswordDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid input data");
        }

        User user = UserService.findUserByEmail(forgotPasswordDto.getEmail());
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        String token = UUID.randomUUID().toString();
        UserService.createPasswordResetTokenForUser(user, token);

        sendPasswordResetEmail(user.getUseremail(), token);

        return ResponseEntity.ok("Password reset email sent successfully");
    }

    private void sendPasswordResetEmail(String useremail, String token) {
    	 EmailService.sendPasswordResetEmail(useremail, token);
		
	}


    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordResetDto passwordResetDto,
                                                BindingResult bindingResult) {
        passwordResetDtoValidator.validate(passwordResetDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid input data");
        }

        User user = UserService.getUserByPasswordResetToken(passwordResetDto.getToken());
        if (user == null) {
            return ResponseEntity.badRequest().body("Invalid token");
        }

        UserService.changeUserPassword(user, passwordResetDto.getNewPassword());

        return ResponseEntity.ok("Password reset successfully");
    }

}
   

   
