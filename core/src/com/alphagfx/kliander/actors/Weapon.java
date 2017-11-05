package com.alphagfx.kliander.actors;

import com.alphagfx.kliander.enums.UserDataType;
import com.alphagfx.kliander.utils.WorldUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.LinkedList;

public class Weapon extends GameActor {

    private short bulletTypes = 1;
    protected Bullet.BulletType selectedBullet;

    //    Relative to the body point where bullets should be created
    private Vector2 shootPoint;
    private float shootAngle;

    LinkedList<Bullet> bullets = new LinkedList<>();

    private float spread;
    private int ammo = 10;
    private float range;

    public Weapon(Body body, Vector2 shootPoint) {

        super(body, 0.2f * 2, 1 * 2);

        setUserDataType(UserDataType.WEAPON);

        setSelectedBullet(Bullet.BulletType.STANDARD);

        this.shootPoint = shootPoint;
    }

    public void fire(float angle) {
        if (isDead()) {
            return;
        }
        Bullet bullet = new Bullet(body.getWorld(), selectedBullet, body.getWorldPoint(shootPoint), body.getAngle());
        Gdx.app.log("weapon body", body == null ? "null" : "present");

        bullets.addFirst(bullet);

        if (bullets.size() > ammo) {
            bullets.removeLast().setDead(true);
        }

    }

    public Bullet.BulletType getSelectedBulletType() {
        return selectedBullet;
    }

    public void setSelectedBullet(Bullet.BulletType selectedBullet) {
        this.selectedBullet = selectedBullet;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (body.isAwake()) {

            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            setRotation(WorldUtils.transformAngle(body.getAngle()) * MathUtils.radiansToDegrees);
        }
    }
}
