package com.alphagfx.kliander.actors;

import com.alphagfx.kliander.utils.WorldUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

public class Fighter extends Creature {

    private float health;
    private Weapon weapon;
    private DistanceJoint distJoint;
    private WeldJoint weldJoint;
    private Vector2 target;

    public Fighter(Body body, Weapon weapon) {
        super(body);

//        setBody(body);

        this.weapon = weapon;
        health = 100;

        WeldJointDef weldJointDef = new WeldJointDef();

        weldJointDef.initialize(weapon.getBody(), body, weapon.getBody().getPosition());
//        weldJointDef.dampingRatio = 0;
//        weldJointDef.frequencyHz = 10;

        weldJoint = (WeldJoint) body.getWorld().createJoint(weldJointDef);

        weapon.getBody().setMassData(new MassData() {
            {
                mass = 0.0001f;
                center.set(Fighter.this.body.getMassData().center);
                I = 0.0001f;
            }
        });

    }

    public Fighter(Body body) {
        this(body, new Weapon(WorldUtils.createWeapon(body.getWorld(), body.getPosition().add(0, -2),
                -MathUtils.PI / 2, 0.2f, 1f), new Vector2(0, 1.6f)));
    }

    public void fire() {
        weapon.fire(body.getAngle());
    }

    public void setTarget(Vector2 target) {
        this.target = target;

        // FIXME: 11/1/17 remove later
        if (target != null && WorldUtils.containsInFOV(body.getPosition(), target, WorldUtils.transformAngle(body.getAngle()))) {
            Gdx.app.log("angle", "contains " + body.getPosition() + " : " + target);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

    }
}
