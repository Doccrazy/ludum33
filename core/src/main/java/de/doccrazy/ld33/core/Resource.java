package de.doccrazy.ld33.core;

import de.doccrazy.ld33.resources.FontResources;
import de.doccrazy.ld33.resources.GfxResources;

public class Resource {
    public static GfxResources GFX;
    //public static SpriterResources SPRITER;
    public static FontResources FONT;
    //public static SoundResources SOUND;
    //public static MusicResources MUSIC;

    private Resource() {
    }

    public static void init() {
        GFX = new GfxResources();
        //SPRITER = new SpriterResources(GFX.getAtlas());
        FONT = new FontResources();
        //SOUND = new SoundResources();
        //MUSIC = new MusicResources();
    }
}

