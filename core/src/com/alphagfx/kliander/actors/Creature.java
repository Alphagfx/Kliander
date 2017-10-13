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

import com.alphagfx.kliander.box2d.CreatureUserData;
import com.alphagfx.kliander.box2d.UserData;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Creature extends GameActor {

    private boolean dashing;

    public Creature(Body body) {
        super(body);
    }

    @Override
    public UserData getUserData() {
        return (CreatureUserData) userData;
    }

    public void dash() {
//        if (!dashing) {
        body.applyLinearImpulse(new Vector2(5, 0), body.getWorldCenter(), true);
//        }
    }

    public void stop() {
        body.setLinearVelocity(0, 0);
    }

    public void noDash() {
        dashing = false;
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
    }

    public Vector2 getPostition() {
        return body.getPosition();
    }
}
