package bgu.spl181.net.impl;


public abstract class User {
	protected String UserName;
	protected String DataBlock;
	protected String Password;
	private int check;
	//TODO: CHNAGE
	

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
