package lan.client.game;

public class MyThread extends Thread {
    @Override
    public void run() { //����дrun����
        DeomFrame panel =new DeomFrame();//��Ϊ�޷���frame��ֱ���˳����Դ�������
        while (true) {
            try {
                Thread.sleep(5);//������������ˢ��
            } catch (InterruptedException e) {
                e.printStackTrace();
            }   panel.repaint();//ˢ��

        }
    }

}
