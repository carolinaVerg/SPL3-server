package bgu.spl181.net.impl;

import java.util.LinkedList;

public class MovieRentalService<T> extends UserServiceProtocol<T>{

	@Override
	public void handelRequest(String RequestName, String Parameters) {
		String nextWord="";
		switch (RequestName) {
		case "balance":{
			nextWord=Parameters.substring(0,Parameters.indexOf(" "));
			switch (nextWord) {
			case "info":
				this.ACK((T) ("balance"+((rentalMovieUser) this.userLogin()).getBalance()));
				break;
			case "add":{
				String amount =Parameters.substring(nextWord.length()+1);
				int Intamount= Integer.parseInt(amount);
				 ((rentalMovieUser) this.userLogin()).addBalance(Intamount);
				 this.ACK((T) ("balance"+ ((rentalMovieUser) this.userLogin()).getBalance()+"addae"+amount) );
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
			if(Parameters.length()==0) {
				toReturn=String.join(",", MovieDataBase.getInstance().getMovieList()); 
				this.ACK((T) ("info "+toReturn));
				return;
			}
			else {
				String movieName =Parameters;
				Movie movie=MovieDataBase.getInstance().getMovie(movieName);
				if(movie==null) {
					this.ERROR((T) (movieName+ "do not exists"));
					return;
				}
				toReturn=movie.getName()+ movie.getAvailbleAmount()+movie.getPrice()+movie.getBannedCountries()
				+movie.getId()+movie.getTotalAmount();
				this.ACK((T) ("info "+toReturn));
			}
			
		}
			break;
	//...............................................................................................		
		case "rent":{
			String movieName =Parameters;
			Movie movie=MovieDataBase.getInstance().getMovie(movieName);
			if(((rentalMovieUser) this.userLogin()).isRenting(movieName)| movie==null) {
				this.ERROR((T) "rent error");
				return;
			}
			if(movie.isBanned(((rentalMovieUser) this.userLogin()).getCountry())| movie.getAvailbleAmount()==0) {
				this.ERROR((T) "rent error");
				return;
			}
			if(((rentalMovieUser) this.userLogin()).getBalance()<movie.getPrice()) {
				this.ERROR((T) "rent error");
				return;
			}
			
			((rentalMovieUser) this.userLogin()).addMovie(movie);
			((rentalMovieUser) this.userLogin()).reduceBalance(movie.getPrice());
			movie.reduceAmount();
			this.ACK((T) ("rent"+movieName+"success"));
			this.BROADCAST((T) ("movie"+movieName+movie.getAvailbleAmount()+movie.getPrice()));
			
		}
			break;
	//...............................................................................................		
		case "return":{
			String movieName =Parameters;
			Movie movie=MovieDataBase.getInstance().getMovie(movieName);
			if(!((rentalMovieUser) this.userLogin()).isRenting(movieName)| movie==null) {
				this.ERROR((T) "return error");
				return;
			}
			((rentalMovieUser) this.userLogin()).removeMovie(movie);
			movie.incAmount();
			this.ACK((T) ("return"+ movieName+"success"));
			this.BROADCAST((T) ("movie"+movieName+movie.getAvailbleAmount()+movie.getPrice()));
		}
			break;
	//...............................................................................................
		case "addmovie":{
			String parametersLeft=Parameters;
			String movieName=parametersLeft.substring(0,parametersLeft.indexOf(" "));
			parametersLeft=parametersLeft.substring(movieName.length()+1);
			String amount=parametersLeft.substring(0,parametersLeft.indexOf(" "));
			parametersLeft=parametersLeft.substring(amount.length()+1);
			int intAmount= Integer.parseInt(amount);
			String price =parametersLeft.substring(0,parametersLeft.indexOf(" "));
			parametersLeft=parametersLeft.substring(price.length()+1);
			int intPrice =Integer.parseInt(price);
			String bannedCountries =parametersLeft;
			String country="";
			LinkedList<String>bannedCountriesList= new LinkedList<String>();
			while(bannedCountries.length()>0) {
				if(bannedCountries.indexOf(",")!=-1) {
					country=bannedCountries.substring(0,bannedCountries.indexOf(","));
					bannedCountriesList.add(country);
					bannedCountries= bannedCountries.substring(bannedCountries.indexOf(",")+1);
					}
				else {
					country=bannedCountries;
					bannedCountries="";
					bannedCountriesList.add(country);
				}
			}
			if(!((rentalMovieUser) this.userLogin()).isAdmin()| MovieDataBase.getInstance().getMovie(movieName)!=null) {
				this.ERROR((T) "addmovie failed");
				return;
			}
			if(intAmount>0 & intPrice>0) {
				Movie movie = new Movie(MovieDataBase.getInstance().getHighestId(), movieName, intPrice, bannedCountriesList, intAmount, intAmount);
				MovieDataBase.getInstance().addMovie(movie);
				this.ACK((T) ("addmovie"+movieName+"success"));
				this.BROADCAST((T) ("movie"+movieName+movie.getAvailbleAmount()+movie.getPrice()));
			}
			else {
				this.ERROR((T) "addmovie failed");
				return;
			}
		}
			
			break;
	//.............................................................................................		
		case "remmovie":{
			String movieName=Parameters;
			Movie movie=MovieDataBase.getInstance().getMovie(movieName);
			if(!((rentalMovieUser) this.userLogin()).isAdmin()| movie==null) {
				this.ERROR((T) "remmovie failed");
				return;
			}
			
			if(movie.getAvailbleAmount()!=movie.getTotalAmount()) {
				this.ERROR((T) "remmovie failed");
				return;
			}
			MovieDataBase.getInstance().removeMovie(movie);
			this.ACK((T) ("remmovie"+movieName+"success"));
			this.BROADCAST((T) ("movie"+movieName+"removed"));
			
		}
			
			break;
	//..........................................................................................		
		case "changeprice":{
			String parametersLeft=Parameters;
			String movieName=parametersLeft.substring(0,parametersLeft.indexOf(" "));
			parametersLeft=parametersLeft.substring(movieName.length()+1);
			String price=parametersLeft;
			int intPrice =Integer.parseInt(price);
			Movie movie=MovieDataBase.getInstance().getMovie(movieName);
			if(!((rentalMovieUser) this.userLogin()).isAdmin()| movie==null) {
				this.ERROR((T) "changeprice failed");
				return;
			}
			if(intPrice<0| intPrice==0) {
				this.ERROR((T) "changeprice failed");
				return;
			}
			movie.setPrice(intPrice);
			this.ACK((T) ("changeprice"+movieName+"success"));
			this.BROADCAST((T) ("movie"+movieName+movie.getAvailbleAmount()+movie.getPrice()));
		}
			
			break;
		default:
			break;
		}
	}

	@Override
	public User addUser(String userName, String password, String dataBlock) {
		//TODO: checkThis
		User normalUser = new rentalMovieUser(userName, password, dataBlock, "normal");
		//TODO CHECK THIS
		UsersDataBase.getInstance().addUser(normalUser);
		return null;
	}

	@Override
	public boolean ValidDataBlock(String DataBlock) {
		// TODO Auto-generated method stub
		return false;
	}

}


