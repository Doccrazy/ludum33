package de.doccrazy.ld33.game.actor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import de.doccrazy.ld33.core.Resource;
import de.doccrazy.ld33.data.ThreadType;
import de.doccrazy.ld33.game.world.AttachedPoint;
import de.doccrazy.ld33.game.world.GameWorld;
import de.doccrazy.shared.game.actor.ShapeActor;
import de.doccrazy.shared.game.base.CollisionListener;
import de.doccrazy.shared.game.base.KeyboardMovementListener;
import de.doccrazy.shared.game.base.MovementInputListener;
import de.doccrazy.shared.game.world.BodyBuilder;
import de.doccrazy.shared.game.world.ShapeBuilder;

public class PlayerActor extends ShapeActor<GameWorld> implements CollisionListener {
    private static final float RADIUS = 0.07f;
    private static final float JUMP_STRENGTH = 0.04f;
    private static final float JUMP_MIN = 0.03f;
    private static final float JUMP_MAX = 0.10f;
    private static final float OOB_KILL_TIME = 1f;

    private MovementInputListener movement;
    private List<Joint> attachJoints = new ArrayList<>();
    private RevoluteJointDef attachJointDef;
    private boolean jump;
    private ThreadType threadType = null;
    private AttachedPoint startBuild;
    private Map<Body, Vector2> contacts = new HashMap<>();
    private float oobTime, stateTime, dampingTime;
    private List<CaughtFlyActor> fliesToConsume = new ArrayList<>();

    public PlayerActor(GameWorld world, Vector2 spawn) {
        super(world, spawn, false);
        setUseRotation(false);
        setScale(4, 2.5f);
        setzOrder(50);
    }

    @Override
    protected BodyBuilder createBody(Vector2 spawn) {
        return BodyBuilder.forDynamic(spawn).gravityScale(0.5f).noRotate()
                .fixSensor().fixShape(ShapeBuilder.circle(RADIUS)).fixProps(1, 1, 1f);
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
        stateTime += delta;
        if (!world.hasAmmo(threadType)) {
            threadType = null;
        }
        boolean released = checkRelease(jump || isLooselyAttached());
        if (released) {
            if (threadType != null) {
                startBuild = world.createAttachedPoint(body.getPosition(), RADIUS, body);
            } else {
                startBuild = null;
            }
            jump(delta);
        }
        checkAttach(!jump);
        checkOOB(delta);
        if (stateTime - dampingTime < 0.5f) {
            body.setLinearDamping(5);
        } else {
            body.setLinearDamping(0);
        }
        for (CaughtFlyActor fly : fliesToConsume) {
            fly.kill();
            world.addScore(1);
        }
        fliesToConsume.clear();
        super.doAct(delta);
    }

    private void checkOOB(float delta) {
        boolean outOfBounds = !world.getLevel().getBoundingBox().contains(body.getPosition());
        if (outOfBounds) {
            oobTime += delta;
        } else {
            oobTime = 0;
        }
        if (oobTime > OOB_KILL_TIME) {
            kill();
        }
    }

