package com.alphagfx.kliander.stages;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;

public class UIStage extends Stage {

    private GameStage gameStage;
    private String selectedAction = "NULL";

    String getSelectedAction() {
        return selectedAction;
    }

    private Table table;
    private Stack uistack;

    public UIStage(Viewport viewport, GameStage gameStage) {
        super(viewport);
        this.gameStage = gameStage;
        gameStage.setUiStage(this);
        create();
    }

    private void create() {
        Skin skin = VisUI.getSkin();
        Label label = new Label("Name:", skin);
        TextField textField = new TextField("", skin);

        table = new Table(skin);
        table.setPosition(200, 300);
        table.defaults().size(100, 50).pad(10);

        addActor(table);

        table.setDebug(true);
        table.add(label);
        table.row();
        table.add(textField);
        table.row();

        for (String string : gameStage.getSelectedGameActor().getActionStack()) {
            table.add(new TextButton(string, skin) {
                private String action = string;

                {
                    addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            super.clicked(event, x, y);
                            selectedAction = action;
                            table.getCell(textField).getActor().setText(gameStage.getSelectedGameActor().toString());

                        }
                    });
                }
            }).row();
        }

        table.add(new Button(skin) {
            {
                addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        table.add("test").width(100);
                        table.row();
                    }
                });
            }
        });
        table.row();


    }

}
