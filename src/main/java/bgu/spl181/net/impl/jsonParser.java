package bgu.spl181.net.impl;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonWriter;

public class jsonParser {

	private MovieDataBase movieDataBase;
	private UsersDataBase userDataBase;
	private Gson gsonParser;
	private ReentrantReadWriteLock moviesLock;
	private ReentrantReadWriteLock usersLock;

	public jsonParser() {
		this.movieDataBase=new MovieDataBase();
		this.userDataBase=new UsersDataBase();
		this.gsonParser=new Gson();
		this.moviesLock= new ReentrantReadWriteLock();
		this.usersLock= new ReentrantReadWriteLock();
	}
	
	public MovieDataBase readMoviesFromFile() {
		this.moviesLock.readLock().lock();
		
		try {
			movieDataBase = gsonParser.fromJson(new FileReader("Database/example_Movies.json"), MovieDataBase.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		movieDataBase.updateData();
		this.moviesLock.readLock().unlock();
		return movieDataBase;
		
	}

	public UsersDataBase readUsersFromFile() {
		this.usersLock.readLock().lock();
		try {
			userDataBase = gsonParser.fromJson(new FileReader("Database/example_Users.json"), UsersDataBase.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		userDataBase.updateData();
		this.usersLock.readLock().unlock();
		return userDataBase;
		//System.out.println("readUsersFromFile - COMPLETED!");
	}

	public void writeMoviesToFile() {
		this.moviesLock.writeLock().lock();
		try (Writer writer = new FileWriter("Database/example_Movies.json")) {
			gsonParser.toJson(movieDataBase, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.moviesLock.writeLock().unlock();
		
		//System.out.println("writeMoviesToFile - COMPLETED!");
	}

	public void writeUsersToFile() {
		this.usersLock.writeLock().lock();
		try (Writer writer = new FileWriter("Database/example_Users.json")) {
			gsonParser.toJson(userDataBase, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.usersLock.writeLock().unlock();

	}
	
	public static void main(String[] args) {
		
		
	}
}
