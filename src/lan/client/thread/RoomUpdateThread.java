package lan.client.thread;

import lan.client.util.RoomUpdate;

public class RoomUpdateThread extends Thread { //刷新房间列表线程
	private RoomHeadList roomHeadList; //创建房间列表对象
	private int checkInterval;//检验室
	private RoomUpdate roomUpdate;//定义了刷线线程变量
	public RoomUpdateThread(RoomHeadList roomHeadList) {
		checkInterval = 500; //这里是5秒刷新1次列表
		this.roomHeadList = roomHeadList;
	}
	
	public int getCheckInterval() {
		return checkInterval;
	}

	public void setCheckInterval(int checkInterval) {
		this.checkInterval = checkInterval;
	}
	
	@Override
	public void run() { //这里是重写了刷新线程的方法
		if(roomHeadList == null)	//如果房间列表为空
			return;				//就返回空
		while(true) {   		//如果有
			roomHeadList.checkTimeOut();
			if(roomUpdate != null)			//如果刷新列表不为空
				roomUpdate.updated(roomHeadList); //就实现接口刷线到房间列表
			try {
				sleep(checkInterval); //超时
			} catch (InterruptedException e) { //中断异常
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setRoomUpdate(RoomUpdate roomUpdate) { //成员方法给俩个参数
		this.roomUpdate = roomUpdate; //自我刷新
	}
}
