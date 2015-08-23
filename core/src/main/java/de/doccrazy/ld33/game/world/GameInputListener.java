package de.doccrazy.ld33.game.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import de.doccrazy.ld33.data.ThreadType;
import de.doccrazy.ld33.game.ui.UiRoot;
import de.doccrazy.shared.game.world.GameState;

public class GameInputListener extends InputListener {
    private GameWorld world;
    private UiRoot root;
    private AttachedPoint start;
    //private ThreadType threadType = ThreadType.STRUCTURE;

    public GameInputListener(UiRoot root) {
        this.root = root;
        this.world = root.getWorld();
        reset();
    }

    @Override
    public boolean keyTyped(InputEvent event, char character) {
        switch (character) {
        case '1':
            //threadType = ThreadType.STRUCTURE;
            world.getPlayer().setThreadType(ThreadType.STRUCTURE);
            break;
        case '2':
            //threadType = ThreadType.STICKY;
            world.getPlayer().setThreadType(ThreadType.STICKY);
            break;
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
