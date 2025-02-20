package com.dxc.service;

import com.dxc.dto.JwtAuthenticationResponse;
import com.dxc.dto.RefreshTokenRequest;
import com.dxc.dto.SignUpRequest;
import com.dxc.dto.SigninRequest;
import com.dxc.exception.InvalidCredentialsException;
import com.dxc.exception.UserExistsException;
import com.dxc.model.User;

public interface AuthenticationService {
    User signup(SignUpRequest signUpRequest) throws UserExistsException;

    JwtAuthenticationResponse signin(SigninRequest signinRequest) throws InvalidCredentialsException;


    JwtAuthenticationResponse adminSignin(SigninRequest signinRequest);

	JwtAuthenticationResponse generateToken(User user);
	
    User findByEmail(String email);
    
    void createPasswordResetTokenForUser(User user, String token);
    
    User getUserByPasswordResetToken(String token);
    
    void changeUserPassword(User user, String newPassword);
}
	

