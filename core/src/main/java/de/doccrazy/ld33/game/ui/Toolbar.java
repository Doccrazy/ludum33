package de.doccrazy.ld33.game.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import de.doccrazy.ld33.core.Resource;
import de.doccrazy.ld33.data.ThreadType;
import de.doccrazy.shared.game.world.GameState;

public class Toolbar extends Table {
    private UiRoot root;

    public Toolbar(UiRoot r) {
        this.root = r;
        pad(5);
        addTileButton(null);
        addTileButton(ThreadType.STRUCTURE);
        addTileButton(ThreadType.STICKY);
        addTileButton(ThreadType.COUNTERWEIGHT);
    }

    private void addTileButton(final ThreadType type) {
        Sprite img = type == null ? Resource.GFX.iconNone : Resource.GFX.icons.get(type);
        Button b = new Button(new SpriteDrawable(img) {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                if (type != null && !root.getWorld().hasAmmo(type)) {
                    return;
                }
                super.draw(batch, x, y, width, height);
                if (root.getWorld().getPlayer().getThreadType() == type) {
                    Resource.GFX.selection.setPosition(x-2, y-2);
                    Resource.GFX.selection.draw(batch);
                }
            }
        });
        add(b).width(32).height(32).expand().space(5);
        b.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                root.getWorld().getPlayer().setThreadType(type);
                Resource.SOUND.select.play();
            }
        });
        b.bottom().right().add(new AmmoLabel(root, type));
        //Image i = new Image(Resource.selection);
        //b.add(i);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setVisible(root.getWorld().getGameState() == GameState.GAME);
    }
}
