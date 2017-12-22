package com.alphagfx.kliander.actors;

import com.alphagfx.kliander.actors.actions.GameAction;
import com.alphagfx.kliander.box2d.IBodyUserData;
import com.alphagfx.kliander.enums.UserDataType;
import com.alphagfx.kliander.utils.WorldUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;

import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class GameActor extends Actor implements IBodyUserData {

    Body body;
    private UserDataType userDataType = UserDataType.UNKNOWN;
    private boolean isDead;

    private float health = 100;
    private boolean invincible;

    private static Hashtable<String, Factory<? extends GameActor>> gameFactory = new Hashtable<>();

    public static Factory<? extends GameActor> getFactory(String key) {
        return gameFactory.get(key);
    }

    public static void addFactory(String key, Factory<? extends GameActor> factory) {
        gameFactory.putIfAbsent(key, factory);
    }

    public static Set<String> getFactoryKeys() {
        return gameFactory.keySet();
    }

    /**
     * HOW TO ADD to Action Set
     * add new protected action set
     * override getActionSet()
     * override static constructor with new action Set and add new actions
     * override doGameAction
     */
    static Set<String> actionSet;

    static {
        actionSet = new LinkedHashSet<>();
        actionSet.add("INFO");

        GameActor.addFactory("Creature", new Creature.Factory());
        GameActor.addFactory("Obstacle", new Obstacle.Factory());
        GameActor.addFactory("Fighter", new Fighter.Factory());
    }

    public Set<String> getActionSet() {
        return actionSet;
    }

    public <T> Action doGameAction(String actionName, T target) {

        switch (actionName) {
            case "INFO": {
                return new GameAction("Info", target) {

                    @Override
                    public boolean doAction() {
                        Gdx.app.log("INFO", target.toString());
                        setActor(null);
                        return true;
                    }
                };
            }
            default: {
                return null;
            }
        }
    }

    protected GameActor(Body body, float sizeX, float sizeY) {

        setBody(body);

        setSize(sizeX, sizeY);
        setOrigin(sizeX / 2, sizeY / 2);

        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRotation(WorldUtils.transformAngle(body.getAngle()) * MathUtils.radiansToDegrees);


    }

    protected GameActor(Body body) {

        if (body != null)
            setBody(body);

    }

    protected abstract void moveTo(Vector2 target);

    protected abstract void turnTo(float angle);

    protected abstract void fire(Vector2 target);

    protected abstract void specialAction(String name, Object... objects);

    /**
     * So now we have only sequential behavior of an actor
     */
    @Override
    public void addAction(Action action) {

        if (getActions().size == 0) {
            super.addAction(new SequenceAction() {
                @Override
                public String toString() {
                    return "(Main Sequence)\nDelete all";
                }
            });
        }
        ((SequenceAction) getActions().get(0)).addAction(action);
    }

    @Override
    public void removeAction(Action action) {
        removeActionInParallel(getActions(), action);
    }

    private void removeActionInParallel(Array<Action> source, Action removeAction) {

        if (source.removeValue(removeAction, true)) {
            removeAction.setActor(null);
        } else {
            for (Action action : source) {
                if (action instanceof ParallelAction) {
                    removeActionInParallel(((ParallelAction) action).getActions(), removeAction);
                }
            }
        }

    }

    public Array<Action> listActions() {
        return listActions(this.getActions());
    }

    public Array<Action> listActions(Array<Action> actionArray) {
        Array<Action> actions = new Array<>();
        for (int i = 0; i < actionArray.size; i++) {
            if (actionArray.get(i).getActor() != null) {
                actions.add(actionArray.get(i));
                if (actionArray.get(i) instanceof ParallelAction) {
                    actions.addAll(listActions(((ParallelAction) actionArray.get(i)).getActions()));
                }
            }
        }
        return actions;
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
        return "GameActor : " + userDataType + " :" + (isDead ? "dead" : "alive");
    }
}
