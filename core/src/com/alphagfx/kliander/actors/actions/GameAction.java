package com.alphagfx.kliander.actors.actions;

import com.alphagfx.kliander.actors.GameActor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;

public abstract class GameAction extends Action {

    private Vector2 position;
    private GameActor gameActor;
    private String name;

    protected <T> GameAction(String name, T target) {

        this.name = name;

        if (target instanceof GameActor) {
            this.gameActor = ((GameActor) target);
        }
        if (target instanceof Vector2) {
            this.position = ((Vector2) target);
        }
    }

    public Vector2 getPosition() {
        if (gameActor == null) {
            return position;
        }
        return gameActor.getBody().getPosition();
    }

    @Override
    public String toString() {
        return name;
    }
}
