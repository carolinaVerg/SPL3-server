package bgu.spl181.net.impl.BBtpc;

import bgu.spl181.net.impl.MovieRentalService;
import bgu.spl181.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl181.net.srv.BaseServer;
import bgu.spl181.net.srv.BlockingConnectionHandler;
import bgu.spl181.net.srv.Server;

public class TPCMain {

	public TPCMain() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		BaseServer<String> movieRentalServer= new BaseServer<String>(7777,()-> new MovieRentalService(), ()-> new LineMessageEncoderDecoder()) {

			@Override
			protected void execute(BlockingConnectionHandler<String> handler) {
				new Thread(handler).start();
			}
		};
		movieRentalServer.serve();
	}

}
