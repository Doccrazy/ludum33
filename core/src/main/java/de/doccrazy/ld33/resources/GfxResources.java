package de.doccrazy.ld33.resources;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;

import de.doccrazy.shared.core.ResourcesBase;
import de.doccrazy.shared.game.base.BlurUtils;

public class GfxResources extends ResourcesBase {
    public Texture threadStructure = textureWrap("thread-structure.png");
    public Texture threadSticky = textureWrap("thread-sticky.png");
    public Texture level1fg = texture("level1-fg.png");
    public Sprite[] fly = createBlurLevels(atlas.createSprite("fly"), 16);
    public Sprite dummy = atlas.createSprite("dummy");

    public GfxResources() {
        super("game.atlas");
    }

    private Texture textureWrap(String filename) {
        Texture tex = texture(filename);
        tex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        return tex;
    }

    private Sprite[] createBlurLevels(Sprite src, int levels) {
        Sprite[] result = new Sprite[levels];
        result[0] = src;
        for (int i = 1; i < levels; i++) {
            result[i] = blur(src, 2, i);
        }
        return result;
    }

    private Texture blur(Texture src, int radius, int iterations) {
        src.getTextureData().prepare();
        Pixmap pixmap = src.getTextureData().consumePixmap();
        Pixmap blurred = BlurUtils.blur(pixmap, radius, iterations, src.getTextureData().disposePixmap());
        return new Texture(blurred);
    }

    private Sprite blur(Sprite src, int radius, int iterations) {
        src.getTexture().getTextureData().prepare();
        Pixmap pixmap = src.getTexture().getTextureData().consumePixmap();
        Pixmap blurred = BlurUtils.blur(pixmap, src.getRegionX(), src.getRegionY(), src.getRegionWidth(), src.getRegionHeight(),
                0, 0, src.getRegionWidth(), src.getRegionHeight(), radius, iterations, src.getTexture().getTextureData().disposePixmap());
        return new Sprite(new Texture(blurred));
    }
}
