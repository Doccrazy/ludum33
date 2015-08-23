package de.doccrazy.ld33.game.actor;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import box2dLight.PointLight;
import de.doccrazy.ld33.core.Resource;
import de.doccrazy.ld33.data.GameRules;
import de.doccrazy.ld33.data.ThreadType;
import de.doccrazy.ld33.game.world.GameWorld;
import de.doccrazy.shared.game.actor.Box2dActor;
import de.doccrazy.shared.game.world.BodyBuilder;
import de.doccrazy.shared.game.world.ShapeBuilder;

public class Level1Actor extends Box2dActor<GameWorld> implements Level {
    private List<Body> bodies = new ArrayList<>();
    private final Rectangle boundingBox = new Rectangle(0, 0, GameRules.LEVEL_WIDTH, GameRules.LEVEL_HEIGHT*2);

    public Level1Actor(GameWorld world) {
        super(world);
        bodies.add(BodyBuilder.forStatic(new Vector2(0, 0))
                .fixShape(ShapeBuilder.boxAbs(2.6f, GameRules.LEVEL_HEIGHT)).build(world));
        bodies.add(BodyBuilder.forStatic(new Vector2(9.7f, 0))
                .fixShape(ShapeBuilder.boxAbs(2.6f, GameRules.LEVEL_HEIGHT)).build(world));
        bodies.add(BodyBuilder.forStatic(new Vector2(2.6f, GameRules.LEVEL_HEIGHT - 0.4f))
                .fixShape(ShapeBuilder.boxAbs(7.1f, 0.4f)).build(world));
        bodies.add(BodyBuilder.forStatic(new Vector2(2.6f, 0))
                .fixShape(ShapeBuilder.boxAbs(7.1f, 0.4f)).build(world));

        light(3.8f, 2f);
        light(7f, 2f);
        light(3.8f, 6f);
        light(7f, 6f);
    }

    private void light(float x, float y) {
        PointLight light = new PointLight(world.rayHandler, 10, new Color(1f, 1f, 0.5f, 0.5f), 2f, x, y);
        light.setXray(true);
        lights.add(light);
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

    @Override
    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    @Override
    public Vector2 getSpawn() {
        return new Vector2(1.5f, 5f);
    }

    @Override
    public ThreadType[] getAllowedThreads() {
        return new ThreadType[]{ThreadType.STRUCTURE, ThreadType.STICKY};
    }

}
