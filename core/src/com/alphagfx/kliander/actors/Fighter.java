package com.alphagfx.kliander.actors;

import com.alphagfx.kliander.utils.WorldUtils;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.LinkedHashSet;
import java.util.Set;

public class Fighter extends Creature {

    protected static Set<String> actionSet;

    static {
        actionSet = new LinkedHashSet<>();
        actionSet.addAll(Creature.actionSet);
        actionSet.add("FIRE");
    }

    @Override
    public Set<String> getActionSet() {
        return actionSet;
    }

    @Override
    public boolean doGameAction(String action, Vector2 vector) {
        boolean action_performed = true;
        switch (action) {
            case "FIRE": {
                fire(vector);
                break;
            }
            default: {
                action_performed = false;
                break;
            }
        }
        if (!action_performed) {
            action_performed = super.doGameAction(action, vector);
        }
        return action_performed;
    }

    private Weapon weapon;
    private Vector2 target;
    private RevoluteJoint revoluteJoint;

    public Fighter(Body body, Weapon weapon) {
        super(body);

        addWeapon(weapon);
    }

    public Fighter(Body body) {
        this(body, new Weapon(WorldUtils.createBody(body.getWorld(), body.getPosition().add(1.5f, -0.7f),
                0, 0.7f, 0.2f), new Vector2(1.3f, 0)));
    }

    public void addWeapon(Weapon weapon) {

        this.weapon = weapon;

        if (getStage() != null && weapon != null && weapon.getStage() == null) {
            getStage().addActor(weapon);
        }

        if (weapon != null) {
            weapon.setHealth(10);
        }

        setHealth(100);


        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.initialize(body, weapon.getBody(), body.getPosition());

        jointDef.lowerAngle = -0.25f * MathUtils.PI; // -45 degrees
        jointDef.upperAngle = 0.25f * MathUtils.PI; // 45 degrees

        jointDef.enableLimit = true;
        jointDef.enableMotor = true;

        jointDef.maxMotorTorque = 10.0f;
        jointDef.motorSpeed = 0.0f;

        revoluteJoint = ((RevoluteJoint) body.getWorld().createJoint(jointDef));


        weapon.getBody().setMassData(new MassData() {
            {
                mass = 0.0001f;
                center.set(body.getMassData().center);
                I = 0.0001f;
            }
        });
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);

        if (stage != null && weapon != null && weapon.getStage() == null) {
            stage.addActor(weapon);
        }
    }

    private void fire(Vector2 vector) {

        setTarget(vector);

        if (WorldUtils.containsInFOV(body.getPosition(), vector, body.getAngle())) {
            turnWeapon(vector);

            weapon.fire(0);
        }
    }

    private void turnWeapon(Vector2 vector) {

        float body_angle = WorldUtils.transformAngle(weapon.getBody().getAngle());
        float targetAngle = WorldUtils.transformAngle(new Vector2(target).sub(weapon.getBody().getPosition()).nor().angleRad());

        revoluteJoint.setMotorSpeed((((targetAngle - body_angle) + MathUtils.PI2) % MathUtils.PI2 > MathUtils.PI) ? -5 : 5);

    }

    private void setTarget(Vector2 target) {
        this.target = target;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (target != null && Math.abs(WorldUtils.transformAngle(new Vector2(target).sub(weapon.getBody().getPosition()).angleRad()) - WorldUtils.transformAngle(weapon.getBody().getAngle())) <= 0.01) {
            revoluteJoint.setMotorSpeed(0);
        }
    }

    @Override
    public void setDead(boolean isDead) {
        super.setDead(isDead);
        weapon.setDead(isDead);
    }

    @Override
    public String toString() {
        return "Fighter: \n angle: " + body.getAngle() + "\n weapon angle: " + weapon.getBody().getAngle();
    }
}
