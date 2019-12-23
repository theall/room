package lan.client.thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import lan.client.util.ClientInterface;
import lan.utils.Room;
import lan.utils.Team;
import lan.utils.NetCommand;
import lan.utils.Player;
import lan.utils.Position;
import lan.utils.NetCommand.Code;

public class WorkThread extends Thread { //工作线程
	private String remoteHost;// Server host
	private int port;
	private Room room;
	private ObjectOutputStream out; //对象输出流
	private ObjectInputStream in;//输入
	private Player me;//玩家
	private String name;//玩家名字
	private Socket socket;//槽插座
	private ClientInterface clientInterface;//客户端接口
	private boolean exit;
	public WorkThread(String host, int port, String name) { //工作线程
		remoteHost = host;
		this.port = port;
		this.name = name;
		this.exit = false;
	}

	public void setClientInterface(ClientInterface clientInterface) { //客户端接口参数
		this.clientInterface = clientInterface; //启动接口
	}

	@Override
	public void run() {
		try {
			// 建立客户端Socket连接，指定服务器的位置以及端口
			InetAddress address = InetAddress.getByName(remoteHost);
			socket = new Socket(address, port);

			out = new ObjectOutputStream(socket.getOutputStream()); //OBject是对象，对象输出流
			in = new ObjectInputStream(socket.getInputStream()); //对象输入流

			NetCommand in_cmd = null;
			while (!exit) {
				try {
					in_cmd = (NetCommand) in.readObject(); //读对象
				} catch (SocketException se) {
					System.out.println("Socket closed!");
					exit = true;
					break;
				} catch (IOException e) {
					System.out.println("Io error, exit!");
					exit = true;
					break;
				}
				NetCommand out_cmd = new NetCommand();
				int senderId = in_cmd.getSender();
				Player sender = room!=null?room.findPlayerById(senderId):null;
				switch (in_cmd.getCode()) {
				case HELLO:
					out_cmd.setCode(Code.OK);
					out_cmd.setData(name);
					break;
				case ROOM_INFO:
					room = (Room) in_cmd.getData();
					System.out.println(room.toString());
					break;
				case MSG:
					if(clientInterface != null)
						clientInterface.onMessage(sender.getName(), (String) in_cmd.getData());
					System.out.println(String.format("%s: %s", sender.getName(), (String) in_cmd.getData()));
					break;
				case NEW_PLAYER:
					Player p = (Player) in_cmd.getData();//创建玩家P如果me是空的就在房间创建1个人
					if(me == null)
						me = p;
					room.add(p);
					System.out.println("New player join: " + p.getName());
					if(clientInterface != null) {
						clientInterface.onPlayerEnter(p.getId());//P是玩家
					}
					break;
				case LEAVE:
					// The leave command is sent by server
					int playerId = (int)in_cmd.getData();
					Player player = room.findPlayerById(playerId);
					if(player == null) {
						// It has been kicked
						break;
					}
					room.remove(playerId);
					System.out.println("Player leave: " + sender.getName());
					if(clientInterface != null) {
						clientInterface.onPlayerLeave(player);
					}
					break;
				case TEAM_CHANGE:
					Position position = (Position)in_cmd.getData();
					room.movePlayer(sender.getId(), position.getType(), position.getIndex());
					if(clientInterface != null) {
						clientInterface.refreshRoom();
					}
					break;
				case READY:
					boolean isReady = (boolean)in_cmd.getData();
					room.setPlayerState(senderId, isReady);
					if(clientInterface != null) {
						clientInterface.onPlayerReadyStateChanged(senderId, isReady);
					}
					break;
				case SEED:
					long seed = (long)in_cmd.getData();
					if(clientInterface != null) {
						clientInterface.onSeed(seed);
					}
					break;
				case KICK:
					int kickedPlayerId = (int)in_cmd.getData();
					Player playerBeenKicked = room.findPlayerById(kickedPlayerId);
					room.remove(kickedPlayerId);
					if(clientInterface != null) { //如果接口不为空
						clientInterface.onPlayerKicked(playerBeenKicked);
					}
					break;
				case SELECT_ROLE:
					int roleId = (int)in_cmd.getData();
					room.synchronizePlayerRoleId(senderId, roleId);
					if(clientInterface != null) {
						clientInterface.refreshRoom();
					}
					break;
				case SET_OWNER:
					int newOwner = (int)in_cmd.getData();
					room.setOwner(newOwner);
					if(clientInterface != null) {
						clientInterface.onOwerReset(newOwner);
					}
					break;
				default:
					break;
				}
				if (!out_cmd.isNull()) {
					out.writeObject(out_cmd);
				}
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {

			}
		}

		if(clientInterface != null)
			clientInterface.workThreadExit();
	}

	public void sendMessage(String msg) {//发送消息通知服务器
		if (out == null)
			return;

		NetCommand command = new NetCommand(Code.MSG);
		command.setSender(me.getId());
		command.setData(msg);
		sendCommand(command);
	}
	
	public void sendRoleChanged(int role_id) {
		if(me.getRoleId() == role_id)
			return;
		
		sendCmdWithInt(Code.SELECT_ROLE, role_id);
	}

	public void sendReadyCmd(boolean isReady) {//这里是在线程中写了一个方法通知服务器启动游戏
		NetCommand command = new NetCommand(Code.READY); //命令
		command.setSender(me.getId());//发送者
		command.setData(isReady);
		sendCommand(command);
	}

	public Player getPlayer(Team.Type teamType, int index) {
		return room!=null?room.getPlayer(teamType, index):null;
	}

	public Player getPlayerById(int playerId) {
		return room!=null?room.findPlayerById(playerId):null;
	}
	
	public Player getMe() {
		return me;
	}
	
	public void sendKickCmd(Team.Type team, int index) { //踢人命令
		Player playerToKick = getPlayer(team, index);
		if(playerToKick == null)
			return;
		
		sendCmdWithInt(Code.KICK, playerToKick.getId());
	}
	
	public void sendTransistOwner(Team.Type team, int index) { //踢人命令
		Player p = getPlayer(team, index);
		if(p == null)
			return;
		
		sendCmdWithInt(Code.SET_OWNER, p.getId());
	}
	
	private void sendCmdWithInt(Code code, int data) {
		NetCommand command = new NetCommand(code);
		command.setSender(me.getId());
		command.setData(data);
		sendCommand(command);
	}
	
	public void changeTeam(Team.Type type, int index) {
		if (out == null)
			return;

		NetCommand command = new NetCommand(Code.TEAM_CHANGE);
		command.setSender(me.getId());
		
		Position position = new Position();//位置
		position.setType(type);
		position.setIndex(index);
		command.setData(position);//发送的数据
		sendCommand(command);
	}

	private void sendCommand(NetCommand netCommand) {
		try {
			out.writeObject(netCommand);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Room getRoom() {
		return room;
	}

	public synchronized void shutdown() {
		exit = true;
		try {
			if (in != null) {
				in.close();
				in = null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void movePlayer(Team.Type type, int index) {
		if(room==null || me==null)
			return;

		try {
			room.movePlayer(me.getId(), type, index);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		shutdown();
	}
	
	public int getMyId() {
		return me!=null?me.getId():-1;
	}

	public int getOwnerId() {
		return room!=null?room.getOwnerId():-1;
	}
	
	public Player getOwner() {
		return room!=null?room.getOwner():null;
	}
	
	public boolean imOwner() {
		return getMyId()==getOwnerId();
	}

}