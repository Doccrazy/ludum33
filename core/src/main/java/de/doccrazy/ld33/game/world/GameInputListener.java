package de.doccrazy.ld33.game.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import de.doccrazy.ld33.game.ui.UiRoot;
import de.doccrazy.shared.game.world.GameState;

public class GameInputListener extends InputListener {
    private GameWorld world;
    private UiRoot root;
    private AttachedPoint start;

    public GameInputListener(UiRoot root) {
        this.root = root;
        this.world = root.getWorld();
        reset();
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if (world.getGameState() != GameState.GAME) {
            return false;
        }
        if (button == 0) {
            start = world.createAttachedPoint(new Vector2(x, y), 0.02f);
        }
        if (button == 1) {
            Vector2 end = new Vector2(x, y);
            if (start != null && start.getAbsolutePos().dst(end) > 1) {
                world.createThread(start.getAbsolutePos(), end);
            }
        }
        return false;
    }

    public void reset() {

    }
}
