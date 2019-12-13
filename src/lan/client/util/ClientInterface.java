package lan.client.util;

import lan.utils.Player;
import lan.utils.Room;

public interface ClientInterface { //�ͻ��˽ӿ�
    void onMessage(String player, String msg); //��Ϣ
    void onSeed(long seed);//����
    void onPlayerKicked(Player player, Room room);//����
    void roomRefreshed(Room room);
}
