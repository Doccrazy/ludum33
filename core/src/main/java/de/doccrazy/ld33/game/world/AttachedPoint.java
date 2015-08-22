package de.doccrazy.ld33.game.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class AttachedPoint {
    private Body body;
    private Vector2 relativePos;

    public AttachedPoint(Body body, Vector2 relativePos) {
        this.body = body;
        this.relativePos = relativePos;
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getRelativePos() {
        return relativePos;
    }

    public Vector2 getAbsolutePos() {
        return body.getWorldPoint(relativePos);
    }
}
