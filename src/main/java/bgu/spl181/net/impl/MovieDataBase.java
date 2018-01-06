package bgu.spl181.net.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import bgu.spl181.net.srv.bidi.ConnectionHandler;

public class MovieDataBase {
	private ConcurrentHashMap<String, Movie> MovieMap;
	private static final MovieDataBase instance= new MovieDataBase();
	private int highestId=0;
	
	private MovieDataBase() {
		this.MovieMap= new ConcurrentHashMap<>();
	}
	
	public Movie getMovie(String name) {
		return this.MovieMap.get(name);
	}
	
	public List<String> getMovieList(){
		List<String> MovieList= new LinkedList<String>();
		for (ConcurrentHashMap.Entry<String, Movie> entry : MovieMap.entrySet()) {
			MovieList.add(entry.getKey());
		}
		return MovieList;
		
	}
	
	public void addMovie(Movie newMovie) {
		if(!MovieMap.containsKey(newMovie.getName()))
			this.MovieMap.put(newMovie.getName(), newMovie);
		if(newMovie.getId()==this.highestId)
			this.highestId=newMovie.getId()+1;
	}
	
	public void removeMovie(Movie movie) {
		this.MovieMap.remove(movie);
		if(movie.getId()==this.highestId-1)
			this.highestId--;
	}
	
	public int getHighestId() {
		return this.highestId;
	}
	
	public static MovieDataBase getInstance() {
		return instance;
	}
}
