package lan.client.util;

import lan.utils.Player;
import lan.utils.Room;

public interface ClientInterface { //客户端接口
    void onMessage(String player, String msg); //消息
    void onSeed(long seed);//种子
    void onPlayerKicked(Player player, Room room);//踢人
    void roomRefreshed(Room room);
}
