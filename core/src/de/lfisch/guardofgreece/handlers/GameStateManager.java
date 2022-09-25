package de.lfisch.guardofgreece.handlers;

import de.lfisch.guardofgreece.GuardOfGreece;
import de.lfisch.guardofgreece.states.*;


import java.util.Stack;

public class GameStateManager {

    private GuardOfGreece game;
    private Stack<GameState> gameStates;

    private boolean twoPlayers;

    /**
     * Game State Id's
     */
    public static final int PLAY = 22454;
    public static final int MENU = 588674;
    public static final int GAMEOVER = 124314;
    public static final int WIN = 213214;
    public static final int SCORES = 234532;

    public GameStateManager(GuardOfGreece game){
        this.game = game;
        gameStates = new Stack<GameState>();
        pushState(MENU);
    }

    public GuardOfGreece game(){return game;}


    public void update(float dt){
        gameStates.peek().update(dt);
    }

    public  void render() {
        gameStates.peek().render();
    }

    /**
     *
     * gets game state with id
     */
    private GameState getState(int state){
        if(state == MENU) return new Menu(this);
        if(state == PLAY) return new Play(this);
        if (state == GAMEOVER) return new GameOver(this);
        if (state == SCORES) return new Score(this);
        return null;
    }

    /**
     *
     * gets called when new the state get switch
     */
    public void setState(int state) {
        popState();
        pushState(state);
    }

    /**
     * Sets the new game state
     *
     */
    public void pushState(int state) {
        gameStates.push(getState(state));
    }

    /**
     * removes the last games state
     */
    public void popState() {
        GameState g = gameStates.pop();
        g.dispose();
    }

}
