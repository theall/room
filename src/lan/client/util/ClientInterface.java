package lan.client.util;

import lan.utils.Room;

public interface ClientInterface { //�ͻ��˽ӿ�
    void onMessage(String player, String msg); //��Ϣ
    void onSeed(long seed);
    void onOut(long out);//����
    public void roomRefreshed(Room room);
}
