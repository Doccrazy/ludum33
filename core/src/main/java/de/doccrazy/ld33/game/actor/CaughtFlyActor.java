package de.doccrazy.ld33.game.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import de.doccrazy.ld33.core.Resource;
import de.doccrazy.ld33.game.world.GameWorld;
import de.doccrazy.shared.game.actor.ShapeActor;
import de.doccrazy.shared.game.world.BodyBuilder;
import de.doccrazy.shared.game.world.ShapeBuilder;

public class CaughtFlyActor extends ShapeActor<GameWorld> {
    private static final float RADIUS = 0.1f;
    private Body threadBody;
    private Vector2 threadContactPoint;

    public CaughtFlyActor(GameWorld world, Vector2 spawn, Body threadBody, Vector2 threadContactPoint) {
        super(world, spawn, false);
        this.threadBody = threadBody;
        this.threadContactPoint = threadContactPoint;
        setScale(3f);
    }

    @Override
    protected void init() {
        super.init();
        RevoluteJointDef jointDef = new RevoluteJointDef();
        if (threadContactPoint == null) {
            threadContactPoint = body.getPosition();
        }
        jointDef.initialize(body, threadBody, threadContactPoint);
        world.box2dWorld.createJoint(jointDef);
    }

    @Override
    protected BodyBuilder createBody(Vector2 spawn) {
        return BodyBuilder.forDynamic(spawn).damping(0, 10)
                .fixShape(ShapeBuilder.circle(RADIUS)).fixProps(1f, 0.1f, 1f);
    }

    @Override
    protected void doAct(float delta) {
        super.doAct(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawRegion(batch, Resource.GFX.fly[0]);
        super.draw(batch, parentAlpha);
    }
}
