package de.doccrazy.ld33.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import de.doccrazy.ld33.core.Resource;
import de.doccrazy.ld33.game.world.GameWorld;
import de.doccrazy.shared.game.world.GameState;

public class DeathLabel extends Label {
	private GameWorld world;

	public DeathLabel(GameWorld world) {
		super("", new LabelStyle(Resource.FONT.retroBig, new Color(1f, 0.4f, 0.3f, 0.7f)));
		this.world = world;

		setAlignment(Align.center);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		setPosition(0, getStage().getHeight() * 0.7f);
		setWidth(getStage().getWidth());

		setVisible(world.isGameFinished());
		if (world.isGameFinished()) {
		    setText(world.getGameState() == GameState.VICTORY ? "You won!" : "You lost!");
		}
	}

}
