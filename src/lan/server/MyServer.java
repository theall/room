package lan.server;

public class MyServer { //服务器

	public static void main(String[] args) {
		Server server = new Server();//创建Server的对象出来
		server.start();//调用Server的start方法
	}
}
