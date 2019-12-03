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
	private RoomHeadInfo roomHeadInfo; //�����б���Ϣ
	private Room room;
	private ObjectOutputStream out; //���������
	private Player player;//���
	private String name;//�������
	private ObjectInputStream in;//����
	private Socket socket;//�۲���
	private ClientInterface clientInterface;//�ͻ��˽ӿ�
	public WorkThread(RoomHeadInfo room, String name) { //�����߳�
		roomHeadInfo = room;
		this.name = name;
	}

	public void setClientInterface(ClientInterface clientInterface) { //�ͻ��˽ӿڲ���
		this.clientInterface = clientInterface; //�����ӿ�
	}

	@Override
	public void run() {
		try {
			// �����ͻ���Socket���ӣ�ָ����������λ���Լ��˿�
			InetAddress address = InetAddress.getByName(roomHeadInfo.host);
			socket = new Socket(address, roomHeadInfo.port);

			out = new ObjectOutputStream(socket.getOutputStream()); //OBject�Ƕ��󣬶��������
			in = new ObjectInputStream(socket.getInputStream()); //����������

			boolean  exit = false; //����Ϊ��
			NetCommand in_cmd; //����ָ�ӹ�
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
						clientInterface.onMessage(in_cmd.getSenderName(), (String) in_cmd.getData());
					System.out.println(String.format("%s: %s", in_cmd.getSenderName(), (String) in_cmd.getData()));
					break;
				case NEW_PLAYER:
					Player p = (Player) in_cmd.getData();
					if (player == null)
						player = p;
					room.add(p);
					System.out.println("New player join: " + p.getName());
					if(clientInterface != null) {
						clientInterface.roomRefreshed(room);
					}
					break;
				case LEAVE:
					room.remove(player.getId());
					System.out.println("Player leave: " + player.getName());
					if(clientInterface != null) {
						clientInterface.roomRefreshed(room);
					}
					break;

				case TEAM_CHANGE:
					Player sender = in_cmd.getSender();
					Position position = (Position)in_cmd.getData();
					room.movePlayer(sender.getId(), position.getType(), position.getIndex());
					System.out.println(String.format("Player [%s] change team to [%s] position [%d]", sender.getName(),
							position.getType().toString(), position.getIndex()));
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
				default:
					break;
				}
				if (!out_cmd.isNull()) {
					out.writeObject(out_cmd);
				}
			}
			// �ر���Դ
			in.close();
			out.close();
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

	public void sendMessage(String msg) throws IOException {
		if (out == null)
			return;

		NetCommand command = new NetCommand(Code.MSG);
		command.setSender(player);
		command.setData(msg);
		out.writeObject(command);
	}

	public boolean sendSeed() {
		boolean ret = false;
		long seed = System.currentTimeMillis();//��ȡһ������
		NetCommand command = new NetCommand(Code.SEED);
		command.setSender(player);
		command.setData(seed);
		try {
			out.writeObject(command);//д�����
			ret = true;
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		return ret;
	}
	
	public void changeTeam(Team.Type type, int index) throws IOException {
		if (out == null)
			return;

		NetCommand command = new NetCommand(Code.TEAM_CHANGE);
		command.setSender(player);
		
		Position position = new Position();
		position.setType(type);
		position.setIndex(index);
		command.setData(position);
		out.writeObject(command);
	}

	public Room getRoom() {
		return room;
	}

	public void shutdown() throws IOException {
		interrupt();

		if (in != null) {
			in.close();
			in = null;
		}
		if (out != null) {
			out.close();
			out = null;
		}
		if (socket != null) {
			socket.close();
			socket = null;
		}
	}

	public void movePlayer(Team.Type type, int index) {
		if(room==null || player==null)
			return;

		try {
			room.movePlayer(player.getId(), type, index);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}