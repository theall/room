package lan.client.thread;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import lan.client.Client;
import lan.client.util.RoomUpdate;
import lan.utils.Utils;

public class FindHostThread extends Thread { //Ѱ�ҷ����߳�
	private RoomHeadList roomHeadList;
	private RoomUpdateThread roomUpdateThread;
	
	public FindHostThread() {//��������
		roomHeadList = new RoomHeadList();//���������б����
		roomUpdateThread = new RoomUpdateThread(roomHeadList); 
	}

	public RoomHeadList getRooms() {
		return roomHeadList;//���ط����б�
	}

	public RoomHeadInfo getRoom(int index) {
		return roomHeadList.getRoom(index);//���ص��Ƿ����б��������
	}

	@Override
	public void run() {
		int port = Utils.BROADCAST_PORT;// ���������Ķ˿�
		DatagramSocket ds = null;
		DatagramPacket dp = null;
		byte[] buf = new byte[1024];// �����洢��������Ϣ
		roomUpdateThread.start();//���������б��߳�
		try {
			boolean exit = false;//������ֵ
			System.out.println("Receiving broadcast...");//���ڽ��չ㲥...
			while (!exit) {//����ѭ��
				// �󶨶˿ڵ�
				ds = new DatagramSocket(port);//��ͨ�Ŷ˿ڸ���ds
				dp = new DatagramPacket(buf, buf.length);//�����洢���ݵ����飬������ĳ���
				ds.receive(dp);//���շ���˷��͹���������
				ds.close();//�ر�ds���ͷ���Դ

				StringBuffer sbuf = new StringBuffer();//����StringBuffer���󣬷���������ĺʹ����������������
				for (int i = 0; i < 1024; i++) {
					if (buf[i] == 0) {//��������˷�����������
						break;//���Ϊ�վͷ���
					}
					sbuf.append((char) buf[i]);//�ѽ��ܵ�������ת��������ַ�����
				}
				String string = sbuf.toString();//����������ת���ַ�������
				if (!string.startsWith(Utils.TOKEN))//����ַ����Ƿ��ָ����ǰ׺��ʼ
					continue;//������Σ����������½���ѭ������
				String[] strings = string.split("\\|");
				if (strings.length != 6)//�жϴ���������ݳ����ǲ��Ǻϸ�
					continue;//������Σ����������½���ѭ������

				String host = strings[1];//����������
				int p = Integer.parseInt(strings[2]);//������ip��ַ
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
			e.printStackTrace();//�쳣����
		}
	}

	public void setRoomUpdate(RoomUpdate roomUpdate) {
		if(roomUpdateThread != null) //���ˢ���̲߳�Ϊ��
			roomUpdateThread.setRoomUpdate(roomUpdate);//��ʾ����
	}
}
