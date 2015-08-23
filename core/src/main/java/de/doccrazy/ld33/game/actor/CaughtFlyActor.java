package de.doccrazy.ld33.game.actor;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import de.doccrazy.ld33.core.Resource;
import de.doccrazy.ld33.data.ThreadType;
import de.doccrazy.shared.game.actor.ShapeActor;
import de.doccrazy.shared.game.base.CollisionListener;
import de.doccrazy.shared.game.world.BodyBuilder;
import de.doccrazy.shared.game.world.Box2dWorld;
import de.doccrazy.shared.game.world.ShapeBuilder;

public class CaughtFlyActor extends ShapeActor implements CollisionListener {
    private static final float RADIUS = 0.1f;
    private final List<RevoluteJointDef> newJoints = new ArrayList<>();
    private boolean attached;

    public CaughtFlyActor(Box2dWorld world, Vector2 spawn) {
        super(world, spawn, false);
        setScale(3f);
    }

    @Override
    protected BodyBuilder createBody(Vector2 spawn) {
        return BodyBuilder.forDynamic(spawn).damping(0, 10)
                .fixShape(ShapeBuilder.circle(RADIUS)).fixProps(1f, 0.1f, 1f);
    }

    @Override
    protected void doAct(float delta) {
        super.doAct(delta);
        for (RevoluteJointDef joint : newJoints) {
            world.box2dWorld.createJoint(joint);
            attached = true;
        }
        newJoints.clear();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawRegion(batch, Resource.GFX.fly[0]);
        super.draw(batch, parentAlpha);
    }

    @Override
    public boolean beginContact(Body me, Body other, Vector2 normal, Vector2 contactPoint) {
        if (other.getUserData() instanceof ThreadActor && !attached) {
            ThreadActor thread = (ThreadActor)other.getUserData();
            if (thread.getThreadType() == ThreadType.STICKY) {
                RevoluteJointDef jointDef = new RevoluteJointDef();
                if (contactPoint == null) {
                    contactPoint = me.getPosition();
                }
                jointDef.initialize(me, other, contactPoint);
                newJoints.add(jointDef);
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
