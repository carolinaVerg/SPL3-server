package bgu.spl181.net.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.BlockingConnectionHandler;
import bgu.spl181.net.srv.bidi.ConnectionHandler;

public class ConnectionsImpl<T> implements Connections<T> {
	
	private ConcurrentHashMap<Integer, ConnectionHandler<T>> connectionMap;
	private List<String> broadCastList;
	private AtomicInteger connectionId= new AtomicInteger(0);

	public ConnectionsImpl() {
		this.connectionMap = new ConcurrentHashMap<Integer, ConnectionHandler<T>>();
		this.broadCastList= new LinkedList<String>();
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
			if(this.broadCastList.contains(entry.getKey()))
				entry.getValue().send(msg);
		}
	}

	@Override
	public void disconnect(int connectionId) {
		try {
			this.connectionMap.get(connectionId).close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		connectionMap.remove(connectionId);
		
	}
	
	public synchronized void addHandler(ConnectionHandler<T> Handler) {
		if(!connectionMap.contains(Handler)) {
			((BlockingConnectionHandler<T>) Handler).setConnectionId(connectionId.get());
			connectionMap.put(connectionId.getAndIncrement(), Handler);
		}
		
	}
	
	public ConnectionHandler<T> getHandler(int connId){
		return this.connectionMap.get(connId);
	}
	
	public void setBroadCastList(List<String> broadCast) {
		this.broadCastList=broadCast;
	}
	
	

	
}
