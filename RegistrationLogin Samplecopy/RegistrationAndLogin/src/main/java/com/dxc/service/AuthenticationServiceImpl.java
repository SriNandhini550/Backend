//package com.dxc.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import com.dxc.dto.JwtAuthenticationResponse;
//import com.dxc.dto.RefreshTokenRequest;
//import com.dxc.dto.SignUpRequest;
//import com.dxc.dto.SigninRequest;
//import com.dxc.model.Role;
//import com.dxc.model.User;
//import com.dxc.repository.UserRepository;
//
//import java.util.HashMap;
//
//@Service
//public class AuthenticationServiceImpl implements AuthenticationService {
//    
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private JWTService jwtService;
//    
//
//    
//    public User signup(SignUpRequest signUpRequest) {
//        User user = new User();
//        user.setUseremail(signUpRequest.getUseremail());
//        user.setUsername(signUpRequest.getUsername());
//        user.setRole(Role.USER);
//        user.setUserpassword(passwordEncoder.encode(signUpRequest.getUserpassword()));
//        
//        return userRepository.save(user);    
//
//        
//    }
//    
//    @Override
//    public JwtAuthenticationResponse signin(SigninRequest signinRequest) {
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getUseremail(), signinRequest.getUserpassword()));
//
//        var user = userRepository.findByUseremail(signinRequest.getUseremail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
//        var jwt = jwtService.generateToken(user);
//        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
//
//        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
//        jwtAuthenticationResponse.setToken(jwt);
//        jwtAuthenticationResponse.setRefreshToken(refreshToken);
//        return jwtAuthenticationResponse;
//    }
//    
//    @Override
//    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
//        String useremail = jwtService.extractUserName(refreshTokenRequest.getToken());
//        User user = userRepository.findByUseremail(useremail).orElseThrow();
//        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
//            var jwt = jwtService.generateToken(user);
//
//            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
//            jwtAuthenticationResponse.setToken(jwt);
//            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
//            return jwtAuthenticationResponse;
//        }
//        return null;
//    }
//
//}

package com.dxc.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dxc.dto.JwtAuthenticationResponse;
import com.dxc.dto.RefreshTokenRequest;
import com.dxc.dto.SignUpRequest;
import com.dxc.dto.SigninRequest;
import com.dxc.exception.InvalidCredentialsException;
import com.dxc.exception.UserExistsException;
import com.dxc.model.PasswordResetToken;
import com.dxc.model.Role;
import com.dxc.model.User;
import com.dxc.repository.PasswordResetTokenRepository;
import com.dxc.repository.UserRepository;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;
    @Autowired
    private PasswordResetTokenRepository passwordTokenRepository;
    
    private static final int EXPIRATION_TIME = 60 * 1;

    @Override
    public User signup(SignUpRequest signUpRequest) {
        String email = signUpRequest.getUseremail();
        if (userRepository.findByuseremail(email).isPresent()) {
            throw new UserExistsException("User with email " + email + " already exists");
        }

        String password = signUpRequest.getUserpassword();
        User user = new User();
        user.setUseremail(email);
        user.setUsername(signUpRequest.getUsername());
        user.setRole(Role.USER);
        user.setUserpassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest signinRequest) {
        String email = signinRequest.getUseremail();
        String password = signinRequest.getUserpassword();

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new InvalidCredentialsException("Incorrect password");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        String token = jwtService.generateToken(userDetails);
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(token);
        
        System.out.println("jwtTOKEN: "+jwtAuthenticationResponse.toString());
        return jwtAuthenticationResponse;
    }

    @Override
    public JwtAuthenticationResponse adminSignin(SigninRequest signinRequest) {
        String email = signinRequest.getUseremail();
        String password = signinRequest.getUserpassword();

        if (isAdminCredentials(email, password)) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            String token = jwtService.generateToken(userDetails);

            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(token);
            return jwtAuthenticationResponse;
        } else {
            throw new InvalidCredentialsException("Invalid admin credentials");
        }
    }

    private boolean isAdminCredentials(String email, String password) {
        return "connected@gmail.com".equals(email) && "connected".equals(password); 
    }

    @Override
    public JwtAuthenticationResponse generateToken(User user) {
        String token = jwtService.generateToken(user);
        JwtAuthenticationResponse response = new JwtAuthenticationResponse();
        response.setToken(token);
        return response;
    }
 

    @Override
    public User findByEmail(String email) {
        Optional<User> userOptional = userRepository.findByuseremail(email);
        return userOptional.orElse(null);
    }
    
    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken();
        myToken.setToken(token);
        myToken.setUser(user);
        myToken.setExpiryDate(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
        passwordTokenRepository.save(myToken);
    }

    @Override
    public User getUserByPasswordResetToken(String token) {
        PasswordResetToken passwordToken = passwordTokenRepository.findByToken(token);
        if (passwordToken != null && !isTokenExpired(passwordToken)) {
            return passwordToken.getUser();
        }
        return null;
    }

    @Override
    public void changeUserPassword(User user, String newPassword) {
        user.setUserpassword(newPassword);
        userRepository.save(user);
    }
    
    private boolean isTokenExpired(PasswordResetToken token) {
        return token.getExpiryDate().before(new Date());
    }
}


