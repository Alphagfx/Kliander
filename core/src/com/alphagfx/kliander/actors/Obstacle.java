//        Copyright Kliander Alphagfx
//
//        Licensed under the Apache License, Version 2.0 (the "License");
//        you may not use this file except in compliance with the License.
//        You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//        Unless required by applicable law or agreed to in writing, software
//        distributed under the License is distributed on an "AS IS" BASIS,
//        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//        See the License for the specific language governing permissions and
//        limitations under the License.

package com.alphagfx.kliander.actors;

import com.alphagfx.kliander.enums.UserDataType;
import com.alphagfx.kliander.utils.WorldUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class Obstacle extends GameActor {

    public Obstacle(Body body) {
        super(body);

        setUserDataType(UserDataType.OBSTACLE);

        setInvincible(true);
    }

    @Override
    protected void moveTo(Vector2 target) {

    }

    @Override
    protected void turnTo(float angle) {

    }

    @Override
    protected void fire(Vector2 target) {

    }

    @Override
    protected void specialAction(String name, Object... objects) {

    }

    public static class Factory implements com.alphagfx.kliander.actors.Factory<Obstacle> {
        @Override
        public Obstacle create(World world, Vector2 position, float angle, Object... objects) {
            if (objects.length == 2 && objects[0] instanceof Float && objects[1] instanceof Float) {
                return new Obstacle(WorldUtils.createObstacle(world, position, ((float) objects[0]), ((float) objects[1])));
            } else {
                return new Obstacle(WorldUtils.createObstacle(world, position, 1, 1));
            }
        }
    }

}
