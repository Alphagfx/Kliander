package com.alphagfx.kliander.box2d;

import com.alphagfx.kliander.enums.UserDataType;

public abstract class UserData {

    protected UserDataType userDataType;
    protected int selectRange;

    public UserData() {

    }

    public UserDataType getUserDataType() {
        return userDataType;
    }

    public int getSelectRange() {
        return selectRange;
    }
}

