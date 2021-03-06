package bgu.spl181.net.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.junit.experimental.theories.Theories;

import com.google.gson.annotations.SerializedName;

public  class UsersDataBase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ConcurrentHashMap<String, String> RegisterMap;
	private transient ConcurrentHashMap<String, String> LoginMap;
	private static ConcurrentHashMap<String, User> UsersMap;
	private static ReentrantReadWriteLock lock;

	//TODO: Fix sync between ArrayList and HashMap
	
	@SerializedName("users")
	private ArrayList<rentalMovieUser> users;
	
	
	public UsersDataBase() {
		RegisterMap= new ConcurrentHashMap<String, String>();
		LoginMap= new ConcurrentHashMap<String, String>();	
		UsersMap=new ConcurrentHashMap<String,User>();
		users = new ArrayList<rentalMovieUser>();
		lock = new ReentrantReadWriteLock();
	}
	
	public void updateData() {
		if(users!=null) {
			for(int i=0; i<users.size(); i++) {
				addUser(users.get(i));
			}
		}
		
	}

	public ArrayList<rentalMovieUser> getAllUsers(){
		return users;
	}
	
	public void addLogin(String UserName, String Password) {
		LoginMap.put(UserName, Password);
		
	}
	
	public void removeLogin(String UserName) {
		LoginMap.remove(UserName);
	}
	
	
	public ConcurrentHashMap<String, String>  getLogin() {
		return LoginMap;
	}
	
	public ConcurrentHashMap<String, String> getRegister() {
		for (rentalMovieUser user : users) {
			if(RegisterMap.get(user.getUserName())==null)
				RegisterMap.put(user.getUserName(), user.getPassword());
		}
		return RegisterMap;
	}
	
	
	
	public void addUser(User user) {
		if(!exists(user)) {
			UsersMap.put(user.getUserName(), user);
			RegisterMap.put(user.getUserName(), user.getPassword());
			if(!users.contains(user))
				users.add((rentalMovieUser)user);
		}
	}
	
	public User getUser(String userName) {
		for (rentalMovieUser user: users) {
			if(UsersMap.get(user.getUserName())==null)
				UsersMap.put(user.getUserName(), user);
			if(RegisterMap.get(user.getUserName())==null)
				RegisterMap.put(user.getUserName(), user.getPassword());
		}
		return UsersMap.get(userName);
	}
	
	private static boolean exists(User user) {
		if(RegisterMap.containsKey(user.getUserName())) {
			return true;
		}
		return false;
	}
	
	public ReentrantReadWriteLock getLock() {
		return lock;
	}
	
}