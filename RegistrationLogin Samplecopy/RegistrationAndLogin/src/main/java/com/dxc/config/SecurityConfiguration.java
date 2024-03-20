package com.dxc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.dxc.model.Role;
import com.dxc.service.UserService;
import com.dxc.service.UserServiceImpl;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Autowired
    private UserServiceImpl userServiceImpl;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
		.authorizeHttpRequests(request -> request.requestMatchers("/api/v1/auth/signup", "/api/v1/auth/signin","/api/v1/admin", "/api/v1/user","/api/v1/recruiter","/api/v1/advertiser","/api/v1/auth/forgotPassword", "api/v1/auth/resetPassword" )
				.permitAll()
//				.requestMatchers("/api/v1/admin").hasAnyAuthority(Role.ADMIN.name())
//				.requestMatchers("/api/v1/user").hasAnyAuthority(Role.USER.name())
//				.requestMatchers("/api/v1/recruiter").hasAnyAuthority(Role.RECRUITER.name())
//				.requestMatchers("/api/v1/advertiser").hasAnyAuthority(Role.ADVERTISER.name())
				.anyRequest().authenticated())
		
		.sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authenticationProvider(authenticationProvider()).addFilterBefore(
				jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class
				);
		return http.build();
    }
    
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//            .authorizeRequests()
//                .requestMatchers("/api/v1/auth/signup", "/api/v1/auth/signin").permitAll()
//                .requestMatchers("/api/v1/admin").hasAnyAuthority(Role.ADMIN.name())
//                .requestMatchers("/api/v1/user").hasAnyAuthority(Role.USER.name())
//                .requestMatchers("/api/v1/recruiter").hasAnyAuthority(Role.RECRUITER.name())
//                .requestMatchers("/api/v1/advertiser").hasAnyAuthority(Role.ADVERTISER.name())
//                .anyRequest().authenticated()
//            .and()
//            .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        http.authenticationProvider(authenticationProvider())
//            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }

//    	http.authorizeRequests().requestMatchers("/api/v1/user","/api/v1/admin")
//		.permitAll().anyRequest().authenticated().and().exceptionHandling().and().sessionManagement()
//		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//		
//		http.authenticationProvider(authenticationProvider()).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//	
//		return http.build();
//	}
		
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
