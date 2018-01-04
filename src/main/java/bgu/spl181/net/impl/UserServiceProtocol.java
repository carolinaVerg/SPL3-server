package bgu.spl181.net.impl;

import java.util.concurrent.atomic.AtomicBoolean;

import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.BlockingConnectionHandler;
import bgu.spl181.net.srv.bidi.ConnectionHandler;

public abstract class UserServiceProtocol<T> implements BidiMessagingProtocol<T> {

	private ConnectionsImpl<T> connections;
	private int connectionId;
	private AtomicBoolean shouldTerminate = new AtomicBoolean(false);
	private User user;
	AtomicBoolean isLogin= new AtomicBoolean(false);

	@Override
	public void start(int connectionId, Connections<T> connections) {
		this.connectionId = connectionId;
		this.connections = (ConnectionsImpl<T>) connections;
	}

	@Override
	public void process(T message) {
		shouldTerminate.set( "/n".equals(message));
		String fullMsg = message.toString();
		String commandType;
		if (message.toString().indexOf(" ") == -1)
			commandType = fullMsg;
		else
			commandType = fullMsg.substring(0, fullMsg.indexOf(" ") - 1);

		if (!shouldTerminate.get()) {
			switch (commandType) {
			case "REGISTER": {
				String LeftCommand = fullMsg.substring(commandType.length()+1);
				String UserName = LeftCommand.substring(0, LeftCommand.indexOf(" ") - 1);
				LeftCommand = LeftCommand.substring(UserName.length());
				String Password;
				String DataBlock="";
				if (LeftCommand.indexOf(" ") == -1)
					Password = LeftCommand;
				else {
					Password = LeftCommand.substring(0, LeftCommand.indexOf(" ")-1);
					DataBlock = LeftCommand.substring(Password.length());
					if(!this.ValidDataBlock(DataBlock)) {
						this.ERROR(commandType);
						return;	
					}
				}
				if (UsersDataBase.getInstance().getLogin().containsKey(UserName)
						| UsersDataBase.getInstance().getRegister().containsKey(UserName)) {
					this.ERROR(commandType);
					return;
				}
				UsersDataBase.getInstance().addRegister(UserName, Password);
				this.ACK(commandType);
			}
				break;

			// ......................................................................................................

			case "LOGIN": {
				if (commandType == fullMsg) {
					this.ERROR(commandType);
					return;
				}
				String LeftCommand = fullMsg.substring(commandType.length());
				String UserName = LeftCommand.substring(0, LeftCommand.indexOf(" ") - 1);
				LeftCommand = LeftCommand.substring(UserName.length());
				String Password = LeftCommand;
				if(this.isLogin.get()) {
					this.ERROR(commandType);
					return;	
				}
				if (UsersDataBase.getInstance().getLogin().containsKey(UserName)) {
					this.ERROR(commandType);
					return;
				}
				if (!UsersDataBase.getInstance().getRegister().containsKey(UserName)
						| !UsersDataBase.getInstance().getRegister().contains(Password)) {
					this.ERROR(commandType);
					return;
				}
				if (UsersDataBase.getInstance().getRegister().get(UserName) != Password) {
					this.ERROR(commandType);
					return;
				}
				UsersDataBase.getInstance().addLogin(UserName, Password);
				this.isLogin.compareAndSet(false, true);
				this.user=this.getUser(UserName, Password);
				this.ACK(commandType);
			}
				break;

			// ............................................................................................................
			case "SIGNOUT": {
				if(!UsersDataBase.getInstance().getLogin().contains(user.getUserName())) {
					this.ERROR(commandType);
					return;
				}
				UsersDataBase.getInstance().removeLogin(user.getUserName());
				this.ACK(commandType);
				this.connections.disconnect(connectionId);
				this.shouldTerminate.set(true);
			}
				break;

			// ...................................................................................................
			case "REQUEST": {
				if(!UsersDataBase.getInstance().getLogin().contains(user.getUserName())) {
					this.ERROR(commandType);
					return;
				}
				String LeftCommand = fullMsg.substring(commandType.length());
				String RequestName = LeftCommand.substring(0, LeftCommand.indexOf(" ") - 1);
				String Parameters="";
				LeftCommand = LeftCommand.substring(RequestName.length());
				if(LeftCommand.length()>0)
					Parameters = LeftCommand.substring(1);
				this.handelRequest(RequestName,Parameters);
			}
				break;

			default:
				break;
			}
		}

	}

	@Override
	public boolean shouldTerminate() {
		return shouldTerminate.get();
	}

	public abstract void handelRequest(String RequestName,String Parameters);

	public abstract User getUser(String userName,String password);
	
	public void ACK(String commandType) {
		this.connections.send(connectionId, (T) ("ACK" +commandType+ "succeeded"));
	}
	
	public void ERROR(String commandType) {
		this.connections.send(connectionId, (T) ("ERROR" +commandType+ "failed"));
	}
	

	public void BROADCAST(T message) {
		// TODO
	}
	public abstract boolean ValidDataBlock(String DataBlock);
	
	

}
