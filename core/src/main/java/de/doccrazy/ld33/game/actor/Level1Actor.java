package de.doccrazy.ld33.game.actor;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import de.doccrazy.ld33.core.Resource;
import de.doccrazy.ld33.data.GameRules;
import de.doccrazy.shared.game.actor.Box2dActor;
import de.doccrazy.shared.game.world.BodyBuilder;
import de.doccrazy.shared.game.world.Box2dWorld;
import de.doccrazy.shared.game.world.ShapeBuilder;

public class Level1Actor extends Box2dActor {
    private List<Body> bodies = new ArrayList<>();

    public Level1Actor(Box2dWorld world) {
        super(world);
        bodies.add(BodyBuilder.forStatic(new Vector2(1.5f, GameRules.LEVEL_HEIGHT/2f))
                .fixShape(ShapeBuilder.box(1.3f, GameRules.LEVEL_HEIGHT/2f)).build(world));
        bodies.add(BodyBuilder.forStatic(new Vector2(11f, GameRules.LEVEL_HEIGHT/2f))
                .fixShape(ShapeBuilder.box(1.3f, GameRules.LEVEL_HEIGHT/2f)).build(world));
        bodies.add(BodyBuilder.forStatic(new Vector2(6.25f, 0.2f))
                .fixShape(ShapeBuilder.box(3.45f, 0.2f)).build(world));
        bodies.add(BodyBuilder.forStatic(new Vector2(6.25f, GameRules.LEVEL_HEIGHT - 0.2f))
                .fixShape(ShapeBuilder.box(3.45f, 0.2f)).build(world));
    }

    @Override
    protected void doAct(float delta) {
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(Resource.GFX.level1fg, 0, 0, GameRules.LEVEL_WIDTH, GameRules.LEVEL_HEIGHT);
    }

    @Override
    protected void doRemove() {
        super.doRemove();
        for (Body body : bodies) {
            world.box2dWorld.destroyBody(body);
        }
    }

}
