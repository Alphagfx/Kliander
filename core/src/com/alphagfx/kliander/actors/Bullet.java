package com.alphagfx.kliander.actors;

import com.alphagfx.kliander.utils.WorldUtils;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class Bullet {

    public enum BulletType {
        STANDARD,
        MELEE
    }

    private Body body;
    private BulletType type;
    private float speed;
    private float mass;

    public Bullet(World world, BulletType type, Vector2 position, float angle) {
        this.body = WorldUtils.createBullet(world, position);
        this.type = type;
        this.speed = 20;
        body.applyLinearImpulse(new Vector2(-MathUtils.sin(angle), MathUtils.cos(angle)).scl(speed), body.getWorldCenter(), true);
    }
}


