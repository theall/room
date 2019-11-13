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
	private Room room;
	public BroadcastThread(Room room) {
		this.room = room;
	}

	private String getLocalIpAddress() {
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
			System.err.println("IP��ַ��ȡʧ��" + e.toString());
		}
		return "";
	}

	@Override
	public void run() {
		String localIp = getLocalIpAddress();
		boolean exit = false;
		while (!exit) {
			// �㲥��ʵ�� :�ɿͻ��˷����㲥���������˽���
			String host = "255.255.255.255";// �㲥��ַ
			int port = Utils.BROADCAST_PORT;// �㲥��Ŀ�Ķ˿�
			try {
				String message = String.format("%s|%s|%d|%s|%d|%d", Utils.TOKEN, localIp, Utils.WORK_PORT, room.getName(),
						room.getUsedSize(), room.getCapacity());// ���ڷ��͵��ַ���
				InetAddress addr = InetAddress.getByName(host);
				DatagramSocket ds = new DatagramSocket();
				DatagramPacket dp = new DatagramPacket(message.getBytes(), message.length(), addr, port);
				ds.send(dp);
				sleep(1000);
				ds.close();
			} catch (Exception e) {
				e.printStackTrace();
				exit = true;
			}
		}
	}
}
