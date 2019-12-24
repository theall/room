package lan.client.game;

import javax.swing.*;
import java.awt.*;

public class RenderPanel extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Game game;
    private int fps;

    public RenderPanel(Game game) {
        this.game = game;
    }

    public void paint(Graphics g) { // paint���ƺ���
        super.paint(g); // ���ø��ຯ��

        game.render(g);
        g.drawString("FPS:"+fps, 0, 20);//����̹�˵Ķ�����������FPS
    }

    public void setFps(int fps) {
        this.fps = fps;
    }
}
