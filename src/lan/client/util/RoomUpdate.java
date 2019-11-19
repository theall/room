package lan.client.util;

import lan.client.thread.RoomHeadList;

public interface RoomUpdate {  //接口用与对接上下级消息
    public void updated(RoomHeadList roomHeadList);
}
