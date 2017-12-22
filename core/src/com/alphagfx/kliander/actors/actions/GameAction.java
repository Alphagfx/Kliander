package com.alphagfx.kliander.actors.actions;

import com.alphagfx.kliander.actors.GameActor;
import com.alphagfx.kliander.stages.GameStage;
import com.alphagfx.kliander.stages.UIStage;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;

public abstract class GameAction extends Action {

    private Vector2 position;
    private GameActor gameActor;
    private String name;

    protected <T> GameAction(String name, T myTarget) {

        this.name = name;

        if (myTarget instanceof GameActor) {
            this.gameActor = ((GameActor) myTarget);
        }
        if (myTarget instanceof Vector2) {
            this.position = ((Vector2) myTarget);
        }

        this.target = actor;
    }

    @Override
    public boolean act(float delta) {
        boolean b = doAction();
        if (b) {
            end();
        }
        return doAction();
    }

    public abstract boolean doAction();

    private void end() {
        ((GameStage) target.getStage()).getUiStage().addAction(new RepeatAction() {
            {
                setAction(new Action() {
                    @Override
                    public boolean act(float delta) {
                        (((UIStage) target.getStage())).updateLeftSide();
                        return true;
                    }
                });
                setCount(1);
            }
        });
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
