package lan.client.thread;

import java.io.IOException;
import java.net.InetAddress;

public class TickThread extends Thread { //�������ӳ��߳�
	private boolean exit;
	private RoomHeadInfo roomHeadInfo;
	private int timeOut;
	private boolean status;

	public TickThread() {
		// TODO �Զ����ɵĹ��캯�����
		this.exit = false;//��һ������ֵ
		this.timeOut = 3000;//��ʱӦ����3������        
		this.status = false;
	}
	 
	public void setIpAddress(RoomHeadInfo roomHeadInfo) {
		this.roomHeadInfo = roomHeadInfo;
	}

	public void run() {
		while (!exit) {
			try { 
				long timeOne = System.currentTimeMillis();
				boolean trueOrFalse = pingCmd(roomHeadInfo.host);
				if(trueOrFalse == true) {
					long timeTwo = System.currentTimeMillis();
					roomHeadInfo.ping = (int)(timeTwo - timeOne)/2;
					System.out.println(roomHeadInfo.name + "ping="+ ((timeTwo - timeOne)/2));
					sleep(1000);
				}else{
					exit = true;
				}
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
	}
	
	public boolean pingCmd(String ipAddress) {
		try {
			status = InetAddress.getByName(ipAddress).isReachable(timeOut);
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		return status;
	}
	
}
