package com.alphagfx.kliander.box2d;

import com.alphagfx.kliander.enums.UserDataType;
import com.badlogic.gdx.physics.box2d.Body;

public interface IBodyUserData {

    Body getBody();

    void setBody(Body body);

    UserDataType getUserDataType();

    void setUserDataType(UserDataType userDataType);

}
