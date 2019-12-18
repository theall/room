package lan.client.util;

import lan.utils.Player;
import lan.utils.Room;

public interface ClientInterface { //�ͻ��˽ӿ�
    void onMessage(String player, String msg); //��Ϣ
    void onSeed(long seed);//����
    void onPlayerKicked(Player player, Room room);//����
    void onOwerReset(int newOwner, Room room);//����ת����
    void roomRefreshed(Room room);
    void onPlayerEnter(Player player, Room room);//��ҽ���ӿ�
    void onPlayerReadyStateChanged(int playerId, boolean isReady);
}
