package bgu.spl181.net.impl;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public abstract class User implements Serializable {
	
	@SerializedName("username")
	protected String UserName;
	protected String DataBlock;
	@SerializedName("password")
	protected String Password;

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
