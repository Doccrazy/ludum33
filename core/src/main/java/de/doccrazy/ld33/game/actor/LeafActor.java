package de.doccrazy.ld33.game.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import de.doccrazy.ld33.core.Resource;
import de.doccrazy.ld33.game.world.GameWorld;
import de.doccrazy.shared.game.actor.ShapeActor;
import de.doccrazy.shared.game.world.BodyBuilder;
import de.doccrazy.shared.game.world.ShapeBuilder;

public class LeafActor extends ShapeActor<GameWorld> {
    private static final float RADIUS = 0.1f;
    private float noise;

    public LeafActor(GameWorld world) {
        super(world, new Vector2(MathUtils.random(world.getLevel().getBoundingBox().width),
                MathUtils.random(world.getLevel().getBoundingBox().height)), false);
        setzOrder(100);
        setScale(0.25f);
        noise = MathUtils.random(-0.01f, 0.01f);
    }

    @Override
    protected BodyBuilder createBody(Vector2 spawn) {
        return BodyBuilder.forDynamic(spawn).damping(5f, 0).gravityScale(1f)
                .velocity(Vector2.Zero, MathUtils.random())
                .fixShape(ShapeBuilder.circle(RADIUS)).fixSensor().fixFilter((short)1, (short)0);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawRegion(batch, Resource.GFX.dustMote);
    }

    @Override
    protected void doAct(float delta) {
        body.applyForce(noise, 0.25f, 0, 0, true);
        Rectangle bb = world.getLevel().getBoundingBox();
        if (body.getPosition().x > bb.width + 2*RADIUS) {
            body.setTransform(-2*RADIUS, body.getPosition().y, body.getAngle());
        }
        if (body.getPosition().x < -2*RADIUS) {
            body.setTransform(bb.width + 2*RADIUS, body.getPosition().y, body.getAngle());
        }
        if (body.getPosition().y > bb.height + 2*RADIUS) {
            body.setTransform(body.getPosition().x, -2*RADIUS, body.getAngle());
        }
        if (body.getPosition().y < -2*RADIUS) {
            body.setTransform(body.getPosition().x, bb.height + 2*RADIUS, body.getAngle());
        }
        super.doAct(delta);
    }
}
