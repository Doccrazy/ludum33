package de.doccrazy.ld33.game.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import de.doccrazy.ld33.data.ThreadType;
import de.doccrazy.ld33.game.ui.UiRoot;
import de.doccrazy.shared.game.world.GameState;
import net.dermetfan.gdx.utils.ArrayUtils;

public class GameInputListener extends InputListener {
    private GameWorld world;
    private UiRoot root;

    public GameInputListener(UiRoot root) {
        this.root = root;
        this.world = root.getWorld();
        reset();
    }

    @Override
    public boolean keyTyped(InputEvent event, char character) {
        ThreadType type = null;
        switch (character) {
        case '1':
            world.getPlayer().setThreadType(null);
            break;
        case '2':
            type = ThreadType.STICKY;
            break;
        case '3':
            type = ThreadType.STRUCTURE;
            break;
        case '4':
            type = ThreadType.COUNTERWEIGHT;
            break;
        }
        if (type != null && ArrayUtils.contains(world.getLevel().getAllowedThreads(), type, false)) {
            world.getPlayer().setThreadType(type);
        }
        return false;
    }

    @Override
    public boolean mouseMoved(InputEvent event, float x, float y) {
        world.setMouseTarget(new Vector2(x, y));
        return true;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if (world.getGameState() != GameState.GAME) {
            return false;
        }
        if (button == 0) {
            world.getPlayer().setJump(true);
            return true;
            //start = world.createAttachedPoint(new Vector2(x, y), 0.02f);
        }
        if (button == 1) {
            world.getPlayer().setBuild(true);
            return true;
            /*Vector2 end = new Vector2(x, y);
            if (start != null && start.getAbsolutePos().dst(end) > 0.5f) {
                world.createThread(start.getAbsolutePos(), end, threadType);
            }*/
        }
        if (button == 2) {
            world.createFly(new Vector2(x, y));
        }
        return false;
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        if (world.getGameState() != GameState.GAME) {
            return;
        }
        if (button == 0) {
            world.getPlayer().setJump(false);
        }
        if (button == 1) {
            world.getPlayer().setBuild(false);
        }
    }

    public void reset() {

    }
}
