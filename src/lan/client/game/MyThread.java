package lan.client.game;

public class MyThread extends Thread {
    @Override
    public void run() { //从重写run方法
        DeomFrame panel =new DeomFrame();//因为无法在frame中直接退出所以创建对象
        while (true) {
            try {
                Thread.sleep(5);//这里利用休眠刷新
            } catch (InterruptedException e) {
                e.printStackTrace();
            }   panel.repaint();//刷新

        }
    }

}
