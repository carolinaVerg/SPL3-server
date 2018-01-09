package bgu.spl181.net.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class rentalMovieUser extends User implements Serializable{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isAdmin=false;
	@SerializedName("type")
	private String type = "normal";
	@SerializedName("movies")
	private ArrayList<Movie> movies;
	@SerializedName("country")
	private String country;
	@SerializedName("balance")
	private long balance;
	
	

	public rentalMovieUser(String userName,String password,String country, boolean isAdmin) {
		super(userName, password, country);
		this.isAdmin=isAdmin;
		if(this.isAdmin)
			type="admin";
		else type="normal";
		this.country=country;
		this.movies= new ArrayList<Movie>();
		this.balance=0;
	}
	
	public boolean isAdmin() {
		return this.isAdmin;
	}
	
	public ArrayList<Movie> getMovies(){
		return this.movies;
	}
	
	public String getCountry() {
		return this.country;
	}
	
	public long getBalance() {
		return this.balance;
	}
	
	public void addBalance(long amount) {
		this.balance=this.balance+amount;
	}
	
	public void reduceBalance(long amount) {
		this.balance=this.balance-amount;
	}
	
	public void changeBalance(long newBalance) {
		this.balance=newBalance;
	}
	
	public boolean isRenting(String movieName) {
		return movies.contains(movieName);
	}
	
	public void addMovie(Movie movie) {
		this.movies.add(movie);
	}
	
	public void removeMovie(Movie movie) {
		this.movies.remove(movie);
	}
	

}
