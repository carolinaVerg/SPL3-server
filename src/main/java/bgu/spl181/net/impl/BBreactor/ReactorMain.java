package bgu.spl181.net.impl.BBreactor;

import bgu.spl181.net.impl.MovieRentalService;
import bgu.spl181.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl181.net.srv.Reactor;

public class ReactorMain {

	public ReactorMain() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		Reactor<String> server = new Reactor<String>(8, 7777, ()-> new MovieRentalService(), ()-> new LineMessageEncoderDecoder()) ;
			
		server.serve();
	}

}
