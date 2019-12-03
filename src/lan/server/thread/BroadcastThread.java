package lan.server.thread;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import lan.utils.Room;
import lan.utils.Utils;

public class BroadcastThread extends Thread {
	private Room room;//创建数据类为room的成员变量
	public BroadcastThread(Room room) {
		this.room = room;//将传递来的参数给成员变量room
	}

	private String getLocalIpAddress() {//获取本机的ip地址
		try {
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
					continue;
				} else {
					Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
					while (addresses.hasMoreElements()) {
						ip = addresses.nextElement();
						if (ip != null && ip instanceof Inet4Address) {
							return ip.getHostAddress();
						}
					}
				}
			}
		} catch (Exception e) {
			System.err.println("IP地址获取失败" + e.toString());
		}
		return "";
	}

	@Override
	public void run() {
		String localIp = getLocalIpAddress();//调用getLocalIpAddress来获取到本地的ip地址
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
				e.printStackTrace();///显示出程序从启动到异常的步骤
				exit = true;//将条件赋真结束循环
			}
		}
	}
}
