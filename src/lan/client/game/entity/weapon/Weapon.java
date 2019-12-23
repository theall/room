package lan.client.game.entity.weapon;

import lan.client.game.entity.bullet.Bullet;

import java.util.ArrayList;

public abstract class Weapon {
    private int bulletCount;
    private Bullet bullet;

    public abstract void fire();
}
