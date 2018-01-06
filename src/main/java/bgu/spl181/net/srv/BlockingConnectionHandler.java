package bgu.spl181.net.srv;


import bgu.spl181.net.api.MessageEncoderDecoder;


import bgu.spl181.net.api.MessagingProtocol;
import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.impl.*;
import bgu.spl181.net.srv.bidi.ConnectionHandler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockingConnectionHandler<T> implements Runnable, ConnectionHandler<T> {

    private final BidiMessagingProtocol<T> protocol;
    private final MessageEncoderDecoder<T> encdec;
    private final Socket sock;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected = true;
    private ConnectionsImpl<T> connectionsImpl;
    private int connectionId=0;
    

    public BlockingConnectionHandler(Socket sock, MessageEncoderDecoder<T> reader,BidiMessagingProtocol<T> protocol, ConnectionsImpl<T> connectionsImpl) {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;
        this.connectionsImpl=connectionsImpl;
    }

    @Override
    public void run() {
        try (Socket sock = this.sock) { //just for automatic closing
            int read;

            in = new BufferedInputStream(sock.getInputStream());

            while (!protocol.shouldTerminate() && connected && (read = in.read()) >= 0) {
                T nextMessage = encdec.decodeNextByte((byte) read);
                if (nextMessage != null) {
                    connectionsImpl.addHandler(this);
                    this.protocol.process(nextMessage);
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
    
    public BidiMessagingProtocol<T> getProtocol() {
    	return this.protocol;
    }

    @Override
    public void close() throws IOException {
        connected = false;
        sock.close();
    }

	@Override
	public void send(T msg) {
		 try {
			out = new BufferedOutputStream(sock.getOutputStream());
			if (msg != null) {
                out.write(encdec.encode(msg));
                out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public int getConnectionId() {
		return this.connectionId;
	}
	
	public void setConnectionId(int Id) {
		this.connectionId=Id;
	}
}
