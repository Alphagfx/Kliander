package com.alphagfx.kliander.utils;

import com.badlogic.gdx.physics.box2d.World;

public class WorldUtils {

    public static World createWorld() {
        return new World(Constants.WORLD_GRAVITY, true);
    }

}
