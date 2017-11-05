package com.alphagfx.kliander.box2d;

import com.alphagfx.kliander.enums.UserDataType;
import com.badlogic.gdx.physics.box2d.Body;

public interface IBodyUserData {

    Body getBody();

    void setBody(Body body);

    UserDataType getUserDataType();

    void setUserDataType(UserDataType userDataType);

    void setDead(boolean isDead);

    boolean isDead();


    float getHealth();

    void setHealth(float health);

    default void receiveDamage(float damage) {
        setHealth(getHealth() - damage);
        if (getHealth() <= 0) {
            setDead(true);
        }
    }

    boolean isInvincible();

    void setInvincible(boolean invincible);


    default boolean destroyBodyUser() {

        if (isDead() == true && getBody() != null && getBody().getWorld() != null) {

            getBody().getWorld().destroyBody(getBody());
            return true;
        }

        return false;
    }

}
