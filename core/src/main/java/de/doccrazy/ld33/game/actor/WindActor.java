package de.doccrazy.ld33.game.actor;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.doccrazy.ld33.data.GameRules;
import de.doccrazy.ld33.game.world.GameWorld;
import de.doccrazy.shared.game.actor.WorldActor;

public class WindActor extends WorldActor<GameWorld> {
    private static final float CHANGE_EVERY = 10;
    private static final float MAX_WIND = 10;

    private float windSpeed, windTarget = MathUtils.random() * MAX_WIND;
    private Vector2 grav = new Vector2();
    private float stateTime;

    public WindActor(GameWorld world) {
        super(world);
        task.every(CHANGE_EVERY, (Void) -> {
            windSpeed = windTarget;
            windTarget = MathUtils.random() * MAX_WIND;
            stateTime = 0;
        });
    }

    @Override
    protected void doAct(float delta) {
        stateTime += delta;

        grav.set(GameRules.GRAVITY);
        grav.x += getWindSpeed();
        world.box2dWorld.setGravity(grav);
    }

    public float getWindSpeed() {
        return MathUtils.lerp(windSpeed, windTarget, MathUtils.clamp(stateTime / CHANGE_EVERY, 0, 1));
    }
}
