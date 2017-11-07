package com.alphagfx.kliander.actors;

import com.alphagfx.kliander.box2d.IBodyUserData;
import com.alphagfx.kliander.enums.UserDataType;
import com.alphagfx.kliander.stages.GameStage;
import com.alphagfx.kliander.utils.Constants;
import com.alphagfx.kliander.utils.WorldUtils;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public abstract class GameActor extends Actor implements IBodyUserData {

    protected Body body;
    protected UserDataType userDataType = UserDataType.UNKNOWN;
    private boolean isDead;

    protected float health = 100;
    protected boolean invincible;

    public GameActor(Body body, float sizeX, float sizeY) {

        setBody(body);

        setSize(sizeX, sizeY);
        setOrigin(sizeX / 2, sizeY / 2);

        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRotation(WorldUtils.transformAngle(body.getAngle()) * MathUtils.radiansToDegrees);

        //        debug
        setDebug(true);

        addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((GameStage) getStage()).setSelectedGameActor((GameActor.this));
                System.out.println("pressed");
                super.clicked(event, x, y);
            }
        });

    }

    public GameActor(Body body) {

        this(body, Constants.GAME_ACTOR_DEFAULT_SIZE_X, Constants.GAME_ACTOR_DEFAULT_SIZE_Y);

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
