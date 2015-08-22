package de.doccrazy.ld33.game.ui;

import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.doccrazy.ld33.game.GameRenderer;
import de.doccrazy.ld33.game.world.GameInputListener;
import de.doccrazy.ld33.game.world.GameWorld;
import de.doccrazy.shared.game.ui.UiBase;

public class UiRoot extends UiBase<GameWorld, GameRenderer, GameInputListener> {
	public UiRoot(Stage stage, GameWorld world, GameRenderer renderer) {
		super(stage, world, renderer);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}

	@Override
	protected InputListener createUiInput() {
		return new UiInputListener(this);
	}

	@Override
	protected GameInputListener createGameInput() {
	    return new GameInputListener(this);
	}
}
