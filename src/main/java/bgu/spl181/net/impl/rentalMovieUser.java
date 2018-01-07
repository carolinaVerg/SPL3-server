package bgu.spl181.net.impl;

import java.util.LinkedList;
import java.util.List;

public class rentalMovieUser extends User{
	private boolean isAdmin=false;
	private List<Movie> MovieList;
	private String Country;
	private long Balance;
	
	

	public rentalMovieUser(String userName,String password,String country, String dataBlock, boolean isAdmin) {
		super(userName, password, dataBlock);
		this.isAdmin=isAdmin;
		this.Country=country;
		this.MovieList= new LinkedList<Movie>();
		this.Balance=0;
	}
	
	public boolean isAdmin() {
		return this.isAdmin;
	}
	
	public List<?> getMovies(){
		return this.MovieList;
	}
	
	public String getCountry() {
		return this.Country;
	}
	
	public long getBalance() {
		return this.Balance;
	}
	
	public void addBalance(long amount) {
		this.Balance=this.Balance+amount;
	}
	
	public void reduceBalance(long amount) {
		this.Balance=this.Balance-amount;
	}
	
	public void changeBalance(long newBalance) {
		this.Balance=newBalance;
	}
	
	public boolean isRenting(String movieName) {
		return MovieList.contains(movieName);
	}
	
	public void addMovie(Movie movie) {
		this.MovieList.add(movie);
	}
	
	public void removeMovie(Movie movie) {
		this.MovieList.remove(movie);
	}
	

}
