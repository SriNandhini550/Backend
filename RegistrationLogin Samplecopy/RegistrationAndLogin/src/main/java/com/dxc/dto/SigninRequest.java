package com.dxc.dto;

import lombok.Getter;
import lombok.Setter;

//import lombok.Data;
//
//@Data
@Getter
@Setter
public class SigninRequest {
	
	private String useremail;

	private String userpassword;

	public String getUseremail() {
		return useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public String getUserpassword() {
		return userpassword;
	}

	public void setUserpassword(String userpassword) {
		this.userpassword = userpassword;
	}


}
