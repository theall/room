package lan.client.util;

import lan.utils.Room;

public interface ClientInterface { //�ͻ��˽ӿ�
    void onMessage(String player, String msg); //��Ϣ
    public void roomRefreshed(Room room);
}
