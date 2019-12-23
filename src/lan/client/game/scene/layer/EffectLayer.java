package lan.client.game.scene.layer;

import lan.client.game.sprite.Effect;

import java.awt.*;
import java.util.ArrayList;

/**
 * 特效层，如炮弹爆炸效果
 */
public class EffectLayer extends Layer {
    private ArrayList<Effect> effectArrayList;

    public  EffectLayer() {
        effectArrayList = new ArrayList<>();
    }

    @Override
    public void step() {
        for(Effect effect : effectArrayList) {
            effect.step();
        }
    }

    @Override
    public void render(Graphics g) {
        for(Effect effect : effectArrayList) {
            effect.render(g);
        }
    }
}
