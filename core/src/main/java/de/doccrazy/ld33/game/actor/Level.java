package de.doccrazy.ld33.game.actor;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import de.doccrazy.ld33.data.ThreadType;

public interface Level {

    Rectangle getBoundingBox();

    Vector2 getSpawn();

    ThreadType[] getAllowedThreads();
}
