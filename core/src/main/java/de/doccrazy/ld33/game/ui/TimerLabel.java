package de.doccrazy.ld33.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import de.doccrazy.ld33.core.Resource;
import de.doccrazy.ld33.game.world.GameWorld;
import de.doccrazy.shared.game.world.GameState;

public class TimerLabel extends Label {
	private GameWorld world;

	public TimerLabel(GameWorld world) {
		super("", new LabelStyle(Resource.FONT.retroSmall, new Color(1f, 1f, 1f, 0.7f)));
		this.world = world;

		setAlignment(Align.right);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
        setVisible(world.getGameState() == GameState.GAME || world.isGameFinished());

		setText("Time: " + (int)(world.getRemainingTime()));
	}

}
