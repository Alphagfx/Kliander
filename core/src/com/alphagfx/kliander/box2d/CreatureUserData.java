package com.alphagfx.kliander.box2d;

import com.alphagfx.kliander.enums.UserDataType;

public class CreatureUserData extends UserData {

    public CreatureUserData(int r) {
        super();
        userDataType = UserDataType.CREATURE;
        selectRange = r;
    }
}
