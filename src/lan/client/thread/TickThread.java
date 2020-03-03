package lan.client.thread;

import java.io.IOException;
import java.net.InetAddress;

public class TickThread extends Thread { //检测玩家延迟线程
	private boolean exit;
	private RoomHeadInfo roomHeadInfo;
	private int timeOut;
	private boolean status;

	public TickThread() {
		// TODO 自动生成的构造函数存根
		this.exit = false;//给一个假数值
		this.timeOut = 3000;//超时应该在3钞以上        
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
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
	
	public boolean pingCmd(String ipAddress) {
		try {
			status = InetAddress.getByName(ipAddress).isReachable(timeOut);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return status;
	}
	
}
