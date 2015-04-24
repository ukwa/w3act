package com.thesecretserver;

public class LoginCredentials {
	public String url;
	public String username;
	public String password;
	
	public LoginCredentials() {}
	public LoginCredentials(String url, String username,
			String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}
}
