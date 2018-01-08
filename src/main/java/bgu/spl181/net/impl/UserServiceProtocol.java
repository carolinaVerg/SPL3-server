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

	@Override
	public void start(int connectionId, Connections<T> connections) {
		this.connectionId = connectionId;
		this.connections = (ConnectionsImpl<T>) connections;
	}

	private String[] msgToArray(String message) {
		Pattern pattern = Pattern.compile("\"[^\"]*\"|[^\\s]*");
		ArrayList<String> dataList = new ArrayList<String>();
		Matcher matcher = pattern.matcher(message);
		while (matcher.find()) {
			String nextEntry = matcher.group(0);
			if (!nextEntry.equals("")) {
				dataList.add(nextEntry.replaceAll("\"", ""));
			}
		}
		String[] dataArray = dataList.toArray(new String[0]);
		commandData=dataArray;
		return dataArray;
	}

	@Override
	public void process(T message) {
		shouldTerminate.set("/n".equals(message));
		String fullMsg = message.toString();
		String commandType;
		if (message.toString().indexOf(" ") == -1)
			commandType = fullMsg;
		else
			commandType = fullMsg.substring(0, fullMsg.indexOf(" ") - 1);

		if (!shouldTerminate.get()) {
			switch (commandType) {
			case "REGISTER": {
				String LeftCommand = fullMsg.substring(commandType.length() + 1);
				String UserName = LeftCommand.substring(0, LeftCommand.indexOf(" ") - 1);
				LeftCommand = LeftCommand.substring(UserName.length());
				String Password;
				String DataBlock = "";
				if (LeftCommand.indexOf(" ") == -1)
					Password = LeftCommand;
				else {
					Password = LeftCommand.substring(0, LeftCommand.indexOf(" ") - 1);
					DataBlock = LeftCommand.substring(Password.length());
					if (!this.ValidDataBlock(DataBlock)) {
						this.ERROR((T) (commandType + "failed"));
						return;
					}
				}
				if (UsersDataBase.getInstance().getLogin().containsKey(UserName)
						| UsersDataBase.getInstance().getRegister().containsKey(UserName)) {
					this.ERROR((T) (commandType + "failed"));
					return;
				}
				this.addUser(UserName, Password, DataBlock);
				this.ACK((T) (commandType + "succeeded"));
			}
				break;

			// ......................................................................................................

			case "LOGIN": {
				if (commandType == fullMsg) {
					this.ERROR((T) (commandType + "failed"));
					return;
				}
				String LeftCommand = fullMsg.substring(commandType.length());
				String UserName = LeftCommand.substring(0, LeftCommand.indexOf(" ") - 1);
				LeftCommand = LeftCommand.substring(UserName.length());
				String Password = LeftCommand;
				if (this.isLogin.get()) {
					this.ERROR((T) (commandType + "failed"));
					return;
				}
				if (UsersDataBase.getInstance().getLogin().containsKey(UserName)) {
					this.ERROR((T) (commandType + "failed"));
					return;
				}
				if (!UsersDataBase.getInstance().getRegister().containsKey(UserName)
						| !UsersDataBase.getInstance().getRegister().contains(Password)) {
					this.ERROR((T) (commandType + "failed"));
					return;
				}
				if (UsersDataBase.getInstance().getRegister().get(UserName) != Password) {
					this.ERROR((T) (commandType + "failed"));
					return;
				}
				UsersDataBase.getInstance().addLogin(UserName, Password);
				this.isLogin.compareAndSet(false, true);
				this.user = UsersDataBase.getInstance().getUser(UserName);
				this.ACK((T) (commandType + "succeeded"));
			}
				break;

			// ............................................................................................................
			case "SIGNOUT": {
				if (!UsersDataBase.getInstance().getLogin().contains(user.getUserName())) {
					this.ERROR((T) commandType);
					return;
				}
				UsersDataBase.getInstance().removeLogin(user.getUserName());
				this.ACK((T) (commandType + "succeeded"));
				this.connections.disconnect(connectionId);
				this.shouldTerminate.set(true);
			}
				break;

			// ...................................................................................................
			case "REQUEST": {
				if (!UsersDataBase.getInstance().getLogin().contains(user.getUserName())) {
					this.ERROR((T) (commandType + "failed"));
					return;
				}
				String LeftCommand = fullMsg.substring(commandType.length() + 1);
				String RequestName = LeftCommand.substring(0, LeftCommand.indexOf(" "));
				String Parameters = "";
				LeftCommand = LeftCommand.substring(RequestName.length());
				if (LeftCommand.length() > 0)
					Parameters = LeftCommand.substring(1);
				this.handelRequest(RequestName, Parameters);
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

	public abstract void handelRequest(String RequestName, String Parameters);

	public abstract User addUser(String userName, String password, String dataBlock);

	public User userLogin() {
		return this.user;
	}

	public void ACK(T commandType) {
		this.connections.send(connectionId, (T) ("ACK" + commandType));
	}

	public void ERROR(T commandType) {
		this.connections.send(connectionId, (T) ("ERROR" + commandType));
	}

	public void BROADCAST(T message) {
		this.connections.setBroadCastList(UsersDataBase.getInstance().getLogedList());
		this.connections.broadcast(message);
	}

	public abstract boolean ValidDataBlock(String DataBlock);

}
