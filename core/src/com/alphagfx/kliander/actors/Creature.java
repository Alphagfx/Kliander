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
import com.alphagfx.kliander.stages.GameStage;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Creature extends GameActor {

    private float max_speed = 20;
    private Vector2 waypoint;

    public Creature(Body body) {
        super(body);
        setPosition(body.getPosition().x, body.getPosition().y);
        setSize(1, 1);
        setColor(Color.CYAN);

        setDebug(true);

        this.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((GameStage) Creature.this.getStage()).setSelectedCreature(Creature.this);
                System.out.println("pressed");
                super.clicked(event, x, y);
            }
        });

        System.out.println(getX() + " " + getY() + " " + getWidth() + " " + getHeight());
    }



    @Override
    public UserData getUserData() {
        return (CreatureUserData) userData;
    }

    @Override
    public UserData setUserData() {
        return userData;
    }

    public void stop() {
        body.setLinearVelocity(0, 0);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public void moveTo(Vector2 vector2) {
        waypoint = vector2;
        Vector2 speed = vector2.cpy();
        speed.sub(this.body.getPosition());

        body.setAngularVelocity(0);
        body.setLinearVelocity(speed.nor().scl(max_speed));
    }


    @Override
    public void act(float delta) {
        setPosition(body.getPosition().x, body.getPosition().y);
        setSize(10, 10);

        if (body.getPosition().epsilonEquals(waypoint, 0.05f)) {
            stop();
        }
    }
}
