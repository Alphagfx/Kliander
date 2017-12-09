package com.alphagfx.kliander.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public interface Factory<T> {

    T create(World world, Vector2 position, float angle, Object... objects);
}
