package de.doccrazy.ld33.resources;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import de.doccrazy.ld33.data.ThreadType;
import de.doccrazy.shared.core.ResourcesBase;
import de.doccrazy.shared.game.base.BlurUtils;

public class GfxResources extends ResourcesBase {
    public Texture threadStructure = textureWrap("thread-structure.png");
    public Texture threadSticky = textureWrap("thread-sticky.png");
    public Texture threadCounter = textureWrap("thread-counter.png");
    public Texture target = texture("target.png");
    public Texture level1fg = texture("level1-fg.png");
    public Texture level2fg = texture("level2.png");
    public Texture level2Tutorial = texture("level2-tutorial.png");
    //public Sprite[] fly = createBlurLevels(atlas.createSprite("fly"), 16);
    public Sprite dummy = atlas.createSprite("dummy");
    public Sprite counterweight = atlas.createSprite("counterweight");
    public Sprite dustMote = atlas.createSprite("dustMote");
    public Animation spiderIdle = new Animation(0.016f, atlas.findRegions("spider_idle/spider_idle"), PlayMode.LOOP);
    public Animation spiderJump = new Animation(0.016f, atlas.findRegions("spider_jump/spider_jump"), PlayMode.LOOP);
    public Animation[] fly1 = createBlurLevels(new Animation(0.010f, atlas.findRegions("fly_anim/fly"), PlayMode.LOOP), 4, 6);
    public Sprite iconNone = atlas.createSprite("icon/none");
    public Map<ThreadType, Sprite> icons = new HashMap<ThreadType, Sprite>();
    public Sprite selection = atlas.createSprite("selection");


    public GfxResources() {
        super("game.atlas");
    }

    private Texture textureWrap(String filename) {
        Texture tex = texture(filename);
        tex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        return tex;
    }

    private Animation[] createBlurLevels(Animation src, int radius, int levels) {
        Animation[] result = new Animation[levels];
        result[0] = src;
        for (int i = 1; i < levels; i++) {
            result[i] = blur(src, radius, i);
        }
        return result;
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

    private Animation blur(Animation src, int radius, int iterations) {
        Array<TextureRegion> newFrames = new Array<>();
        for (TextureRegion frame : src.getKeyFrames()) {
            frame.getTexture().getTextureData().prepare();
            Pixmap pixmap = frame.getTexture().getTextureData().consumePixmap();
            Pixmap blurred = BlurUtils.blur(pixmap, frame.getRegionX(), frame.getRegionY(), frame.getRegionWidth(), frame.getRegionHeight(),
                    0, 0, frame.getRegionWidth(), frame.getRegionHeight(), radius, iterations, frame.getTexture().getTextureData().disposePixmap());
            newFrames.add(new TextureRegion(new Texture(blurred)));
        }
        return new Animation(src.getFrameDuration(), newFrames, src.getPlayMode());
    }

    public void initIcons() {
        icons.put(ThreadType.STRUCTURE, atlas.createSprite("icon/structure"));
        icons.put(ThreadType.STICKY, atlas.createSprite("icon/sticky"));
        icons.put(ThreadType.COUNTERWEIGHT, atlas.createSprite("icon/counter"));
    }
}
