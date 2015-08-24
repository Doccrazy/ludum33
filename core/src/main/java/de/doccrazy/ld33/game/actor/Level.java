package de.doccrazy.ld33.game.actor;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import de.doccrazy.ld33.game.world.GameWorld;
import de.doccrazy.shared.game.actor.Box2dActor;

public abstract class Level extends Box2dActor<GameWorld> {

    public Level(GameWorld world) {
        super(world);
    }

    public abstract Rectangle getBoundingBox();

    public abstract Vector2 getSpawn();

    public abstract int getScoreGoal();

    public abstract float getTime();
}
