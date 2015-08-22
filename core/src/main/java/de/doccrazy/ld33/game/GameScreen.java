package de.doccrazy.ld33.game;

import com.badlogic.gdx.scenes.scene2d.Stage;

import de.doccrazy.ld33.game.ui.UiRoot;
import de.doccrazy.ld33.game.world.GameWorld;
import de.doccrazy.shared.game.BaseGameScreen;
import de.doccrazy.shared.game.world.Box2dWorld;

public class GameScreen extends BaseGameScreen<GameWorld, GameRenderer> {

	@Override
	protected GameWorld createWorld() {
		return new GameWorld();
	}

	@Override
	protected GameRenderer createRenderer(Box2dWorld world) {
		return new GameRenderer(world);
	}

	@Override
	protected void createUI(Stage uiStage, GameWorld world, GameRenderer renderer) {
		new UiRoot(uiStage, world, renderer);
	}
}
