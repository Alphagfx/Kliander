package com.alphagfx.kliander.box2d;

import com.alphagfx.kliander.enums.UserDataType;

public abstract class UserData {

    protected UserDataType userDataType;

    public UserData() {

    }

    public UserDataType getUserDataType() {
        return userDataType;
    }
}

