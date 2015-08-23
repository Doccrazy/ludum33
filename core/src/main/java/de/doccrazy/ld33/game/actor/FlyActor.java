package de.doccrazy.ld33.game.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import de.doccrazy.ld33.core.Resource;
import de.doccrazy.ld33.data.ThreadType;
import de.doccrazy.ld33.game.world.GameWorld;
import de.doccrazy.shared.game.actor.ShapeActor;
import de.doccrazy.shared.game.base.CollisionListener;
import de.doccrazy.shared.game.world.BodyBuilder;
import de.doccrazy.shared.game.world.ShapeBuilder;

public class FlyActor extends ShapeActor<GameWorld> implements CollisionListener {
    private static final float RADIUS = 0.1f;
    private static final float Z_SPEED = 2f;
    private static final float SCALE = 3f;

    private float stateTime;
    private float startZ = -3f, endZ = 3f, zSpeed = Z_SPEED;
    private float z;
    private Body threadBody;
    private Vector2 threadContactPoint;

    public FlyActor(GameWorld world, Vector2 spawn) {
        super(world, spawn, false);
    }

    @Override
    protected void init() {
        super.init();
        body.setActive(false);
        setScale(SCALE);
    }

    @Override
    protected BodyBuilder createBody(Vector2 spawn) {
        return BodyBuilder.forDynamic(spawn).zeroGrav()
                .fixShape(ShapeBuilder.circle(RADIUS)).fixSensor();
    }

    @Override
    protected void doAct(float delta) {
        super.doAct(delta);
        stateTime += delta;
        z = startZ + zSpeed * stateTime;
        body.setActive(z > -0.3f && z < 0.3f);
        if (threadBody != null) {
            kill();
            world.addActor(new CaughtFlyActor(world, body.getPosition(), threadBody, threadContactPoint));
        }
        if (z > endZ) {
            kill();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float progress = MathUtils.clamp((z - startZ) / (endZ - startZ), 0.01f, 0.99f);
        float zDist = Math.abs(progress - 0.5f) * 2f;
        setScale(SCALE * MathUtils.lerp(1.5f, 0.5f, progress));
        batch.setColor(1, 1, 1, Interpolation.exp10In.apply(1f, 0f, zDist));
        drawRegion(batch, Resource.GFX.fly[(int)(zDist * Resource.GFX.fly.length)]);
        batch.setColor(1, 1, 1, 1);
    }

    @Override
    public boolean beginContact(Body me, Body other, Vector2 normal, Vector2 contactPoint) {
        float impulse = 0.005f;
        if (contactPoint == null) {
            contactPoint = me.getPosition();
        }
        if (other.getUserData() instanceof ThreadActor) {
            ThreadActor thread = (ThreadActor)other.getUserData();
            other.applyLinearImpulse(-impulse + (float)Math.random() * 2f * impulse,
                    -impulse + (float)Math.random() * 2f * impulse, contactPoint.x, contactPoint.y, true);
            if (thread.getThreadType() == ThreadType.STICKY) {
                threadBody = other;
                threadContactPoint = contactPoint;
            }
        }
        return true;
    }

    @Override
    public void endContact(Body other) {
    }

    @Override
    public void hit(float force) {
    }
}
