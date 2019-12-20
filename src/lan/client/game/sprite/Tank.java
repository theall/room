package lan.client.game.sprite;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Tank extends Sprite {
    private Action[] actions = new Action[4];//4��ͼ
    private int currentIndex;
    private Action currentAction;
    private static int DURATION = 10;//����ʱ��
    private int x, y;// x  y  ������tank���ڵ�λ��
    private int oldX, oldY;//��¼̹����һ����λ��
    private static final int WIDTH = 50, HEIGHT = 50;//̹�˴�С
    private static final int XSPEED = 5, YSPEED = 5;//̹��xy�����˶��ٶ�
    private boolean good;
    private boolean live = true;
    private Random random;
    private int life = 100;//���ҷ�̹������Ѫ��
    private float speed = 1f;
    private int stepCount = 0;
    private Dir dir;

    public Dir getDir() {
        return dir;
    }
    public void setDir(Dir dir) {
        this.dir = dir;
        switch (dir) {
            case UP:
                currentAction = actions[3];//��ǰͼƬ
                setSpeed(0, -speed);//����
                break;
            case DOWN:
                currentAction = actions[0];
                setSpeed(0, speed);
                break;
            case LEFT:
                currentAction = actions[1];
                setSpeed(-speed, 0);
                break;
            case RIGHT:
                currentAction = actions[2];
                setSpeed(speed, 0);
                break;
        }
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void setGood(boolean good) {
        this.good = good;
    }

    public boolean isGood() {
        return good;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public Tank() {

    }

    public Tank(int x, int y, boolean good) {
        this.x = x;
        this.y = y;
        this.good = good;
        setDir(Dir.LEFT);
    }

    public void generateRandomDir() {
        Random random = new Random();
        int i = random.nextInt(Dir.values().length);
        Dir dir = Dir.values()[i];
        setDir(dir);
    }

    public void load(BufferedImage image) {//��ͼ
        int chunkWidth = image.getWidth();
        int chunkHeight = image.getHeight() / 4;
        int count = 0;
        for (int x = 0; x < 4; x++) {
            //����Сͼ�Ĵ�С������
            BufferedImage bufferedImage = new BufferedImage(chunkWidth, chunkHeight, image.getType());

            //д��ͼ������
            Graphics2D gr = bufferedImage.createGraphics();
            gr.drawImage(image, 0, 0,
                    chunkWidth, chunkHeight,
                    0, chunkHeight * x,
                    chunkWidth,
                    chunkHeight * (x + 1), null); //����ͼƬ��xy���
            gr.dispose();

            Action action = new Action();
            action.load(bufferedImage);
            actions[x] = action;
        }
        if (currentAction == null)
            currentAction = actions[2];
    }

    @Override
    public void step() {//����
        super.step();
        currentAction.step();
        Image image = currentAction.getCurrentImage();
        setImage(image);

        stepCount++;
        if(stepCount%90 == 0) {
            generateRandomDir();
        }
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);
    }
}