    private boolean isLooselyAttached() {
        for (Joint joint : attachJoints) {
            if (joint.getBodyB() == null) {
                return true;
            }
            if (joint.getBodyB().getUserData() instanceof ThreadActor) {
                ThreadActor thread = (ThreadActor)joint.getBodyB().getUserData();
                if (thread.isLoose(joint.getBodyB())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void jump(float delta) {
        Vector2 jumpImpulse = getJumpImpulse();
        System.out.println(jumpImpulse.len());
        body.applyLinearImpulse(jumpImpulse, body.getPosition(), true);
        Resource.SOUND.jump.play();
    }

    private Vector2 getJumpImpulse() {
        Vector2 jumpImpulse = world.getMouseTarget().cpy().sub(body.getPosition());
        jumpImpulse = jumpImpulse.scl(JUMP_STRENGTH).clamp(JUMP_MIN, JUMP_MAX);
        return jumpImpulse;
    }

    private boolean checkRelease(boolean shouldRelease) {
        if (shouldRelease && !attachJoints.isEmpty()) {
            //release
            for (Joint joint : attachJoints) {
                if (joint.getBodyB() != null) {
                    world.box2dWorld.destroyJoint(joint);
                }
            }
            attachJoints.clear();
            return true;
        } else if (startBuild != null && movement.pollJump()) {
            ThreadActor newThread = createThread(null, body.getPosition());
            if (newThread != null) {
                attachToBody(newThread.getEndBody(), body.getPosition());
                jump = false;
            }
        } else if (attachJointDef != null) {
            attachJoints.add(world.box2dWorld.createJoint(attachJointDef));
            createThread(attachJointDef.bodyB, attachJointDef.bodyB.getWorldPoint(attachJointDef.localAnchorB));
            attachJointDef = null;
            dampingTime = stateTime;
            world.startNoBreakMode();
        }
        return false;
    }

    private boolean checkAttach(boolean shouldAttach) {
        if (shouldAttach && attachJoints.isEmpty() && !contacts.isEmpty()) {
            Entry<Body, Vector2> entry = contacts.entrySet().iterator().next();
            Body other = entry.getKey();
            Vector2 contactPoint = entry.getValue();
            //attach
            attachToBody(other, contactPoint);
            return true;
        }
        return false;
    }

    private void attachToBody(Body other, Vector2 contactPoint) {
        RevoluteJointDef jointDef = new RevoluteJointDef();
        Vector2 attachPoint;
        if (other.getUserData() instanceof ThreadActor) {
            //thread
            attachPoint = other.getPosition();
        } else {
            //level object
            attachPoint = contactPoint == null ? body.getPosition() : contactPoint;
            //move slightly to center of static body
            attachPoint.add(other.getPosition().cpy().sub(body.getPosition()).clamp(0, RADIUS));
            Resource.SOUND.grab.play();
        }
        jointDef.initialize(body, other, body.getPosition());
        jointDef.localAnchorA.setZero();
        jointDef.localAnchorB.set(other.getLocalPoint(attachPoint));
        attachJointDef = jointDef;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Animation anim = attachJoints.isEmpty() ? Resource.GFX.spiderJump: Resource.GFX.spiderIdle;
        TextureRegion frame = anim.getKeyFrame(stateTime);
        drawRegion(batch, frame);

        if (world.isGameInProgress() && world.getMouseTarget() != null) {
            Vector2 impulse = getJumpImpulse();
            float targetLen = impulse.len() * 20;
            float targetAngle = impulse.angle();
            batch.draw(Resource.GFX.target, body.getPosition().x, body.getPosition().y - 0.1f, 0, 0.1f, targetLen, 0.2f,
                    1, 1, targetAngle,
                    0, 0, Resource.GFX.target.getWidth(), Resource.GFX.target.getHeight(), false, false);
        }
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public ThreadType getThreadType() {
        return threadType;
    }

    public void setThreadType(ThreadType threadType) {
        this.threadType = threadType;
    }

    @Override
    public boolean beginContact(Body me, Body other, Vector2 normal, Vector2 contactPoint) {
        if (other.getUserData() instanceof CaughtFlyActor) {
            fliesToConsume.add((CaughtFlyActor)other.getUserData());
        } else {
            contacts.put(other, contactPoint);
        }
        return true;
    }

    private ThreadActor createThread(Body other, Vector2 attachPoint) {
        if (startBuild != null && startBuild.getAbsolutePos().dst(attachPoint) > 0.5f
                && world.hasAmmo(threadType)) {
            Vector2 startPos = startBuild.getAbsolutePos();
            if (startBuild.getBody().getUserData() instanceof ThreadActor) {
                startPos = startBuild.getBody().getPosition();
            }
            ThreadActor result = world.createThread(startPos, attachPoint, threadType);
            if (result != null) {
                world.addAmmo(threadType, -1);
                Resource.SOUND.twang[MathUtils.random(Resource.SOUND.twang.length-1)].play();
            }
            startBuild = null;
            dampingTime = stateTime;
            return result;
        }
        return null;
    }

    @Override
    public void endContact(Body other) {
        contacts.remove(other);
    }

    @Override
    public void hit(float force) {
    }
}
