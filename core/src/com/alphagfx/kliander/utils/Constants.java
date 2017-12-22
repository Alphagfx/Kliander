package com.alphagfx.kliander.utils;

import com.badlogic.gdx.math.Vector2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Constants {

    static {
        File file = new File("log");
        String content = "This is the text content";

        try (FileOutputStream fop = new FileOutputStream(file)) {

            // if file doesn't exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // get the content in bytes
            byte[] contentInBytes = content.getBytes();

            fop.write(contentInBytes);
//            log = fop;
            fop.flush();
            fop.close();

            System.out.println("Done");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final static int APP_WIDTH = 1024;
    public final static int APP_HEIGHT = 768;

    public final static float DEFAULT_X = 0;
    public final static float DEFAULT_y = 0;

    public final static int WORLD_WIDTH = 100;
    public final static int WORLD_HEIGHT = 100;

    public final static Vector2 WORLD_GRAVITY = new Vector2(0, 0);

    public final static float GAME_ACTOR_DEFAULT_SIZE_X = 2;
    public final static float GAME_ACTOR_DEFAULT_SIZE_Y = 2;

//    public final static FileOutputStream log;

    /**
     * Default duration of one game turn in seconds
     */
    public final static float GAME_TURN_DURATION = 10;


}
