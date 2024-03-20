package com.dxc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dxc.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	

			
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			    return userRepository.findByUseremail(username)
			            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
			}



			@Override
			public UserDetailsService userDetailsService() {
				// TODO Auto-generated method stub
				return null;
			}

		}
	




