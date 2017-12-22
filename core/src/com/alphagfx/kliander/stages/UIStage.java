package com.alphagfx.kliander.stages;

import com.alphagfx.kliander.actors.GameActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;

import java.util.Hashtable;
import java.util.Map;

public class UIStage extends Stage {

    private GameStage gameStage;
    private String selectedAction = "NULL";

    private Skin skin = VisUI.getSkin();

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
        table.add(setScrollPaneSide(rightSide)).height(600);
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
    }

    private ScrollPane setScrollPaneSide(Table refSide) {

        refSide.setDebug(true);
        refSide.setSkin(skin);
        refSide.defaults().size(150, 50).pad(10);
        refSide.top();

        return new ScrollPane(refSide, skin);
    }

    private void setVisibleActionMenu() {
        rightSide.setVisible(!rightSide.isVisible());
        leftSide.setVisible(!leftSide.isVisible());
    }

    void updateActionMenu() {

        for (Cell cell : rightSide.getCells()) {
            cell.clearActor();
        }

        if (gameStage.getSelectedGameActor() == null) {
            return;
        }

        int i = 0;
        for (String key : actionSet.keySet()) {
            if (gameStage.getSelectedGameActor().getActionSet().contains(key)) {
                rightSide.getCells().get(i).setActor(actionSet.get(key));
                i++;
            }
        }
        updateLeftSide();
    }

    private Pool<TextButton> textButtonPool = new Pool<TextButton>(4, 20) {
        @Override
        protected TextButton newObject() {
            return new TextButton("empty", skin) {
                Actor actor = null;
                Action action = null;

                {
                    addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            actor.removeAction(action);
                            action = null;
                            actor = null;
                            updateLeftSide();
                        }
                    });
                }

                @Override
                public void setUserObject(Object userObject) {
                    try {
                        Object[] objects = ((Object[]) userObject);
                        actor = ((Actor) objects[0]);
                        action = ((Action) objects[1]);
                    } catch (ClassCastException e) {
                        // FIXME: 12/22/17 print to file log
                        e.printStackTrace();
                    }
                }

            };
        }
    };

    public void updateLeftSide() {

        for (Cell cell : leftSide.getCells()) {
            textButtonPool.free((TextButton) cell.getActor());

        }
        leftSide.clearChildren();

        if (gameStage.getSelectedGameActor() == null) {
            return;
        }

        for (Action action : gameStage.getSelectedGameActor().listActions()) {
            TextButton textButton = textButtonPool.obtain();
            textButton.setText(action.toString());
            textButton.setUserObject(new Object[]{gameStage.getSelectedGameActor(), action});
            leftSide.add(textButton);
            leftSide.row();
        }

    }

    void updateActionSet(GameActor gameActor) {

        for (String str : gameActor.getActionSet()) {
            actionSet.putIfAbsent(str, new TextButton(str, skin) {
                private String action = str;

                {
                    addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
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

}
