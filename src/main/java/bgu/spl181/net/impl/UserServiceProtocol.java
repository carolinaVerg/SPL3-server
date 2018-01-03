package bgu.spl181.net.impl;

import java.util.concurrent.atomic.AtomicBoolean;

import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.BlockingConnectionHandler;
import bgu.spl181.net.srv.bidi.ConnectionHandler;

public abstract class UserServiceProtocol<T> implements BidiMessagingProtocol<T> {

	private ConnectionsImpl<T> connections;
	private int connectionId;
	private boolean shouldTerminate = false;
	AtomicBoolean isLogin= new AtomicBoolean(false);

	@Override
	public void start(int connectionId, Connections<T> connections) {
		this.connectionId = connectionId;
		this.connections = (ConnectionsImpl<T>) connections;
	}

	@Override
	public void process(T message) {
		shouldTerminate = "/n".equals(message);
		String fullMsg = message.toString();
		String commandType;
		if (message.toString().indexOf(" ") == -1)
			commandType = fullMsg;
		else
			commandType = fullMsg.substring(0, fullMsg.indexOf(" ") - 1);

		if (!shouldTerminate) {
			switch (commandType) {
			case "REGISTER": {
				if (commandType == fullMsg) {
					reply((T) "ERROR registration failed (no userName)");
					return;
				}
				String LeftCommand = fullMsg.substring(commandType.length()+1);
				String UserName = LeftCommand.substring(0, LeftCommand.indexOf(" ") - 1);
				if (LeftCommand == UserName) {
					reply((T) "ERROR registration failed (no password)");
					return;
				}
				LeftCommand = LeftCommand.substring(UserName.length());
				String Password;
				String DataBlock="";
				if (LeftCommand.indexOf(" ") == -1)
					Password = LeftCommand;
				else {
					Password = LeftCommand.substring(0, LeftCommand.indexOf(" ")-1);
					DataBlock = LeftCommand.substring(Password.length());
					if(!this.ValidDataBlock(DataBlock)) {
						reply((T) "ERROR registration failed (not valid dataBlock)");
						return;	
					}
				}
				if (UsersDataBase.getInstance().getLogin().containsKey(UserName)
						| UsersDataBase.getInstance().getRegister().containsKey(UserName)) {
					reply((T) "ERROR registration failed (user exists)");
					return;
				}
				UsersDataBase.getInstance().addRegister(UserName, Password);
				reply((T) "ACK registration succeeded");
			}
				break;

			// ......................................................................................................

			case "LOGIN": {
				if (commandType == fullMsg) {
					reply((T) "ERROR login failed");
					return;
				}
				String LeftCommand = fullMsg.substring(commandType.length());
				String UserName = LeftCommand.substring(0, LeftCommand.indexOf(" ") - 1);
				if (LeftCommand == UserName) {
					reply((T) "ERROR login failed (no password)");
					return;
				}
				LeftCommand = LeftCommand.substring(UserName.length());
				String Password = LeftCommand;
				if(this.isLogin.get()) {
					reply((T) "ERROR login failed (client already logged in)");
					return;	
				}
				if (UsersDataBase.getInstance().getLogin().containsKey(UserName)) {
					reply((T) "ERROR login failed (username already logged in)");
					return;
				}
				if (!UsersDataBase.getInstance().getRegister().containsKey(UserName)
						| !UsersDataBase.getInstance().getRegister().contains(Password)) {
					reply((T) "ERROR login failed (Wrong username or password)");
					return;
				}
				if (UsersDataBase.getInstance().getRegister().get(UserName) != Password) {
					reply((T) "ERROR login failed");
					return;
				}
				UsersDataBase.getInstance().addLogin(UserName, Password);
				this.isLogin.compareAndSet(false, true);
				reply((T) "ACK login succeeded");
			}
				break;

			// ............................................................................................................
			case "SIGNOUT": {
				String msgToSend = "";
				if (commandType != fullMsg)
					msgToSend = fullMsg.substring(fullMsg.indexOf(" ") + 1);
				reply((T) msgToSend);

			}
				break;

			// ...................................................................................................
			case "REQUEST": {
				String msgToSend = "";
				if (commandType != fullMsg)
					msgToSend = fullMsg.substring(fullMsg.indexOf(" ") + 1);
				reply((T) msgToSend);

			}
				break;

			default:
				break;
			}
		}

	}

	@Override
	public boolean shouldTerminate() {
		return shouldTerminate;
	}

	public abstract void handelRequest();

	public abstract User creatUser(ConnectionHandler<T> connectionHandler, String dataBlock);
	

	public void reply(T message) {
		this.connections.send(connectionId, message);
	}
	public abstract boolean ValidDataBlock(String DataBlock);
	
	

}
