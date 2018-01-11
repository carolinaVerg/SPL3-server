package bgu.spl181.net.impl.BBreactor;

import bgu.spl181.net.impl.MovieRentalService;
import bgu.spl181.net.impl.jsonParser;
import bgu.spl181.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl181.net.srv.Reactor;

public class ReactorMain {

	public ReactorMain() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		jsonParser parser =new jsonParser();
		Reactor<String> server = new Reactor<String>(8, 7877, ()-> new MovieRentalService(parser.readUsersFromFile(),parser.readMoviesFromFile(), parser), ()-> new LineMessageEncoderDecoder()) ;
			
		server.serve();
	}

}
