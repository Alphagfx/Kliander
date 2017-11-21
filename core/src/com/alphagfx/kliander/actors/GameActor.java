package com.alphagfx.kliander.actors;

import com.alphagfx.kliander.box2d.IBodyUserData;
import com.alphagfx.kliander.enums.UserDataType;
import com.alphagfx.kliander.utils.Constants;
import com.alphagfx.kliander.utils.WorldUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class GameActor extends Actor implements IBodyUserData {

    protected Body body;
    private UserDataType userDataType = UserDataType.UNKNOWN;
    private boolean isDead;

    private float health = 100;
    private boolean invincible;

    /**
     * HOW TO ADD to Action Set
     * add new protected action set
     * override getActionSet()
     * override static constructor with new action Set and add new actions
     * override doGameAction
     */

    protected static Set<String> actionSet;

    static {
        actionSet = new LinkedHashSet<>();
//        actionSet.add("GAME ACTOR");
        actionSet.add("INFO");
    }

    public Set<String> getActionSet() {
        return actionSet;
    }

    public boolean doGameAction(String action, Vector2 vector) {
        boolean action_performed = true;
        switch (action) {
            case "INFO": {
                Gdx.app.log("INFO", toString());
                break;
            }
            default: {
                action_performed = false;
                break;
            }
        }
        return action_performed;
    }

    public GameActor(Body body, float sizeX, float sizeY) {

        setBody(body);

        setSize(sizeX, sizeY);
        setOrigin(sizeX / 2, sizeY / 2);

        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRotation(WorldUtils.transformAngle(body.getAngle()) * MathUtils.radiansToDegrees);


    }

    public GameActor(Body body) {

        this(body, Constants.GAME_ACTOR_DEFAULT_SIZE_X, Constants.GAME_ACTOR_DEFAULT_SIZE_Y);

    }

    /**
     * So now we have only sequential behavior of an actor
     */

    @Override
    public void addAction(Action action) {

        if (getActions().size == 0) {
            super.addAction(new SequenceAction());
        }
        ((SequenceAction) getActions().get(0)).addAction(action);
    }

    // FIXME: 11/20/17 removeValue(action, IDENTITY) and check for null
    @Override
    public void removeAction(Action action) {

        if (getActions().size != 0) {
            ((SequenceAction) getActions().get(0)).getActions().removeValue(action, true);
        }
    }

    @Override
    public float getHealth() {
        return health;
    }

    @Override
    public void setHealth(float health) {

        if (!isInvincible()) {
            this.health = health;
        }
    }

    @Override
    public boolean isInvincible() {
        return invincible;
    }

    @Override
    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    @Override
    public boolean isDead() {
        return isDead;
    }

    @Override
    public void setDead(boolean isDead) {
        this.isDead = isDead;
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

    @Override
    public String toString() {
        return "GameActor : " + (isDead ? "dead" : "alive") + "   DataType : " + userDataType;
    }
}
