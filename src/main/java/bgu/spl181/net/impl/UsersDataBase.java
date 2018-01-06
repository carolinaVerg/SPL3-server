package bgu.spl181.net.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public  class UsersDataBase {
	private ConcurrentHashMap<String, String> RegisterMap;
	private ConcurrentHashMap<String, String> LoginMap;
	private LinkedList<String>logedInList;
	private ConcurrentHashMap<String, User> UsersMap;
	private static UsersDataBase Instance = new UsersDataBase();
	
	
	private UsersDataBase() {
		this.RegisterMap= new ConcurrentHashMap<String, String>();
		this.LoginMap= new ConcurrentHashMap<String, String>();	
		this.UsersMap=new ConcurrentHashMap<String,User>();
		this.logedInList= new LinkedList<String>();
	}

	public void addLogin(String UserName, String Password) {
		LoginMap.putIfAbsent(UserName, Password);
		logedInList.add(UserName);
	}
	
	public void removeLogin(String UserName) {
		LoginMap.remove(UserName);
		logedInList.remove(UserName);
	}
	
	public List<String> getLogedList(){
		return this.logedInList;
	}
	
	public ConcurrentHashMap<String, String>  getLogin() {
		return LoginMap;
	}
	
	public ConcurrentHashMap<String, String> getRegister() {
		return RegisterMap;
	}
	
	public void addUser(User user) {
		if(!this.exists(user)) {
			this.UsersMap.put(user.getUserName(), user);
			this.RegisterMap.put(user.getUserName(), user.getPassword());
		}
	}
	public User getUser(String userName) {
		return this.UsersMap.get(userName);
	}
	
	private boolean exists(User user) {
		if(this.RegisterMap.containsKey(user.getUserName()))
			return true;
		return false;
	}
	
	public static UsersDataBase getInstance() {
		return Instance;
	}
}