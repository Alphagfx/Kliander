package com.alphagfx.kliander.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.ArrayList;

public class Weapon {

    private Body body;
    private short bulletTypes = 1;
    private Bullet.BulletType selectedBullet = null;

    //    Relative to the body point where bullets should be created
    private Vector2 shootPoint;
    private float shootAngle;

    ArrayList<Bullet> bullets = new ArrayList<>();

    private float spread;
    private int ammo;
    private float range;

    public Weapon(Body body, Vector2 shootPoint) {

        this.body = body;
        this.selectedBullet = Bullet.BulletType.STANDARD;
        this.shootPoint = shootPoint;
//        this.shootAngle = body.getAngle();
    }

    public void fire(float angle) {
        Bullet bullet = new Bullet(body.getWorld(), selectedBullet, body.getWorldPoint(shootPoint), body.getAngle());
        bullets.add(bullet);
    }

    public Body getBody() {
        return body;
    }
}
