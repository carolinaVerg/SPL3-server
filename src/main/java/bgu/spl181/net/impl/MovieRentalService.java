package bgu.spl181.net.impl;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.swing.JEditorPane;

public class MovieRentalService<T> extends UserServiceProtocol<T>{
	private MovieDataBase movieDataBase;
	private ReentrantReadWriteLock usersLock;
	private ReentrantReadWriteLock moviesLock;
	
	
	public MovieRentalService(UsersDataBase usersDataBase, MovieDataBase movieDataBase, jsonParser parser) {
		super(usersDataBase, parser);
		this.movieDataBase= movieDataBase;
		this.usersLock=this.getUserDataBase().getLock();
		this.moviesLock=movieDataBase.getLock();
	}

	@Override
	public void handelRequest(String[] commandData) {
		String RequestName=commandData[1];
		switch (RequestName) {
		case "balance":{
			String nextWord=commandData[2];
			switch (nextWord) {
			case "info":{
				this.usersLock.readLock().lock();
				this.ACK((T) ("balance "+((rentalMovieUser) this.userLogin()).getBalance()));
				this.usersLock.readLock().unlock();
			}
				break;
			case "add":{
				String amount =commandData[3];
				int Intamount= Integer.parseInt(amount);
				this.usersLock.writeLock().lock();
				 ((rentalMovieUser) this.userLogin()).addBalance(Intamount);
				 this.ACK((T) ("balance "+ ((rentalMovieUser) this.userLogin()).getBalance()+" add "+amount) );
				 this.usersLock.writeLock().unlock();
			}
				break;

			default:
				break;
			}
		}
			break;
		//...........................................................................................	
		case "info":{
			String toReturn="";
			this.moviesLock.readLock().lock();
			if(commandData.length<3) {
				toReturn=String.join(",", this.movieDataBase.getMovieNameList()); 
				this.ACK((T) ("info "+toReturn));
				this.moviesLock.readLock().unlock();
				return;
			}
			else {
				String movieName =commandData[2];
				Movie movie=this.movieDataBase.getMovie(movieName);
				if(movie==null) {
					this.ERROR((T) ("request info"));
					this.moviesLock.readLock().unlock();
					return;
				}
				if(movie.getBannedCountries().size()>0)
					toReturn=movie.getName()+" "+ movie.getAvailbleAmount()+" "+movie.getPrice()+" "+movie.getBannedCountries();
				else
					toReturn=movie.getName()+" "+ movie.getAvailbleAmount()+" "+movie.getPrice();
				this.ACK((T) ("info "+toReturn));
				this.moviesLock.readLock().unlock();
			}
			
		}
			break;
	//...............................................................................................		
		case "rent":{
			String movieName =commandData[2];
			this.usersLock.readLock().lock();
			this.moviesLock.readLock().lock();
			Movie movie=this.movieDataBase.getMovie(movieName);
			if(((rentalMovieUser) this.userLogin()).isRenting(movieName)| movie==null) {
				this.ERROR((T) "request rent failed");
				this.usersLock.readLock().unlock();
				this.moviesLock.readLock().unlock();
				return;
			}
			if(movie.isBanned(((rentalMovieUser) this.userLogin()).getCountry())| movie.getAvailbleAmount()==0) {
				this.ERROR((T) "request rent failed");
				this.usersLock.readLock().unlock();
				this.moviesLock.readLock().unlock();
				return;
			}
			if(((rentalMovieUser) this.userLogin()).getBalance()<movie.getPrice()) {
				this.ERROR((T) "request rent failed");
				this.usersLock.readLock().unlock();
				this.moviesLock.readLock().unlock();
				return;
			}
			this.usersLock.readLock().unlock();
			this.moviesLock.readLock().unlock();
			this.usersLock.writeLock().lock();
			this.moviesLock.writeLock().lock();
			((rentalMovieUser) this.userLogin()).addMovie(movie);
			((rentalMovieUser) this.userLogin()).reduceBalance(movie.getPrice());
			movie.reduceAmount();
			this.ACK((T) ("rent "+movieName+" success"));
			this.BROADCAST((T) ("movie "+movieName+" "+movie.getAvailbleAmount()+" "+movie.getPrice()));
			this.usersLock.writeLock().unlock();
			this.moviesLock.writeLock().unlock();
		}
			break;
	//...............................................................................................		
		case "return":{
			String movieName =commandData[2];
			this.usersLock.readLock().lock();
			this.moviesLock.readLock().lock();
			Movie movie=this.movieDataBase.getMovie(movieName);
			if(!((rentalMovieUser) this.userLogin()).isRenting(movieName)| movie==null) {
				this.ERROR((T) "request return failed");
				this.usersLock.readLock().unlock();
				this.moviesLock.readLock().unlock();
				return;
			}
			this.usersLock.readLock().unlock();
			this.moviesLock.readLock().unlock();
			this.usersLock.writeLock().lock();
			this.moviesLock.writeLock().lock();
			((rentalMovieUser) this.userLogin()).removeMovie(movie);
			movie.incAmount();
			this.ACK((T) ("return "+ movieName+"success"));
			this.BROADCAST((T) ("movie "+movieName+movie.getAvailbleAmount()+" " +movie.getPrice()));
			this.usersLock.writeLock().unlock();
			this.moviesLock.writeLock().unlock();
		}
			break;
	//...............................................................................................
		case "addmovie":{
			this.usersLock.readLock().lock();
			this.moviesLock.readLock().lock();
			String movieName=commandData[2];
			String amount=commandData[3];
			int intAmount= Integer.parseInt(amount);
			String price =commandData[4];
			int intPrice =Integer.parseInt(price);
			LinkedList<String>bannedCountriesList= new LinkedList<String>();
			if(commandData.length>5) {
			for(int i=5; i<commandData.length; i++)
				bannedCountriesList.add(commandData[i]);
			}
			if(!((rentalMovieUser) this.userLogin()).isAdmin()| this.movieDataBase.getMovie(movieName)!=null) {
				this.ERROR((T) "request addmovie failed");
				this.usersLock.readLock().unlock();
				this.moviesLock.readLock().unlock();
				return;
			}
			
			if(intAmount>0 & intPrice>0) {
				Movie movie = new Movie(this.movieDataBase.getHighestId(), movieName, intPrice, bannedCountriesList, intAmount, intAmount);
				this.usersLock.readLock().unlock();
				this.moviesLock.readLock().unlock();
				this.moviesLock.writeLock().lock();
				this.movieDataBase.addMovie(movie);
				this.ACK((T) ("addmovie "+movieName+" success"));
				this.BROADCAST((T) ("movie "+movieName+" " +movie.getAvailbleAmount()+" "+movie.getPrice()));
				this.moviesLock.writeLock().unlock();
			}
			else {
				this.ERROR((T) "request addmovie failed");
				this.usersLock.readLock().unlock();
				this.moviesLock.readLock().unlock();
				return;
			}
		}
			
			break;
	//.............................................................................................		
		case "remmovie":{
			String movieName=commandData[2];
			this.usersLock.readLock().lock();
			this.moviesLock.readLock().lock();
			Movie movie=this.movieDataBase.getMovie(movieName);
			if(!((rentalMovieUser) this.userLogin()).isAdmin()| movie==null) {
				this.ERROR((T) "request remmovie failed");
				this.usersLock.readLock().unlock();
				this.moviesLock.readLock().unlock();
				return;
			}
			
			if(movie.getAvailbleAmount()!=movie.getTotalAmount()) {
				this.ERROR((T) "request remmovie failed");
				this.usersLock.readLock().unlock();
				this.moviesLock.readLock().unlock();
				return;
			}
			this.usersLock.readLock().unlock();
			this.moviesLock.readLock().unlock();
			this.moviesLock.writeLock().lock();
			this.movieDataBase.removeMovie(movie);
			this.moviesLock.writeLock().unlock();
			this.ACK((T) ("remmovie "+movieName+" success"));
			this.BROADCAST((T) ("movie "+movieName+" removed"));
			
		}
			
			break;
	//..........................................................................................		
		case "changeprice":{
			String movieName=commandData[2];
			String price=commandData[3];
			this.usersLock.readLock().lock();
			this.moviesLock.readLock().lock();
			int intPrice =Integer.parseInt(price);
			Movie movie=this.movieDataBase.getMovie(movieName);
			if(!((rentalMovieUser) this.userLogin()).isAdmin()| movie==null) {
				this.ERROR((T) "request changeprice failed");
				this.usersLock.readLock().unlock();
				this.moviesLock.readLock().unlock();
				return;
			}
			if(intPrice<0| intPrice==0) {
				this.ERROR((T) "request changeprice failed");
				return;
			}
			this.usersLock.readLock().unlock();
			this.moviesLock.readLock().unlock();
			this.moviesLock.writeLock().lock();
			movie.setPrice(intPrice);
			this.ACK((T) ("changeprice "+movieName+" success"));
			this.BROADCAST((T) ("movie "+movieName+movie.getAvailbleAmount()+" "+ movie.getPrice()));
			this.moviesLock.writeLock().unlock();
		}
			
			break;
		default:
			break;
		}
	}

	@Override
	public User addUser(String userName, String password, String dataBlock) {
		User normalUser = new rentalMovieUser(userName, password, dataBlock, "normal");
		this.getUserDataBase().addUser(normalUser);
		return null;
	}

	@Override
	public boolean ValidDataBlock(String DataBlock) {
		try {
			String toCheck= DataBlock.substring(0, DataBlock.indexOf("=")+1);
			if(toCheck.equals("country="))
				return true;
		}
		catch(Exception e){
			return false;
		}
		return false;
	}

}


