package lan.client.thread;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import lan.client.Client;
import lan.utils.Utils;

public class FindHostThread extends Thread {
	private RoomHeadList roomHeadList;

	public FindHostThread(Client c) {
		roomHeadList = new RoomHeadList();
	}

	public RoomHeadList getRooms() {
		return roomHeadList;
	}

	@Override
	public void run() {
		int port = Utils.BROADCAST_PORT;// 开启监听的端口
		DatagramSocket ds = null;
		DatagramPacket dp = null;
		byte[] buf = new byte[1024];// 存储发来的消息
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
}
