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

package com.alphagfx.kliander;

import com.alphagfx.kliander.utils.Constants;
import com.badlogic.gdx.physics.box2d.Shape;

public abstract class Subject {
    private float health;
    private float position_x;
    private float position_y;
    private Shape shape;

    Subject() {
        setHealth(100);
        setPosition_x(Constants.DEFAULT_X);
        setPosition_y(Constants.DEFAULT_y);
    }

    Subject(float health, float position_x, float position_y, Shape shape) {
        setHealth(health);
        setPosition_x(position_x);
        setPosition_y(position_y);
        setShape(shape);
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getPosition_x() {
        return position_x;
    }

    public void setPosition_x(float position_x) {
        this.position_x = position_x;
    }

    public float getPosition_y() {
        return position_y;
    }

    public void setPosition_y(float position_y) {
        this.position_y = position_y;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }
}
