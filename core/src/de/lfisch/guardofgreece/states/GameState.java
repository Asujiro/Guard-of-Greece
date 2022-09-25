package de.lfisch.guardofgreece.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.lfisch.guardofgreece.GuardOfGreece;
import de.lfisch.guardofgreece.handlers.BoundedCamera;
import de.lfisch.guardofgreece.handlers.GameStateManager;

public abstract class GameState {

    protected GameStateManager gsm;
    protected GuardOfGreece game;

    protected SpriteBatch sb;
    protected OrthographicCamera cam;
    protected OrthographicCamera hudCam;


    protected GameState(GameStateManager gsm) {
        this.gsm = gsm;
        game = gsm.game();
        sb = game.getSpriteBatch();
        cam = game.getCamera();
        hudCam = game.getHudCamera();

    }

    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render();
    public abstract void dispose();

}
