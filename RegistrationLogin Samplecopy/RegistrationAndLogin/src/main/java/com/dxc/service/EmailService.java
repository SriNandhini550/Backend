package com.dxc.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);

	static void sendPasswordResetEmail(String useremail, String token) {
		// TODO Auto-generated method stub
		
	}
}
