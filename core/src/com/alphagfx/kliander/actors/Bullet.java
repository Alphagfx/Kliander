package com.alphagfx.kliander.actors;

import com.alphagfx.kliander.box2d.IBodyUserData;
import com.alphagfx.kliander.enums.UserDataType;
import com.alphagfx.kliander.utils.WorldUtils;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class Bullet implements IBodyUserData {

    public enum BulletType {
        STANDARD,
        MELEE
    }

    UserDataType userDataType;

    protected Body body;
    protected BulletType bulletType;
    protected float speed;

    public Bullet(World world, BulletType type, Vector2 position, float angle) {

        setBody(WorldUtils.createBullet(world, position));

        setUserDataType(UserDataType.BULLET);

        setBulletType(BulletType.STANDARD);
        setSpeed(20);

        launch(angle);
    }

    public void launch(float angle) {

        body.applyLinearImpulse(new Vector2(-MathUtils.sin(angle), MathUtils.cos(angle)).scl(speed), body.getWorldCenter(), true);
    }

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public void setBody(Body body) {
        body.setUserData(this);
        this.body = body;
    }

    @Override
    public UserDataType getUserDataType() {
        return userDataType;
    }

    @Override
    public void setUserDataType(UserDataType userDataType) {
        this.userDataType = userDataType;
    }

    public BulletType getBulletType() {
        return bulletType;
    }

    public void setBulletType(BulletType bulletType) {
        this.bulletType = bulletType;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}


