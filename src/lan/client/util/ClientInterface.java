package lan.client.util;

import lan.utils.Player;

public interface ClientInterface { //�ͻ��˽ӿ�
    void onMessage(String player, String msg); //��Ϣ
    void onSeed(long seed);//����
    void onPlayerKicked(Player player);//����
    void onOwerReset(int newOwner);//����ת����
    void refreshRoom();
    void onPlayerEnter(int playerId);//��ҽ���ӿ�
    void onPlayerLeave(Player player);
    void onPlayerReadyStateChanged(int playerId, boolean isReady);
    void workThreadExit();
}
