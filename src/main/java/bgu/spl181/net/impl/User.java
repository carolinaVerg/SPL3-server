package bgu.spl181.net.impl;

import java.util.concurrent.atomic.AtomicBoolean;

import bgu.spl181.net.srv.bidi.ConnectionHandler;

public abstract class User {
	String UserName;
	String DataBlock;
	String Password;
	

	public User(String userName,String password, String dataBlock) {
		this.DataBlock=dataBlock;
		this.UserName=userName;
		this.Password=password;
	}
	
	public String getUserName() {
		return this.UserName;
	}
	public String getPassword() {
		return this.Password;
	}

}
