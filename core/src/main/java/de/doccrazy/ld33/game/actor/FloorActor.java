package de.doccrazy.ld33.game.actor;

import com.badlogic.gdx.math.Vector2;

import de.doccrazy.shared.game.actor.ShapeActor;
import de.doccrazy.shared.game.world.BodyBuilder;
import de.doccrazy.shared.game.world.Box2dWorld;
import de.doccrazy.shared.game.world.ShapeBuilder;

public class FloorActor extends ShapeActor {
	private Vector2 size;

	public FloorActor(Box2dWorld world, Vector2 spawn, Vector2 size) {
		super(world, spawn, true);
		this.size = size;
	}

	@Override
	protected BodyBuilder createBody(Vector2 spawn) {
		return BodyBuilder.forStatic(spawn)
				.fixShape(ShapeBuilder.box(size.x/2, size.y/2));
	}

}
