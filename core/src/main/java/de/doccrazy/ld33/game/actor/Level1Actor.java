package de.doccrazy.ld33.game.actor;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import box2dLight.PointLight;
import de.doccrazy.ld33.core.Resource;
import de.doccrazy.ld33.data.GameRules;
import de.doccrazy.ld33.data.ThreadType;
import de.doccrazy.ld33.game.world.GameWorld;
import de.doccrazy.shared.game.world.BodyBuilder;
import de.doccrazy.shared.game.world.GameState;
import de.doccrazy.shared.game.world.ShapeBuilder;

public class Level1Actor extends Level {
    private List<Body> bodies = new ArrayList<>();
    private final Rectangle boundingBox = new Rectangle(0, 0, GameRules.LEVEL_WIDTH, GameRules.LEVEL_HEIGHT);

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

        light(4.2f, 2f);
        light(8f, 2f);
        light(4.2f, 5.5f);
        light(8f, 5.5f);

        world.addAmmo(ThreadType.STRUCTURE, 6);
        world.addAmmo(ThreadType.STICKY, 25);

        task.in(0.5f, Void -> {}).thenEvery(1f, Void -> {
            if (world.getGameState() == GameState.GAME && MathUtils.randomBoolean(1.25f)) {
                world.addActor(new FlyActor(world, new Vector2(MathUtils.random(3f, 9.3f), MathUtils.random(1f, GameRules.LEVEL_HEIGHT - 1f))));
            }
        });
        Resource.MUSIC.music1.play();
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
        Resource.MUSIC.music1.stop();
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
    public int getScoreGoal() {
        return 20;
    }

    @Override
    public float getTime() {
        return 120;
    }

}
