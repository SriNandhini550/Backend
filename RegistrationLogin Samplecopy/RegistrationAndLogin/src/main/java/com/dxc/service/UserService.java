package com.dxc.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.dxc.model.User;

public interface UserService extends UserDetailsService{

UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

static User findUserByEmail(String userEmail) {
	// TODO Auto-generated method stub
	return null;
}

static void createPasswordResetTokenForUser(User user, String token) {
	// TODO Auto-generated method stub
	
}

static User getUserByPasswordResetToken(String token) {
	// TODO Auto-generated method stub
	return null;
}

static void changeUserPassword(User user, String newPassword) {
	// TODO Auto-generated method stub
	
}


}
