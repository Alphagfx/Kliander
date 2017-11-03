package com.alphagfx.kliander.actors;

import com.alphagfx.kliander.enums.UserDataType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.ArrayList;

public class Weapon extends GameActor {

    private short bulletTypes = 1;
    protected Bullet.BulletType selectedBullet;

    //    Relative to the body point where bullets should be created
    private Vector2 shootPoint;
    private float shootAngle;

    ArrayList<Bullet> bullets = new ArrayList<>();

    private float spread;
    private int ammo;
    private float range;

    public Weapon(Body body, Vector2 shootPoint) {

        super(body);

        setUserDataType(UserDataType.WEAPON);

        setSelectedBullet(Bullet.BulletType.STANDARD);

        this.shootPoint = shootPoint;
//        this.shootAngle = body.getAngle();
    }

    public void fire(float angle) {
        Bullet bullet = new Bullet(body.getWorld(), selectedBullet, body.getWorldPoint(shootPoint), body.getAngle());
        bullets.add(bullet);
    }

    public Bullet.BulletType getSelectedBullet() {
        return selectedBullet;
    }

    public void setSelectedBullet(Bullet.BulletType selectedBullet) {
        this.selectedBullet = selectedBullet;
    }

}
