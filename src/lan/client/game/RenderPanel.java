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

    public void paint(Graphics g) { // paint绘制函数
        super.paint(g); // 调用父类函数

        game.render(g);
        g.drawString("FPS:"+fps, 0, 20);//调用坦克的对象在面板绘制FPS
    }

    public void setFps(int fps) {
        this.fps = fps;
    }
}
