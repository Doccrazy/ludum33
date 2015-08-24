package de.doccrazy.ld33.data;

import com.badlogic.gdx.graphics.Texture;

import de.doccrazy.ld33.core.Resource;

public enum ThreadType {
    STRUCTURE(0.05f, Resource.GFX.threadStructure, 20f),
    STICKY(0.03f, Resource.GFX.threadSticky, 1.5f),
    COUNTERWEIGHT(0.04f, Resource.GFX.threadCounter, 30f);

    private final float width;
    private Texture texture;
    private float maxForce;

    ThreadType(float width, Texture texture, float maxForce) {
        this.width = width;
        this.texture = texture;
        this.maxForce = maxForce;
    }

    public float getWidth() {
        return width;
    }

    public Texture getTexture() {
        return texture;
    }

    public float getMaxForce() {
        return maxForce;
    }
}
