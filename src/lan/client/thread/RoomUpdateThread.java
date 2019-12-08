package lan.client.thread;

import lan.client.util.RoomUpdate;

public class RoomUpdateThread extends Thread { //ˢ�·����б��߳�
	private RoomHeadList roomHeadList; //���������б����
	private int checkInterval;//������
	private RoomUpdate roomUpdate;//������ˢ���̱߳���
	public RoomUpdateThread(RoomHeadList roomHeadList) {
		checkInterval = 500; //������5��ˢ��1���б�
		this.roomHeadList = roomHeadList;
	}
	
	public int getCheckInterval() {
		return checkInterval;
	}

	public void setCheckInterval(int checkInterval) {
		this.checkInterval = checkInterval;
	}
	
	@Override
	public void run() { //��������д��ˢ���̵߳ķ���
		if(roomHeadList == null)	//��������б�Ϊ��
			return;				//�ͷ��ؿ�
		while(true) {   		//�����
			roomHeadList.checkTimeOut();
			if(roomUpdate != null)			//���ˢ���б�Ϊ��
				roomUpdate.updated(roomHeadList); //��ʵ�ֽӿ�ˢ�ߵ������б�
			try {
				sleep(checkInterval); //��ʱ
			} catch (InterruptedException e) { //�ж��쳣
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setRoomUpdate(RoomUpdate roomUpdate) { //��Ա��������������
		this.roomUpdate = roomUpdate; //����ˢ��
	}
}
