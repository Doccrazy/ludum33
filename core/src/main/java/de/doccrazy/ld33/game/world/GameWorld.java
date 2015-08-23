package de.doccrazy.ld33.game.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import box2dLight.RayHandler;
import de.doccrazy.ld33.data.GameRules;
import de.doccrazy.ld33.data.ThreadType;
import de.doccrazy.ld33.game.actor.FlyActor;
import de.doccrazy.ld33.game.actor.Level1Actor;
import de.doccrazy.ld33.game.actor.PlayerActor;
import de.doccrazy.ld33.game.actor.ThreadActor;
import de.doccrazy.shared.game.base.GamepadMovementListener;
import de.doccrazy.shared.game.world.Box2dWorld;
import de.doccrazy.shared.game.world.GameState;

public class GameWorld extends Box2dWorld {

    private PlayerActor[] players;
    private int[] scores = new int[2];
	private boolean multiplayer, waitingForRound, gameOver;
	private int round;
	private boolean partInit;
	private GamepadMovementListener moveListener;

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
            	players = new PlayerActor[2];
            	//addActor(players[0] = new PlayerActor(this, new Vector2(2, 0.25f), 0));
                //addActor(players[1] = new PlayerActor(this, new Vector2(GameRules.LEVEL_WIDTH-2, 0.25f), 1).flip());
                //addActor(new FloorActor(this, new Vector2(0, 5.5f), new Vector2(1, 1)));
                //addActor(new FloorActor(this, new Vector2(11, 5.5f), new Vector2(1, 1)));
                addActor(new Level1Actor(this));
                //addActor(new PunchingBagActor(this, new Vector2(8, 2.5f)));
                //addActor(new DummyActor(this, new Vector2(8, 0.0f)));
                break;
            case PRE_GAME:
            	round++;
                break;
            case GAME:
            	//Resource.MUSIC.intro.stop();
            	//Resource.MUSIC.victory.stop();
            	//Resource.MUSIC.fight[(int)(Math.random()*Resource.MUSIC.fight.length)].play();
                //players[0].setupKeyboardControl();
                stage.setKeyboardFocus(players[0]);
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
    		if (getStateTime() > 0.5f) {
    			transition(GameState.GAME);
    		}
    		break;
		default:
    	}
    }

    public PlayerActor getPlayer(int index) {
		return players[index];
	}

    public String getPlayerName(int index) {
    	return index > 0 ? (isMultiplayer() ? "P2" : "AI") : "P1";
    }

    public int getPlayerScore(int index) {
    	return scores[index];
    }

    public boolean isMultiplayer() {
    	return multiplayer;
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

    public AttachedPoint createAttachedPoint(Vector2 pos, float radius) {
        Body body = bodyAt(pos, radius);
        if (body != null) {
            return new AttachedPoint(body, body.getLocalPoint(pos));
        }
        return null;
    }

    public void createThread(Vector2 start, Vector2 end, ThreadType threadType) {
        addActor(new ThreadActor(this, start, end, threadType));
    }

    public void createFly(Vector2 spawn) {
        addActor(new FlyActor(this, spawn));
    }
}
