package de.doccrazy.ld33.game.ui;

import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.doccrazy.ld33.game.GameRenderer;
import de.doccrazy.ld33.game.world.GameInputListener;
import de.doccrazy.ld33.game.world.GameWorld;
import de.doccrazy.shared.game.ui.UiBase;

public class UiRoot extends UiBase<GameWorld, GameRenderer, GameInputListener> {
    private Toolbar toolbar;

	public UiRoot(Stage stage, GameWorld world, GameRenderer renderer) {
		super(stage, world, renderer);

        toolbar = new Toolbar(this);
        toolbar.setVisible(false);
        left().add(toolbar);

        add(new TimerLabel(world)).expandX().center();
        add(new ScoreLabel(world)).pad(5);

        getStage().addActor(new DeathLabel(getWorld()));
        getStage().addActor(new DeathLabel2(getWorld()));
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
