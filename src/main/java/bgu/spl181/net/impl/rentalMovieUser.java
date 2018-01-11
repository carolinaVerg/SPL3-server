package bgu.spl181.net.impl;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class rentalMovieUser extends User implements Serializable{
	
	private static final long serialVersionUID = 1L;
	//private boolean isAdmin=false;
	@SerializedName("type")
	private String type = "normal";
	@SerializedName("movies")
	private ArrayList<movieNameId> movies;
//	private ArrayList<Movie> fullMovies;
	@SerializedName("country")
	private String country;
	@SerializedName("balance")
	private long balance;
	
	

	public rentalMovieUser(String userName,String password,String country, String type) {
		super(userName, password, country);
		this.type=type;
		this.country=country;
		this.movies= new ArrayList<movieNameId>();
		this.balance=0;
	}
	
	public boolean isAdmin() {
		return this.type.equals("admin");
	}
	
	
	public ArrayList<movieNameId> getMovies(){
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
		for (movieNameId movieNameId : movies) {
			if(movieNameId.getName().equals(movieName))
				return true;
		}
		return false;
	}
	
	public void addMovie(Movie movie) {
		movieNameId movieToAdd = new movieNameId(movie.getName(), movie.getId());
		this.movies.add(movieToAdd);
	}
	
	public void removeMovie(Movie movie) {
		this.movies.remove(movie);
	}
	
}
