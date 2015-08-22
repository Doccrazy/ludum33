package de.doccrazy.ld33.resources;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;

import de.doccrazy.shared.core.ResourcesBase;

public class GfxResources extends ResourcesBase {
    public Texture net = texture("net.png");
    public Texture level1fg = texture("level1-fg.png");

    public GfxResources() {
        //super("game.atlas");
        net.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        net.setFilter(TextureFilter.Linear, TextureFilter.Linear);
    }
}
