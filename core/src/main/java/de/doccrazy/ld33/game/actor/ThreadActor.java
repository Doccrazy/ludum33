package de.doccrazy.ld33.game.actor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import de.doccrazy.ld33.data.ThreadType;
import de.doccrazy.shared.game.actor.WorldActor;
import de.doccrazy.shared.game.base.PolyLineRenderer;
import de.doccrazy.shared.game.world.BodyBuilder;
import de.doccrazy.shared.game.world.Box2dWorld;
import de.doccrazy.shared.game.world.ShapeBuilder;
import net.dermetfan.gdx.physics.box2d.Chain;
import net.dermetfan.gdx.physics.box2d.Chain.Connection;

public class ThreadActor extends WorldActor {
    private static float RADIUS = 0.05f;
    private Vector2 start, end;
    private List<Chain> chains = new ArrayList<>();
    private ThreadType threadType;
    private float[] colors = new float[1000];
    private float stateTime;

    public ThreadActor(Box2dWorld world, Vector2 start, Vector2 end, ThreadType threadType) {
        super(world);
        this.start = start;
        this.end = end;
        this.threadType = threadType;
    }

    @Override
    protected void init() {
        super.init();
        Body attachStart = world.bodyAt(start, RADIUS/2f);
        Body attachEnd = world.bodyAt(end, RADIUS/2f);

        Body startBody = createLinkBody(start).build(world);
        Body endBody = createLinkBody(end).build(world);
        createChain(startBody, endBody, RADIUS, RADIUS);

        attach(startBody, attachStart, start);
        attach(endBody, attachEnd, end);
    }

    private void attach(Body bodyA, Body bodyB, Vector2 joinPoint) {
        if (bodyA != null && bodyB != null) {
            RevoluteJointDef jointDef = new RevoluteJointDef();
            jointDef.initialize(bodyA, bodyB, joinPoint);
            world.box2dWorld.createJoint(jointDef);
        }
    }

    private void createChain(Body startBody, Body endBody, float radius, float spacing) {
        float len = start.dst(end);
        float dist = radius*2;
        List<Body> chainBodies = new ArrayList<>();
        while (dist < len - radius*2) {
            Vector2 pos = end.cpy().sub(start).nor().scl(dist).add(start);
            chainBodies.add(createLinkBody(pos).build(world));
            dist += radius*2 + spacing;
        }
        DistanceJointDef jointDef = new DistanceJointDef();
        jointDef.localAnchorA.x = 0;
        jointDef.localAnchorB.x = 0;
        jointDef.length = radius*2 + spacing/2f;
        jointDef.frequencyHz = 60;
        jointDef.dampingRatio = 0.6f;
        Chain.DefBuilder builder = new Chain.DefBuilder(world.box2dWorld, null, null, jointDef);
        Chain chain = new Chain(builder);
        chain.add(startBody);
        chain.add(chainBodies.toArray(new Body[]{}));
        chain.add(endBody);
        chains.add(chain);
    }

    private BodyBuilder createLinkBody(Vector2 pos) {
        return BodyBuilder.forDynamic(pos).damping(0.5f, 2f).gravityScale(0.33f).userData(this)
                .fixShape(ShapeBuilder.circle(RADIUS)).fixSensor().fixProps(3f, 0.1f, 0.1f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        Color c1 = new Color(0.5f,1,0.5f,1);
        Color c2 = new Color(1,0.5f,0.5f,1);
        for (Chain chain: chains) {
            if (chain.length() < 2) {
                continue;
            }
            List<Vector2> points = new ArrayList<>(chain.length());

            for (Body body : chain.getSegments()) {
                points.add(body.getPosition());
            }
            float[] colors = this.colors;
            if (drawForces()) {
                for (int i = 0; i < chain.getSegments().size; i++) {
                    Float force = null;
                    if (i == 0) {
                        force = (Float) chain.getConnection(0).joints.get(0).getUserData();
                    } else if (i == chain.getSegments().size - 1) {
                        force = (Float) chain.getConnection(chain.length()-2).joints.get(0).getUserData();
                    } else {
                        Connection con1 = chain.getConnection(i - 1);
                        Connection con2 = chain.getConnection(i);
                        if (con1.joints.get(0).getUserData() != null && con2.joints.get(0).getUserData() != null) {
                            force = (((Float)con1.joints.get(0).getUserData()) + ((Float)con2.joints.get(0).getUserData())) / 2;
                        }
                    }

                    if (force != null) {
                        colors[i] = c1.cpy().lerp(c2, Math.min(1f, force / threadType.getMaxForce())).toFloatBits();
                    } else {
                        colors[i] = new Color(1,1,1,1).toFloatBits();
                    }
                }
            } else {
                colors = null;
            }

            PolyLineRenderer.drawLine(points, threadType.getWidth(), batch.getProjectionMatrix(), threadType.getTexture(), colors);
        }
        batch.begin();
    }

    private boolean drawForces() {
        return true;
    }

    @Override
    protected void doRemove() {
        super.doRemove();
        for (Chain chain : chains) {
            chain.destroy(0, chain.length() - 1);
        }
    }

    @Override
    protected void doAct(float delta) {
        stateTime += delta;
        removeOffscreenChains();
        handleBreaking(delta);
    }

    private void removeOffscreenChains() {
        for (Iterator<Chain> it = chains.iterator(); it.hasNext(); ) {
            Chain chain = it.next();
            boolean aboveFloor = false;
            for (Body body : chain.getSegments()) {
                aboveFloor = aboveFloor || body.getPosition().y > 0;
            }
            if (!aboveFloor || chain.length() < 2) {
                chain.destroy(0, chain.length()-1);
                it.remove();
            }
        }
        if (chains.isEmpty()) {
            kill();
        }
    }

    private void handleBreaking(float delta) {
        float invDt = 1f/delta;
        List<Chain> newChains = new ArrayList<>();
        for (Chain chain : chains) {
            for (int i = 0; i < chain.getConnections().size; i++) {
                Joint joint = chain.getConnection(i).joints.get(0);
                float force = joint.getReactionForce(invDt).len2();
                joint.setUserData(force);
                if (force > threadType.getMaxForce() && stateTime > 0.3f) {
                    newChains.add(chain.split(i));
                    break;
                }
            }
        }
        chains.addAll(newChains);
    }

    public ThreadType getThreadType() {
        return threadType;
    }
}
