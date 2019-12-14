package lan.server.thread;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import lan.utils.Room;
import lan.utils.Utils;

public class BroadcastThread extends Thread {
	private Room room;//����������Ϊroom�ĳ�Ա����
	
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
		String localIp = Utils.getLocalIpAddress();//����getLocalIpAddress����ȡ�����ص�ip��ַ
		boolean exit = false;//��һ������ֵ
		while (!exit) {
			// �㲥��ʵ�� :�ɿͻ��˷����㲥���������˽���
			String host = "255.255.255.255";// �㲥��ַ���趨ȫip�ι㲥
			int port = Utils.BROADCAST_PORT;// �㲥��Ŀ�Ķ˿ڣ�ͨ���˿ڹ㲥��ȥ
			try {
				String message = String.format("%s|%s|%d|%s|%d|%d", Utils.TOKEN, localIp, Utils.WORK_PORT, room.getName(),//�����������֣�������ip��ַ���Է���Ҫ���ӵĶ˿ڣ�������ҵ�����
						room.getUsedSize(), room.getCapacity());// ������ҵ����������ж��ٷ�������
				InetAddress addr = InetAddress.getByName(host);//��ip��ַת��
				DatagramSocket ds = new DatagramSocket();//������һ��Socket��һ����Ҫ�������Ӿ��ܷ������ݣ�
				DatagramPacket dp = new DatagramPacket(message.getBytes(), message.length(), addr, port);//��ô���ĸ�ʽ�����ȣ�����ip���㲥�˿�
				ds.send(dp);//���ͣ���һ��������Socket���ͣ�
				sleep(1000);//������ʱ��ÿ��㲥
				ds.close();//�رմ���
			} catch (Exception e) {//�쳣����
				System.out.println("Broadcast thread closed.");
				exit = true;//�������������ѭ��
			}
		}
	}
}
