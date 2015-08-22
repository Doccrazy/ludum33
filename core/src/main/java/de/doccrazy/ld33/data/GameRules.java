package de.doccrazy.ld33.data;

import com.badlogic.gdx.math.Vector2;

public class GameRules {
    public static final Vector2 GRAVITY = new Vector2(0, -9.8f);

    public static final float LEVEL_WIDTH = 12;
    public static final float LEVEL_HEIGHT = LEVEL_WIDTH*9f/16f;
    public static final float WALL_WIDTH = 0.3f;

    public static final float PLAYER_HEATH = 2000;
    public static final float REGEN_DELAY = 1.5f;
    public static final float REGEN_PER_SEC = 100f;
    public static final float STAGGER_TIME = 0.75f;

    public static final int ROUNDS_TO_WIN = 3;

}
