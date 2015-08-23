package de.doccrazy.ld33.game.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import box2dLight.RayHandler;
import de.doccrazy.ld33.data.GameRules;
import de.doccrazy.ld33.data.ThreadType;
import de.doccrazy.ld33.game.actor.FlyActor;
import de.doccrazy.ld33.game.actor.LeafActor;
import de.doccrazy.ld33.game.actor.Level;
import de.doccrazy.ld33.game.actor.Level2Actor;
import de.doccrazy.ld33.game.actor.PlayerActor;
import de.doccrazy.ld33.game.actor.ThreadActor;
import de.doccrazy.ld33.game.actor.WindActor;
import de.doccrazy.shared.game.actor.WorldActor;
import de.doccrazy.shared.game.world.Box2dWorld;
import de.doccrazy.shared.game.world.GameState;

public class GameWorld extends Box2dWorld {

    private PlayerActor player;
    private int score;
	private boolean waitingForRound, gameOver;
	private int round;
    private Vector2 mouseTarget;
    private Level level;
    private boolean renderForces;

	public GameWorld() {
        super(GameRules.GRAVITY);
        RayHandler.useDiffuseLight(true);
    }

    @Override
    protected void doTransition(GameState newState) {
        switch (newState) {
            case INIT:
            	//Resource.MUSIC.intro.play();
            	waitingForRound = false;
                //addActor(players[1] = new PlayerActor(this, new Vector2(GameRules.LEVEL_WIDTH-2, 0.25f), 1).flip());
                //addActor(new FloorActor(this, new Vector2(0, 5.5f), new Vector2(1, 1)));
                //addActor(new FloorActor(this, new Vector2(11, 5.5f), new Vector2(1, 1)));
                addActor((WorldActor)(level = new Level2Actor(this)));
                //addActor(new PunchingBagActor(this, new Vector2(8, 2.5f)));
                //addActor(new DummyActor(this, new Vector2(8, 0.0f)));
                addActor(player = new PlayerActor(this, level.getSpawn()));
                player.setThreadType(null);
                addActor(new WindActor(this));
                for (int i = 0; i < 50; i++) {
                    addActor(new LeafActor(this));
                }
                break;
            case PRE_GAME:
            	round++;
                break;
            case GAME:
            	//Resource.MUSIC.intro.stop();
            	//Resource.MUSIC.victory.stop();
            	//Resource.MUSIC.fight[(int)(Math.random()*Resource.MUSIC.fight.length)].play();
                player.setupKeyboardControl();
                stage.setKeyboardFocus(player);
                break;
            case VICTORY:
            case DEFEAT:
            	//for (Music m : Resource.MUSIC.fight) {
            	//	m.stop();
            	//}
            	//Resource.MUSIC.victory.play();
            	//players[0].setupController(null);
        }
    }

    @Override
    protected void doUpdate(float delta) {
    	switch (getGameState()) {
    	case GAME:
	    	/*if (players[1].isDead()) {
	    		scores[0]++;
	    		transition(GameState.VICTORY);
	    	} else if (players[0].isDead()) {
	    		scores[1]++;
	    		transition(GameState.DEFEAT);
	    	}
	    	if (scores[0] >= GameRules.ROUNDS_TO_WIN || scores[1] >= GameRules.ROUNDS_TO_WIN) {
	    		gameOver = true;
	    	}*/
    		break;
    	case PRE_GAME:
			transition(GameState.GAME);
    		break;
		default:
    	}
    }

    public PlayerActor getPlayer() {
		return player;
	}

    public int getPlayerScore() {
    	return score;
    }

    public int getRound() {
    	return round;
    }

    public boolean isGameOver() {
    	return gameOver;
    }

    public void waitingForRound() {
    	waitingForRound = true;
    }

    public boolean isWaitingForRound() {
    	return waitingForRound;
    }

    public AttachedPoint createAttachedPoint(Vector2 pos, float radius, Body exclude) {
        Body body = bodyAt(pos, radius, exclude);
        if (body != null) {
            return new AttachedPoint(body, body.getLocalPoint(pos));
        }
        return null;
    }

    public ThreadActor createThread(Vector2 start, Vector2 end, ThreadType threadType) {
        ThreadActor result = new ThreadActor(this, start, end, threadType);
        addActor(result);
        return result;
    }

    public void createFly(Vector2 spawn) {
        addActor(new FlyActor(this, spawn));
    }

    public void setMouseTarget(Vector2 mouseTarget) {
        this.mouseTarget = mouseTarget;
    }

    public Vector2 getMouseTarget() {
        return mouseTarget;
    }

    public Level getLevel() {
        return level;
    }

    public boolean isRenderForces() {
        return renderForces;
    }

    public void setRenderForces(boolean renderForces) {
        this.renderForces = renderForces;
    }
}
