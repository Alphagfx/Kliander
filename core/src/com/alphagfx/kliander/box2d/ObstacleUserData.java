package com.alphagfx.kliander.box2d;

import com.alphagfx.kliander.enums.UserDataType;

public class ObstacleUserData extends UserData {

    public ObstacleUserData() {
        super();
        userDataType = UserDataType.OBSTACLE;
    }

}
