package com.alphagfx.kliander.stages;

import com.alphagfx.kliander.actors.GameActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;

import java.util.Hashtable;
import java.util.Map;

public class UIStage extends Stage {

    private GameStage gameStage;
    private String selectedAction = "NULL";

    //    Because not null
    private Map<String, Actor> actionSet;

    String getSelectedAction() {
        return selectedAction;
    }

    private Table table;
    private Table rightSide = new Table();
    private Table leftSide = new Table();

    public UIStage(Viewport viewport, GameStage gameStage) {
        super(viewport);
        this.gameStage = gameStage;
        gameStage.setUiStage(this);
        actionSet = new Hashtable<>();
        create();
    }

    private void create() {
        Skin skin = VisUI.getSkin();


        table = new Table(skin);
        table.setFillParent(true);
        table.setDebug(false);
        table.addListener(new ClickListener());

        addActor(table);

        Table topSide = new Table(skin);
        topSide.setDebug(true);
        topSide.setHeight(50);
        topSide.defaults().size(150, 35).pad(5);
        topSide.add(new Label("status bar", skin)).getActor().setAlignment(1);
        topSide.add(new TextButton("show/hide", skin) {
            {
                addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        setVisibleActionMenu();
                    }
                });
            }
        });

        topSide.left();

        table.add(topSide).expand(true, false).left();
        table.top().row();

        table.add(setScrollPaneSide(leftSide)).height(600).left();
        table.add(setScrollPaneSide(rightSide)).height(600).left();

        table.row();

        TextButton textButton = new TextButton("play", skin) {
            {
                addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        gameStage.setPaused(!gameStage.isPaused());
                    }
                });
            }
        };

        textButton.getLabel().setAlignment(1, 1);
        topSide.add(textButton);

//        Gdx.app.log("table set", table.toString());

    }

    private ScrollPane setScrollPaneSide(final Table refSide) {

        refSide.setDebug(true);
        refSide.setSkin(VisUI.getSkin());
        refSide.defaults().size(150, 50).pad(10);
        refSide.top();

        return new ScrollPane(refSide, VisUI.getSkin());
    }

    private void setVisibleActionMenu() {
        rightSide.setVisible(!rightSide.isVisible());
        leftSide.setVisible(!leftSide.isVisible());
    }

    public void updateActionMenu() {

        for (Cell cell : rightSide.getCells()) {
            cell.clearActor();
        }

        int i = 0;
        for (String key : actionSet.keySet()) {
            if (gameStage.getSelectedGameActor().getActionSet().contains(key)) {
                rightSide.getCells().get(i).setActor(actionSet.get(key));
                i++;
            }
        }
    }

    private void updateLeftSide() {

        leftSide.clearChildren();

        if (gameStage.getSelectedGameActor() == null) {
            return;
        }
        for (Action action : gameStage.getSelectedGameActor().getActions()) {
            if (action instanceof SequenceAction) {
                for (Action action1 : ((SequenceAction) action).getActions()) {
                    if (action1.getActor() != null) {
                        leftSide.add(action1.toString());
                        leftSide.row();
                    }
                }
            } else {
                leftSide.add(action.toString());
                leftSide.row();
            }
        }

    }

    public void updateActionSet(GameActor gameActor) {

        for (String str : gameActor.getActionSet()) {
            actionSet.putIfAbsent(str, new TextButton(str, VisUI.getSkin()) {
                private String action = str;

                {
                    addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            super.clicked(event, x, y);
                            selectedAction = action;
                        }
                    });
                }
            });
        }

        while (actionSet.size() > rightSide.getCells().size) {
            rightSide.add();
            rightSide.row();
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        updateLeftSide();
    }
}
