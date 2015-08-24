package de.doccrazy.ld33.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import de.doccrazy.ld33.data.ThreadType;

public class AmmoLabel extends Label {
    private UiRoot root;
    private ThreadType type;

    public AmmoLabel(UiRoot root, ThreadType type) {
        super("", new LabelStyle(new BitmapFont(), new Color(0.5f, 1f, 0.5f, 1f)));
        this.root = root;
        this.type = type;

        setAlignment(Align.right | Align.bottom);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Integer a = root.getWorld().getAmmo().get(type);
        setText(a == null ? "" : a.toString());
    }
}
