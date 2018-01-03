package bgu.spl181.net.impl;

import java.util.concurrent.atomic.AtomicBoolean;

import bgu.spl181.net.srv.bidi.ConnectionHandler;

public abstract class User {
	ConnectionHandler<String> Handler;
	String UserName;
	String DataBlock;
	String Password;
	

	public User(ConnectionHandler<String> handler, String dataBlock, String UserName, String Password) {
		this.Handler =handler;
		this.DataBlock=dataBlock;
		this.UserName=UserName;
		this.Password=Password;
	}
	
	public String getUserName() {
		return this.UserName;
	}
	public String getPassword() {
		return this.Password;
	}

}
