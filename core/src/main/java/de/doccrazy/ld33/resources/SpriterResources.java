package de.doccrazy.ld33.resources;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.brashmonkey.spriter.Entity;
import de.doccrazy.shared.spriter.ResourcesBaseSpriter;

public class SpriterResources extends ResourcesBaseSpriter {
    public Entity char1 = entity("char1");

    public SpriterResources(TextureAtlas atlas) {
        super("Game.scml", atlas);
    }
}
