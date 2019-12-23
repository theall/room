package lan.client.game.entity;

import lan.client.game.entity.weapon.Missile;
import lan.client.game.entity.weapon.Weapon;
import lan.client.game.sprite.Creature;

import java.awt.*;

public class Tank extends Creature {
    private int sp;
    private Weapon majorWeapon;
    private Weapon minorWeapon;
    private Missile missile;

    public Tank() {

    }

    @Override
    public void step() {//²½½ø
        super.step();

        if(getBindButton() == null) {
            int stepCount = getStepCount();
            if(stepCount%90 == 0) {
                generateRandomDir();
            }
        }
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);
    }
}
