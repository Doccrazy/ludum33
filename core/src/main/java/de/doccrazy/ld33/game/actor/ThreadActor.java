package de.doccrazy.ld33.game.actor;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;

import de.doccrazy.ld33.core.Resource;
import de.doccrazy.shared.game.actor.ShapeActor;
import de.doccrazy.shared.game.base.PolyLineRenderer;
import de.doccrazy.shared.game.world.BodyBuilder;
import de.doccrazy.shared.game.world.Box2dWorld;
import de.doccrazy.shared.game.world.ShapeBuilder;
import net.dermetfan.gdx.physics.box2d.Chain;

public class ThreadActor extends ShapeActor {
    private static float RADIUS = 0.05f;
    private Vector2 end;
    private Body endBody;
    private List<Body> chainBodies = new ArrayList<>();
    private List<Joint> attachJoints = new ArrayList<>();
    private Chain chain;

    public ThreadActor(Box2dWorld world, Vector2 start, Vector2 end) {
        super(world, start, false);
        this.end = end;
    }

    @Override
    protected BodyBuilder createBody(Vector2 spawn) {
        return createLinkBody(spawn);
    }

    @Override
    protected void init() {
        Body attachStart = world.bodyAt(spawn, RADIUS/2f);
        Body attachEnd = world.bodyAt(end, RADIUS/2f);
        super.init();
        endBody = createLinkBody(end).build(world);
        createChain(RADIUS, RADIUS);
        attach(body, attachStart, spawn);
        attach(endBody, attachEnd, end);
    }

    private void attach(Body bodyA, Body bodyB, Vector2 joinPoint) {
        if (bodyA != null && bodyB != null) {
            RevoluteJointDef jointDef = new RevoluteJointDef();
            jointDef.initialize(bodyA, bodyB, joinPoint);
            attachJoints.add(world.box2dWorld.createJoint(jointDef));
        }
    }

    private void createChain(float radius, float spacing) {
        float len = spawn.dst(end);
        float dist = radius*2;
        while (dist < len - radius*2) {
            Vector2 pos = end.cpy().sub(spawn).nor().scl(dist).add(spawn);
            chainBodies.add(createLinkBody(pos).build(world));
            dist += radius*2 + spacing;
        }
        RopeJointDef jointDef = new RopeJointDef();
        jointDef.localAnchorA.x = 0;
        jointDef.localAnchorB.x = 0;
        jointDef.maxLength = radius*2 + spacing/2f;
        Chain.DefBuilder builder = new Chain.DefBuilder(world.box2dWorld, null, null, jointDef);
        chain = new Chain(builder);
        chain.add(body);
        chain.add(chainBodies.toArray(new Body[]{}));
        chain.add(endBody);
    }

    private BodyBuilder createLinkBody(Vector2 pos) {
        return BodyBuilder.forDynamic(pos).damping(5f, 1f)
                .fixShape(ShapeBuilder.circle(RADIUS)).fixSensor().fixProps(3f, 0.1f, 0.1f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        List<Vector2> points = new ArrayList<>(chainBodies.size() + 2);

        points.add(body.getPosition());
        for (Body body : chainBodies) {
            points.add(body.getPosition());
        }
        points.add(endBody.getPosition());

        PolyLineRenderer.drawLine(points, 0.05f, batch.getProjectionMatrix(), Resource.GFX.net);
        batch.begin();
    }

    @Override
    protected void doRemove() {
        super.doRemove();
        chain.remove(0, chain.length()-1);
        for (Body body : chainBodies) {
            world.box2dWorld.destroyBody(body);
        }
        world.box2dWorld.destroyBody(endBody);
        for (Joint joint : attachJoints) {
            world.box2dWorld.destroyJoint(joint);
        }
    }
}
