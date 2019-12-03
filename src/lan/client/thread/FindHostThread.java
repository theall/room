package lan.client.thread;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import lan.client.Client;
import lan.client.util.RoomUpdate;
import lan.utils.Utils;

public class FindHostThread extends Thread { //寻找房间线程
	private RoomHeadList roomHeadList;
	private RoomUpdateThread roomUpdateThread;
	
	public FindHostThread() {//查找主机
		roomHeadList = new RoomHeadList();//创建房间列表对象
		roomUpdateThread = new RoomUpdateThread(roomHeadList); 
	}

	public RoomHeadList getRooms() {
		return roomHeadList;//返回房间列表
	}

	public RoomHeadInfo getRoom(int index) {
		return roomHeadList.getRoom(index);//返回的是房间列表的索引号
	}

	@Override
	public void run() {
		int port = Utils.BROADCAST_PORT;// 开启监听的端口
		DatagramSocket ds = null;
		DatagramPacket dp = null;
		byte[] buf = new byte[1024];// 用来存储发来的消息
		roomUpdateThread.start();//启动房间列表线程
		try {
			boolean exit = false;//给假数值
			System.out.println("Receiving broadcast...");//正在接收广播...
			while (!exit) {//无限循环
				// 绑定端口的
				ds = new DatagramSocket(port);//将通信端口给到ds
				dp = new DatagramPacket(buf, buf.length);//用来存储数据的数组，和数组的长度
				ds.receive(dp);//接收服务端发送过来的数据
				ds.close();//关闭ds，释放资源

				StringBuffer sbuf = new StringBuffer();//定义StringBuffer对象，方便后续更改和处理服务器发过来的
				for (int i = 0; i < 1024; i++) {
					if (buf[i] == 0) {//遍历服务端发过来的数据
						break;//如果为空就返回
					}
					sbuf.append((char) buf[i]);//把接受到的数据转到定义的字符串内
				}
				String string = sbuf.toString();//把所有数据转到字符串类型
				if (!string.startsWith(Utils.TOKEN))//检测字符串是否从指定的前缀开始
					continue;//舍弃这次，将函数重新交回循环手上
				String[] strings = string.split("\\|");
				if (strings.length != 6)//判断传输过来数据长度是不是合格
					continue;//舍弃这次，将函数重新交回循环手上

				String host = strings[1];//服务器名字
				int p = Integer.parseInt(strings[2]);//服务器ip地址
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
			e.printStackTrace();//异常处理
		}
	}

	public void setRoomUpdate(RoomUpdate roomUpdate) {
		if(roomUpdateThread != null) //如果刷新线程不为空
			roomUpdateThread.setRoomUpdate(roomUpdate);//显示出来
	}
}
