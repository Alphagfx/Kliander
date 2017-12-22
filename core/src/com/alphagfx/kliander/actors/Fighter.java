package com.alphagfx.kliander.actors;

import com.alphagfx.kliander.actors.actions.GameAction;
import com.alphagfx.kliander.utils.WorldUtils;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.LinkedHashSet;
import java.util.Set;

public class Fighter extends Creature {

    static Set<String> actionSet;

    static {
        actionSet = new LinkedHashSet<>(Creature.actionSet);
        actionSet.add("FIRE");
    }

    @Override
    public Set<String> getActionSet() {
        return actionSet;
    }

    @Override
    public <T> Action doGameAction(String action, T target) {

        switch (action) {
            case "FIRE": {
                return new GameAction("Fire", target) {
                    {
                        setName("Fire");
                    }

                    @Override
                    public boolean doAction() {
                        ((GameActor) target).fire(getPosition());
                        setActor(null);
                        return true;
                    }
                };
            }
            default: {
                return super.doGameAction(action, target);
            }
        }
    }

    private Weapon weapon;
    private Vector2 target;
    private RevoluteJoint revoluteJoint;

    public Fighter(Body body, Weapon weapon) {
        super(body);

        addWeapon(weapon);
    }

    public Fighter(Body body) {
        this(body, new Weapon(WorldUtils.createBody(body.getWorld(), body.getWorldPoint(new Vector2(1.5f, -0.7f)),
                body.getAngle(), 0.7f, 0.2f), new Vector2(1.3f, 0)));
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

    @Override
    protected void fire(Vector2 target) {

        setTarget(target);

        if (WorldUtils.containsInFOV(body.getPosition(), target, body.getAngle())) {
            turnWeapon();

            weapon.fire(target);
        }
    }

    private void turnWeapon() {

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

    public static class Factory implements com.alphagfx.kliander.actors.Factory<Fighter> {
        @Override
        public Fighter create(World world, Vector2 position, float angle, Object... objects) {
            return new Fighter(WorldUtils.createBody(world, position, angle, 0.6f, 1.5f));
        }
    }
}
