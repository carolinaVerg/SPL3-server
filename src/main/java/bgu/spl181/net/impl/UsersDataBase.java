package bgu.spl181.net.impl;

import java.util.concurrent.ConcurrentHashMap;

public  class UsersDataBase {
	private ConcurrentHashMap<String, String> RegisterMap;
	private ConcurrentHashMap<String, String> LoginMap;
	private static UsersDataBase Instance = new UsersDataBase();
	
	
	private UsersDataBase() {
		this.RegisterMap= new ConcurrentHashMap<String, String>();
		this.LoginMap= new ConcurrentHashMap<String, String>();	
	}

	public synchronized void addLogin(String UserName, String Password) {
		LoginMap.putIfAbsent(UserName, Password);
	}
	public synchronized void addRegister(String UserName, String Password) {
		RegisterMap.putIfAbsent(UserName, Password);
	}
	public ConcurrentHashMap<String, String>  getLogin() {
		return LoginMap;
	}
	public ConcurrentHashMap<String, String> getRegister() {
		return RegisterMap;
	}
	
	public static UsersDataBase getInstance() {
		return Instance;
	}
}