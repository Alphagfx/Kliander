package com.alphagfx.kliander.actors;

import com.alphagfx.kliander.box2d.IBodyUserData;
import com.alphagfx.kliander.enums.UserDataType;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class GameActor extends Actor implements IBodyUserData {

    protected Body body;
    protected UserDataType userDataType = UserDataType.UNKNOWN;

    public GameActor(Body body) {

        setBody(body);

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
}
