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

public class WorkThread extends Thread { //�����߳�
	private String remoteHost;// Server host
	private int port;
	private Room room;
	private ObjectOutputStream out; //���������
	private ObjectInputStream in;//����
	private Player me;//���
	private String name;//�������
	private Socket socket;//�۲���
	private ClientInterface clientInterface;//�ͻ��˽ӿ�
	private boolean exit;
	public WorkThread(String host, int port, String name) { //�����߳�
		remoteHost = host;
		this.port = port;
		this.name = name;
		this.exit = false;
	}

	public void setClientInterface(ClientInterface clientInterface) { //�ͻ��˽ӿڲ���
		this.clientInterface = clientInterface; //�����ӿ�
	}

	@Override
	public void run() {
		try {
			// �����ͻ���Socket���ӣ�ָ����������λ���Լ��˿�
			InetAddress address = InetAddress.getByName(remoteHost);
			socket = new Socket(address, port);

			out = new ObjectOutputStream(socket.getOutputStream()); //OBject�Ƕ��󣬶��������
			in = new ObjectInputStream(socket.getInputStream()); //����������

			NetCommand in_cmd;
			while (!exit) {
				try {
					in_cmd = (NetCommand) in.readObject(); //������
				} catch (SocketException se) {
					System.out.println("Server shutdown, exit!");
					break;
				} catch (IOException e) {
					System.out.println("Io error, exit!");
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
					Player p = (Player) in_cmd.getData();
					if(me == null)
						me= p;
					room.add(p);
					System.out.println("New player join: " + p.getName());
					if(clientInterface != null) {
						clientInterface.roomRefreshed(room);
					}
					break;
				case LEAVE:
					room.remove(sender.getId());
					System.out.println("Player leave: " + sender.getName());
					if(clientInterface != null) {
						clientInterface.roomRefreshed(room);
					}
					break;
				case TEAM_CHANGE:
					Position position = (Position)in_cmd.getData();
					room.movePlayer(sender.getId(), position.getType(), position.getIndex());
					if(clientInterface != null) {
						clientInterface.roomRefreshed(room);
					}
					break;
				case SEED:
					long seed = (long)in_cmd.getData();
					if(clientInterface != null) {
						clientInterface.onSeed(seed);
					}
					break;
				case KICK: //��Ӧ�ӿ�����
					int kickedPlayerId = (int)in_cmd.getData();
					room.remove(kickedPlayerId);
					if(clientInterface != null) { //����ӿڲ�Ϊ��
						clientInterface.onPlayerKicked(me, room);
					}
					break;
				case SELECT_ROLE:
					int roleId = (int)in_cmd.getData();
					room.synchronizePlayerRoleId(senderId, roleId);
					if(clientInterface != null) {
						clientInterface.roomRefreshed(room);
					}
					break;
				default:
					break;
				}
				if (!out_cmd.isNull()) {
					out.writeObject(out_cmd);
				}
			}
			// �ر���Դ
			//in.close();
			//out.close();
			socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendMessage(String msg) throws IOException {//������Ϣ֪ͨ������
		if (out == null)
			return;

		NetCommand command = new NetCommand(Code.MSG);
		command.setSender(me.getId());
		command.setData(msg);
		out.writeObject(command);
	}
	
	public void sendRoleChanged(int role_id) {
		if(me.getRoleId() == role_id)
			return;
		
		NetCommand command = new NetCommand(Code.SELECT_ROLE);
		me.setRoleId(role_id);
		command.setSender(me.getId());
		command.setData(role_id);
		try {
			out.writeObject(command);
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}//д�����
	}

	public boolean sendSeed() {//���������߳���д��һ������֪ͨ������������Ϸ
		boolean ret = false;
		long seed = System.currentTimeMillis();//��ȡһ������
		NetCommand command = new NetCommand(Code.SEED); //����
		command.setSender(me.getId());//������
		command.setData(seed);//����
		try {
			out.writeObject(command);//д�����
			ret = true;
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		return ret;
	}

	public Player getPlayer(Team.Type teamType, int index) {
		return room.getPlayer(teamType, index);
	}

	public void sendKickCmd(Team.Type team, int index) { //��������
		Player playerToKick = getPlayer(team, index);
		if(playerToKick == null)
			return;
		
		NetCommand command = new NetCommand(Code.KICK);
		command.setSender(me.getId());
		command.setData(playerToKick.getId());
		try {
			out.writeObject(command);//д�����
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
	
	public void changeTeam(Team.Type type, int index) {
		if (out == null)
			return;

		NetCommand command = new NetCommand(Code.TEAM_CHANGE);
		command.setSender(me.getId());
		
		Position position = new Position();//λ��
		position.setType(type);
		position.setIndex(index);
		command.setData(position);//���͵�����
		try {
			out.writeObject(command);
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
		return me.getId();
	}
}