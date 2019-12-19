package lan.client.util;

import lan.utils.Player;

public interface ClientInterface { //客户端接口
    void onMessage(String player, String msg); //消息
    void onSeed(long seed);//种子
    void onPlayerKicked(Player player);//踢人
    void onOwerReset(int newOwner);//房主转移了
    void refreshRoom();
    void onPlayerEnter(int playerId);//玩家进入接口
    void onPlayerLeave(Player player);
    void onPlayerReadyStateChanged(int playerId, boolean isReady);
    void workThreadExit();
}
