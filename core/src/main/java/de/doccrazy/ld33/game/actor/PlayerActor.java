package de.doccrazy.ld33.game.actor;

import com.badlogic.gdx.math.Vector2;

import de.doccrazy.shared.game.actor.ShapeActor;
import de.doccrazy.shared.game.world.BodyBuilder;
import de.doccrazy.shared.game.world.Box2dWorld;
import de.doccrazy.shared.game.world.ShapeBuilder;

public class PlayerActor extends ShapeActor {
    private static final float RADIUS = 0.1f;

    public PlayerActor(Box2dWorld world, Vector2 spawn, boolean spawnIsLeftBottom) {
        super(world, spawn, spawnIsLeftBottom);
    }

    @Override
    protected BodyBuilder createBody(Vector2 spawn) {
        return BodyBuilder.forDynamic(spawn).fixShape(ShapeBuilder.circle(RADIUS));
    }

    @Override
    protected void doAct(float delta) {
        super.doAct(delta);
    }
}
