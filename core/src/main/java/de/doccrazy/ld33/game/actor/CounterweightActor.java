package de.doccrazy.ld33.game.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import de.doccrazy.ld33.core.Resource;
import de.doccrazy.ld33.data.GameRules;
import de.doccrazy.ld33.game.world.GameWorld;
import de.doccrazy.shared.game.actor.ShapeActor;
import de.doccrazy.shared.game.world.BodyBuilder;
import de.doccrazy.shared.game.world.ShapeBuilder;

public class CounterweightActor extends ShapeActor<GameWorld> {
    private static final float RADIUS = 0.2f;
    private Body attachTo;

    public CounterweightActor(GameWorld world, Vector2 spawn, Body attachTo) {
        super(world, spawn, false);
        this.attachTo = attachTo;
    }

    @Override
    protected void init() {
        super.init();
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.bodyA = body;
        jointDef.bodyB = attachTo;
        world.box2dWorld.createJoint(jointDef);
    }

    @Override
    protected BodyBuilder createBody(Vector2 spawn) {
        return BodyBuilder.forDynamic(spawn).gravityScale(0.2f)
                .fixShape(ShapeBuilder.circle(RADIUS)).fixProps(1, 1, 1).fixSensor();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawRegion(batch, Resource.GFX.counterweight);
    }

    @Override
    protected void doAct(float delta) {
        super.doAct(delta);
        body.applyForce(0, GameRules.GRAVITY.y * body.getMass() * 0.5f, body.getPosition().x, body.getPosition().y, true);
    }

}
