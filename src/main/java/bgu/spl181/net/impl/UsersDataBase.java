package bgu.spl181.net.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.experimental.theories.Theories;

import com.google.gson.annotations.SerializedName;

public  class UsersDataBase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ConcurrentHashMap<String, String> RegisterMap;
	private ConcurrentHashMap<String, String> LoginMap;
	private LinkedList<String>logedInList;
	private static ConcurrentHashMap<String, User> UsersMap;
	private static UsersDataBase Instance = new UsersDataBase();

	//TODO: Fix sync between ArrayList and HashMap
	
	@SerializedName("users")
	private ArrayList<rentalMovieUser> users;
	
	
	private UsersDataBase() {
		RegisterMap= new ConcurrentHashMap<String, String>();
		LoginMap= new ConcurrentHashMap<String, String>();	
		UsersMap=new ConcurrentHashMap<String,User>();
		logedInList= new LinkedList<String>();
		users = new ArrayList<rentalMovieUser>();
		if(users!=null)
			for(int i=0; i<users.size(); i++) {
				addUser(users.get(i));
			}
	}

	public ArrayList<rentalMovieUser> getAllUsers(){
		return users;
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
	
	
	//TODO: Check
	public void addUser(User user) {
		if(!exists(user)) {
			UsersMap.put(user.getUserName(), user);
			RegisterMap.put(user.getUserName(), user.getPassword());
		}
	}
	public User getUser(String userName) {
		return UsersMap.get(userName);
	}
	
	private static boolean exists(User user) {
		if(RegisterMap.containsKey(user.getUserName()))
			return true;
		return false;
	}
	
	public static UsersDataBase getInstance() {
		return Instance;
	}
}