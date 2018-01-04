package bgu.spl181.net.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.bidi.ConnectionHandler;

public class ConnectionsImpl<T> implements Connections<T> {
	
	private ConcurrentHashMap<Integer, ConnectionHandler<T>> connectionMap;
	private AtomicInteger connectionId= new AtomicInteger(0);

	public ConnectionsImpl() {
		connectionMap = new ConcurrentHashMap<Integer, ConnectionHandler<T>>();
		
	}
	
	@Override
	public boolean send(int connectionId, T msg) {
		if(!connectionMap.containsKey(connectionId))
			return false;
		connectionMap.get(connectionId).send(msg);
		return true;
	}

	@Override
	public void broadcast(T msg) {
		for (Map.Entry<Integer, ConnectionHandler<T>> entry : connectionMap.entrySet()) {
			entry.getValue().send(msg);
		}
	}

	@Override
	public void disconnect(int connectionId) {
		try {
			this.connectionMap.get(connectionId).close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connectionMap.remove(connectionId);
		
	}
	
	public synchronized void addHandler(ConnectionHandler<T> Handler) {
		if(!connectionMap.contains(Handler)) {
			connectionMap.put(connectionId.getAndIncrement(), Handler);
		}
	}
	
	public ConnectionHandler<T> getHandler(int connId){
		return this.connectionMap.get(connId);
	}

	
	

}
