package com.alphagfx.kliander.stages;

import com.alphagfx.kliander.actors.GameActor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;

import java.util.Hashtable;

public class UIStage extends Stage {

    private GameStage gameStage;
    private String selectedAction = "NULL";

    //    Because not null
    private Hashtable<String, Actor> actionSet;

    String getSelectedAction() {
        return selectedAction;
    }

    private Table table;

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


        setRightSide();

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

    private void setRightSide() {
        Skin skin = VisUI.getSkin();

        Table rightSide = new Table(skin);
        rightSide.setDebug(true);
        rightSide.setWidth(250);
        rightSide.defaults().size(150, 50).pad(10);
        table.add(rightSide).right();

    }

    private void setVisibleActionMenu() {
        boolean state = table.getCells().get(1).getActor().isVisible();
        Actor actor = table.getCells().get(1).getActor();
        actor.setVisible(!state);
    }

    public void updateActionMenu() {
        Table rightSide = ((Table) table.getCells().get(1).getActor());
        for (Cell cell : rightSide.getCells()) {
            cell.clearActor();
//            System.out.println("clear");
        }
/*        for (int i = rightSide.getCells().size-1; i > -1; i--) {
            rightSide.getCells().removeIndex(i);
        }
        rightSide.invalidate();
        rightSide.layout();
        System.out.println(rightSide.toString());*/

/*
        Label label = new Label("Name:", VisUI.getSkin());
        label.setAlignment(1);
        TextField textField = new TextField("", VisUI.getSkin());
        textField.setAlignment(1);
        textField.clearListeners();
        textField.setVisible(false);

        rightSide.add(label);
        rightSide.row();
        rightSide.add(textField);
        rightSide.row();
        */
        int i = 0;
        for (String key : actionSet.keySet()) {
            if (gameStage.getSelectedGameActor().getActionSet().contains(key)) {
                rightSide.getCells().get(i).setActor(actionSet.get(key));
                i++;
            }
        }
    }

    public void updateActionSet(GameActor gameActor) {

        Table rightSide = ((Table) table.getCells().get(1).getActor());

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
//            System.out.println("add");
        }
    }

}
