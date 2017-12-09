package com.alphagfx.kliander.actors;

import com.alphagfx.kliander.enums.UserDataType;
import com.alphagfx.kliander.utils.WorldUtils;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.LinkedList;

public class Weapon extends GameActor {

    private short bulletTypes = 1;
    private Bullet.BulletType selectedBullet;

    //    Relative to the body point where bullets should be created
    private Vector2 shootPoint;
    private float shootAngle;

    private LinkedList<Bullet> bullets = new LinkedList<>();

    private float spread;
    private int ammo = 10;
    private float range;

    Weapon(Body body, Vector2 shootPoint) {

        super(body, 1, 1);

        setUserDataType(UserDataType.WEAPON);

        setSelectedBullet(Bullet.BulletType.STANDARD);

        this.shootPoint = shootPoint;
    }

    @Override
    protected void moveTo(Vector2 target) {

    }

    @Override
    protected void turnTo(float angle) {

    }

    @Override
    protected void fire(Vector2 target) {
        if (isDead()) {
            return;
        }
        Bullet bullet = new Bullet(body.getWorld(), selectedBullet, body.getWorldPoint(shootPoint), body.getAngle());
//        Gdx.app.log("weapon body", body == null ? "null" : "present");
//        Gdx.app.log("weapon angle", "" + body.getAngle());

        bullets.addFirst(bullet);

        if (bullets.size() > ammo) {
            bullets.removeLast().setDead(true);
        }

    }

    @Override
    protected void specialAction(String name, Object... objects) {
        //Place for reload action or switch bullets

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
