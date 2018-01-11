package bgu.spl181.net.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.bidi.Connections;

public abstract class UserServiceProtocol<T> implements BidiMessagingProtocol<T> {

	private ConnectionsImpl<T> connections;
	private int connectionId;
	private AtomicBoolean shouldTerminate = new AtomicBoolean(false);
	private User user;
	AtomicBoolean isLogin = new AtomicBoolean(false);
	private String[] commandData;
	private UsersDataBase userDataBase;
	
	public UserServiceProtocol(UsersDataBase userDataBase) {
		this.userDataBase=userDataBase;
	}

	@Override
	public void start(int connectionId, Connections<T> connections) {
		this.connectionId = connectionId;
		this.connections = (ConnectionsImpl<T>) connections;
	}

	private String[] msgToArray(String message) {
		Pattern pattern = Pattern.compile("\"[^\"]+\"|[^\\s]+");
		ArrayList<String> dataList = new ArrayList<String>();
		Matcher matcher = pattern.matcher(message);
		while (matcher.find()) {
			String nextEntry = matcher.group(0);
			if (!nextEntry.equals("")) {
				dataList.add(nextEntry);
			}
		}
		String[] dataArray = dataList.toArray(new String[0]);
		commandData=dataArray;
		return dataArray;
	}

	@Override
	public void process(T message) {
		shouldTerminate.set("/n".equals(message));
		this.msgToArray((String) message);
		String commandType= this.commandData[0];
		if (!shouldTerminate.get()) {
			switch (commandType) {
			case "REGISTER": {
				if(this.commandData.length<3) {
					this.ERROR((T) (commandType + "failed"));
					return;
				}
				String UserName = this.commandData[1];
				String Password=this.commandData[2];
				String DataBlock = "";
				if(this.commandData.length>3)
					DataBlock=this.commandData[3];
				if(!this.ValidDataBlock(DataBlock)) {
					this.ERROR((T) (commandType + "failed"));
					return;
				}
				DataBlock=DataBlock.replaceAll("country=","");
				if(this.isLogin.get()) {
					this.ERROR((T) (commandType + "failed"));
					return;
				}
				if(userDataBase.getRegister().get(UserName)!=null) {
					this.ERROR((T) (commandType + "failed"));
					return;
				}
				this.addUser(UserName, Password, DataBlock);
				
				this.ACK((T) (commandType + "succeeded"));
			}
				break;

			// ......................................................................................................

			case "LOGIN": {
				if (this.commandData.length<3) {
					this.ERROR((T) (commandType + "failed"));
					return;
				}
				String UserName = this.commandData[1];
				String Password = this.commandData[2];
				if (this.isLogin.get()) {
					this.ERROR((T) (commandType + "failed"));
					return;
				}
				if (userDataBase.getLogin().containsKey(UserName)) {
					this.ERROR((T) (commandType + "failed"));
					return;
				}
				if (!userDataBase.getRegister().containsKey(UserName)
						| !userDataBase.getRegister().contains(Password)) {
					this.ERROR((T) (commandType + "failed"));
					return;
				}
				if (!userDataBase.getRegister().get(UserName).equals(Password)) {
					
					this.ERROR((T) (commandType + "failed"));
					return;
				}
				userDataBase.addLogin(UserName, Password);
				this.isLogin.compareAndSet(false, true);
				this.user = userDataBase.getUser(UserName);
				this.ACK((T) (commandType + "succeeded"));
			}
				break;

			// ............................................................................................................
			case "SIGNOUT": {
				if (!userDataBase.getLogin().contains(user.getUserName())) {
					this.ERROR((T) commandType);
					return;
				}
				userDataBase.removeLogin(user.getUserName());
				this.ACK((T) (commandType + "succeeded"));
				this.connections.disconnect(connectionId);
				this.shouldTerminate.set(true);
			}
				break;

			// ...................................................................................................
			case "REQUEST": {
				if (this.user==null ||userDataBase.getLogin().get(this.user.getUserName())==null) {
					this.ERROR((T) (commandType + "failed"));
					return;
				}
				String RequestName = this.commandData[1];
				String Parameters = "";
				if (this.commandData.length>2)
					Parameters = this.commandData[2];
				this.handelRequest(this.commandData);
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

	public abstract void handelRequest(String[] commandData);

	public abstract User addUser(String userName, String password, String dataBlock);

	public User userLogin() {
		return this.user;
	}

	public void ACK(T commandType) {
		this.connections.send(connectionId, (T) ("ACK " + commandType+" "));
	}

	public void ERROR(T commandType) {
		this.connections.send(connectionId, (T) ("ERROR " + commandType+" "));
	}

	public void BROADCAST(T message) {
		this.connections.setBroadCastList(userDataBase.getLogedList());
		this.connections.broadcast(message);
	}
	
	public UsersDataBase getUserDataBase() {
		return this.userDataBase;
	}

	public abstract boolean ValidDataBlock(String DataBlock);

}
