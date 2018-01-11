package bgu.spl181.net.impl;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public abstract class User implements Serializable {
	
	@SerializedName("username")
	protected String UserName;
	protected transient String DataBlock;
	@SerializedName("password")
	protected String Password;
	private transient boolean logedIn= false;
	private transient Integer ConnectionId;

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
	
	public boolean getLogedIn() {
		return this.logedIn;
	}
	public void setLogedIn(boolean logedIn) {
		this.logedIn=logedIn;
	}
	public Integer getConnectionId() {
		return this.ConnectionId;
	}
	public void setConectionId(int Id) {
		this.ConnectionId= new Integer(Id);
	}

}
