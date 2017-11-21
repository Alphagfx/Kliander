package com.alphagfx.kliander.actors.actions;

import com.alphagfx.kliander.actors.GameActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

public class GameAction extends TemporalAction {

    private Vector2 position;
    private GameActor gameActor;
    private String name;

    public <T> GameAction(String name, float duration, T target) {
        this.name = name;
        setDuration(duration);
        if (target instanceof GameActor) {
            this.gameActor = ((GameActor) target);
        }
        if (target instanceof Vector2) {
            this.position = ((Vector2) target);
        }
    }

    @Override
    protected void begin() {
        super.begin();
        if (target instanceof GameActor) {
            position = gameActor != null ? gameActor.getBody().getPosition() : position;
            ((GameActor) target).doGameAction(name, position);
            Gdx.app.log(name, "begin " + getDuration());
        }
    }

    @Override
    protected void update(float percent) {

    }

    @Override
    protected void end() {
        Gdx.app.log(name, "end");
        setActor(null);
    }

    @Override
    public String toString() {
        return name;
    }
}
