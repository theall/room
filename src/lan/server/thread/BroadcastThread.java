package lan.server.thread;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import lan.utils.Room;
import lan.utils.Utils;

public class BroadcastThread extends Thread {
	private Room room;//创建数据类为room的成员变量
	
	public BroadcastThread() {
		
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	@Override
	public void run() {
		String localIp = Utils.getLocalIpAddress();//调用getLocalIpAddress来获取到本地的ip地址
		boolean exit = false;//给一个假数值
		while (!exit) {
			// 广播的实现 :由客户端发出广播，服务器端接收
			String host = "255.255.255.255";// 广播地址，设定全ip段广播
			int port = Utils.BROADCAST_PORT;// 广播的目的端口，通过端口广播出去
			try {
				String message = String.format("%s|%s|%d|%s|%d|%d", Utils.TOKEN, localIp, Utils.WORK_PORT, room.getName(),//将本机的名字，本机的ip地址，对方需要连接的端口，现有玩家的名字
						room.getUsedSize(), room.getCapacity());// 已有玩家的人数，还有多少房间容量
				InetAddress addr = InetAddress.getByName(host);//将ip地址转码
				DatagramSocket ds = new DatagramSocket();//创建的一个Socket（一个不要建立链接就能发送数据）
				DatagramPacket dp = new DatagramPacket(message.getBytes(), message.length(), addr, port);//获得打包的格式，长度，本机ip，广播端口
				ds.send(dp);//发送（将一个包给到Socket发送）
				sleep(1000);//设置延时，每秒广播
				ds.close();//关闭传输
			} catch (Exception e) {//异常处理
				System.out.println("Broadcast thread closed.");
				exit = true;//将条件赋真结束循环
			}
		}
	}
}
