package bgu.spl181.net.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import bgu.spl181.net.srv.bidi.ConnectionHandler;

public class MovieDataBase {
	private static ConcurrentHashMap<String, Movie> MovieMap;
	private static final MovieDataBase instance= new MovieDataBase();
	private static int highestId=0;
	private static List<Movie> movies;
	
	private MovieDataBase() {
		MovieMap= new ConcurrentHashMap<>();
	}
	
	public List<Movie> getAllMovies() {
		return movies;
	}
	
	public Movie getMovie(String name) {
		return MovieMap.get(name);
	}
	
	public List<String> getMovieList(){
		List<String> MovieList= new LinkedList<String>();
		for (ConcurrentHashMap.Entry<String, Movie> entry : MovieMap.entrySet()) {
			MovieList.add(entry.getKey());
		}
		return MovieList;
		
	}
	
	public synchronized static void  addMovie(Movie newMovie) {
		if(!MovieMap.containsKey(newMovie.getName())) {
			MovieMap.put(newMovie.getName(), newMovie);
			if(!movies.contains(newMovie))
				movies.add(newMovie);
		}
		
		if(newMovie.getId()==highestId)
			highestId=newMovie.getId()+1;
		
		
	}
	
	public synchronized void removeMovie(Movie movie) {
		MovieMap.remove(movie);
		movies.remove(movie);
		if(movie.getId()==highestId-1)
			highestId--;
		
	}
	
	public int getHighestId() {
		return highestId;
	}
	
	public static MovieDataBase getInstance() {
		for(int i=0; i<movies.size(); i++)
			addMovie(movies.get(i));
		return instance;
	}
}
