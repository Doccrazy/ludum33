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
import de.doccrazy.ld33.data.ThreadType;
import de.doccrazy.shared.game.actor.Box2dActor;
import de.doccrazy.shared.game.world.BodyBuilder;
import de.doccrazy.shared.game.world.Box2dWorld;
import de.doccrazy.shared.game.world.ShapeBuilder;

public class Level2Actor extends Box2dActor implements Level {
    private static final float SCALE = 9f/12f;
    private List<Body> bodies = new ArrayList<>();
    private final Rectangle boundingBox = new Rectangle(0, 0, 9, 9*9f/16f);
    private float stateTime;

    public Level2Actor(Box2dWorld world) {
        super(world);
        bodies.add(BodyBuilder.forStatic(new Vector2(0, 0))
                .fixShape(ShapeBuilder.boxAbs(2.4f*SCALE, boundingBox.height)).build(world));
        bodies.add(BodyBuilder.forStatic(new Vector2(9.8f*SCALE, 0))
                .fixShape(ShapeBuilder.boxAbs(2.5f*SCALE, boundingBox.height)).build(world));
        bodies.add(BodyBuilder.forStatic(new Vector2(2.4f*SCALE, boundingBox.height - 0.9f*SCALE))
                .fixShape(ShapeBuilder.boxAbs(7.4f*SCALE, 0.9f*SCALE)).build(world));
        bodies.add(BodyBuilder.forStatic(new Vector2(5.9f*SCALE, 0))
                .fixShape(ShapeBuilder.boxAbs(0.2f*SCALE, boundingBox.height - 0.9f*SCALE)).build(world));

        //light(3.8f, 2f);
        //light(7f, 2f);
        //light(3.8f, 6f);
        //light(7f, 6f);
    }

    private void light(float x, float y) {
        PointLight light = new PointLight(world.rayHandler, 10, new Color(1f, 1f, 0.5f, 0.5f), 2f, x, y);
        light.setXray(true);
        lights.add(light);
    }

    @Override
    protected void doAct(float delta) {
        stateTime += delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(Resource.GFX.level2fg, 0, 0, boundingBox.width, boundingBox.height);
        if ((stateTime > 1.5f && stateTime < 3f)
                || (stateTime > 3.2f && stateTime < 3.4f) || (stateTime > 3.6f && stateTime < 3.8f)) {
            batch.draw(Resource.GFX.level2Tutorial, 0, 0, boundingBox.width, boundingBox.height);
        }
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
        return new Vector2(1f, 4f);
    }

    @Override
    public ThreadType[] getAllowedThreads() {
        return new ThreadType[]{ThreadType.STICKY};
    }
}
