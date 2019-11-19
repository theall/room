package lan.client.thread;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import lan.client.Client;
import lan.client.util.RoomUpdate;
import lan.utils.Utils;

public class FindHostThread extends Thread { //寻找房间线程
	private RoomHeadList roomHeadList;
	private RoomUpdateThread roomUpdateThread;
	
	public FindHostThread() {
		roomHeadList = new RoomHeadList();
		roomUpdateThread = new RoomUpdateThread(roomHeadList);
	}

	public RoomHeadList getRooms() {
		return roomHeadList;//返回房间列表
	}

	public RoomHeadInfo getRoom(int index) {
		return roomHeadList.getRoom(index);//返回的是房间列表的索引号
	}

	@Override
	public void run() {
		int port = Utils.BROADCAST_PORT;// 开启监听的端口
		DatagramSocket ds = null;
		DatagramPacket dp = null;
		byte[] buf = new byte[1024];// 存储发来的消息
		roomUpdateThread.start();
		try {
			boolean exit = false;
			System.out.println("Receiving broadcast...");
			while (!exit) {
				// 绑定端口的
				ds = new DatagramSocket(port);
				dp = new DatagramPacket(buf, buf.length);
				ds.receive(dp);
				ds.close();

				StringBuffer sbuf = new StringBuffer();
				for (int i = 0; i < 1024; i++) {
					if (buf[i] == 0) {
						break;
					}
					sbuf.append((char) buf[i]);
				}
				String string = sbuf.toString();
				if (!string.startsWith(Utils.TOKEN))
					continue;
				String[] strings = string.split("\\|");
				if (strings.length != 6)
					continue;

				String host = strings[1];
				int p = Integer.parseInt(strings[2]);
				RoomHeadInfo roomHeadInfo = new RoomHeadInfo();
				roomHeadInfo.host = host;
				roomHeadInfo.port = p;
				roomHeadInfo.name = strings[3];
				roomHeadInfo.current = Integer.parseInt(strings[4]);
				roomHeadInfo.capacity = Integer.parseInt(strings[5]);
				roomHeadInfo.timestamp = System.currentTimeMillis();
				roomHeadList.add(roomHeadInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setRoomUpdate(RoomUpdate roomUpdate) {
		if(roomUpdateThread != null) //如果刷新线程不为空
			roomUpdateThread.setRoomUpdate(roomUpdate);//显示出来
	}
}
