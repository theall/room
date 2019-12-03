package lan.client.util;

import lan.utils.Room;

public interface ClientInterface { //客户端接口
    void onMessage(String player, String msg); //消息
    void onSeed(long seed);
    public void roomRefreshed(Room room);
}
