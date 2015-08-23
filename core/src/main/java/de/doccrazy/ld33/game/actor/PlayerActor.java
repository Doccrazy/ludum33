package de.doccrazy.ld33.game.actor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Timer;

import de.doccrazy.ld33.core.Resource;
import de.doccrazy.ld33.data.ThreadType;
import de.doccrazy.ld33.game.world.AttachedPoint;
import de.doccrazy.ld33.game.world.GameWorld;
import de.doccrazy.shared.game.actor.ShapeActor;
import de.doccrazy.shared.game.base.CollisionListener;
import de.doccrazy.shared.game.base.KeyboardMovementListener;
import de.doccrazy.shared.game.base.MovementInputListener;
import de.doccrazy.shared.game.world.BodyBuilder;
import de.doccrazy.shared.game.world.Box2dWorld;
import de.doccrazy.shared.game.world.GameState;
import de.doccrazy.shared.game.world.ShapeBuilder;

public class PlayerActor extends ShapeActor implements CollisionListener {
    private static final float RADIUS = 0.07f;
    private static final float JUMP_STRENGTH = 0.04f;
    private static final float JUMP_MIN = 0.03f;
    private static final float JUMP_MAX = 0.15f;

    private MovementInputListener movement;
    private List<Joint> attachJoints = new ArrayList<>();
    private DistanceJointDef attachJointDef;
    private boolean jump, build;
    private ThreadType threadType = ThreadType.STRUCTURE;
    private AttachedPoint startBuild;
    private Map<Body, Vector2> contacts = new HashMap<>();

    public PlayerActor(Box2dWorld world, Vector2 spawn) {
        super(world, spawn, false);
        setUseRotation(false);
        setScale(1.5f);
    }

    @Override
    protected BodyBuilder createBody(Vector2 spawn) {
        return BodyBuilder.forDynamic(spawn).gravityScale(0.5f).noRotate()
                .fixSensor().fixShape(ShapeBuilder.circle(RADIUS)).fixProps(1, 1, 1);
    }

    public void setupKeyboardControl() {
        movement = new KeyboardMovementListener();
        addListener((InputListener)movement);
    }

    public void setupController(MovementInputListener movement) {
        this.movement = movement;
    }

    @Override
    protected void doAct(float delta) {
        boolean shouldJump = jump || build;
        boolean released = checkRelease(shouldJump);
        if (released) {
            if (build) {
                startBuild = ((GameWorld)world).createAttachedPoint(body.getPosition(), RADIUS, body);
            } else {
                startBuild = null;
            }
            jump(delta);
        }
        checkAttach(!shouldJump);
        if (!((GameWorld)world).getLevel().getBoundingBox().contains(body.getPosition())) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    world.transition(GameState.INIT);
                }
            }, 1f);
        }
        super.doAct(delta);
    }

    private void jump(float delta) {
        Vector2 jumpImpulse = ((GameWorld)world).getMouseTarget().cpy().sub(body.getPosition());
        jumpImpulse = jumpImpulse.scl(JUMP_STRENGTH).clamp(JUMP_MIN, JUMP_MAX);
        System.out.println(jumpImpulse.len());
        body.applyLinearImpulse(jumpImpulse, body.getPosition(), true);
    }

    private boolean checkRelease(boolean shouldRelease) {
        if (shouldRelease && !attachJoints.isEmpty()) {
            //release
            for (Joint joint : attachJoints) {
                world.box2dWorld.destroyJoint(joint);
            }
            attachJoints.clear();
            return true;
        } else if (attachJointDef != null) {
            attachJoints.add(world.box2dWorld.createJoint(attachJointDef));
            postAttach(attachJointDef.bodyB, attachJointDef.bodyB.getWorldPoint(attachJointDef.localAnchorB));
            attachJointDef = null;
        }
        return false;
    }

    private boolean checkAttach(boolean shouldAttach) {
        if (shouldAttach && attachJoints.isEmpty() && !contacts.isEmpty()) {
            Entry<Body, Vector2> entry = contacts.entrySet().iterator().next();
            Body other = entry.getKey();
            Vector2 contactPoint = entry.getValue();
            //attach
            DistanceJointDef jointDef = new DistanceJointDef();
            Vector2 attachPoint;
            if (other.getUserData() instanceof ThreadActor) {
                //thread
                attachPoint = other.getPosition();
            } else {
                //level object
                attachPoint = contactPoint == null ? body.getPosition() : contactPoint;
                //move slightly to center of static body
                attachPoint.add(other.getPosition().cpy().sub(body.getPosition()).clamp(0, RADIUS));
            }
            jointDef.initialize(body, other, body.getPosition(), attachPoint);
            jointDef.frequencyHz = 60f;
            jointDef.dampingRatio = 0.8f;
            jointDef.length = RADIUS/2f;
            attachJointDef = jointDef;
            return true;
        }
        return false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawRegion(batch, Resource.GFX.dummy);
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void setBuild(boolean build) {
        this.build = build;
    }

    public void setThreadType(ThreadType threadType) {
        this.threadType = threadType;
    }

    @Override
    public boolean beginContact(Body me, Body other, Vector2 normal, Vector2 contactPoint) {
        contacts.put(other, contactPoint);
        return true;
    }

    private void postAttach(Body other, Vector2 attachPoint) {
        if (startBuild != null && startBuild.getAbsolutePos().dst(attachPoint) > 0.5f) {
            ((GameWorld)world).createThread(startBuild.getAbsolutePos(), attachPoint, threadType);
        }
    }

    @Override
    public void endContact(Body other) {
        contacts.remove(other);
    }

    @Override
    public void hit(float force) {
    }
}
