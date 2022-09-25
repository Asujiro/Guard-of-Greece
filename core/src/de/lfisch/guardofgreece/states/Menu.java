package de.lfisch.guardofgreece.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.lfisch.guardofgreece.GuardOfGreece;
import de.lfisch.guardofgreece.handlers.GameStateManager;


public class Menu extends GameState{

    private static final int BUTTON_HEIGHT = 74;
    private static final int BUTTON_WIDTH = 192;
    private static final int EXIT_BUTTON_Y = 50;
    public static final int TWO_PLAYER_BUTTON_Y = 130;
    public static final int PLAY_BUTTON_Y = 210;

    public static final int SCORE_BUTTON_Y = 290;



    Texture exit = GuardOfGreece.res.getTexture("exit_button");
    Texture play = GuardOfGreece.res.getTexture("play_button");
    Texture twoPlay = GuardOfGreece.res.getTexture("2player_button");
    Texture score = GuardOfGreece.res.getTexture("score_button");


    public Menu(GameStateManager gsm){
        super(gsm);

        cam.setToOrtho(false, GuardOfGreece.V_WIDTH, GuardOfGreece.V_HEIGHT);




    }

    public void handleInput() {

    }

    public void update(float dt) {



    }
// Fixing Mouse Input to fit the Coordinates of the buttons
    public Vector2 getInputInGameWorld () {
        Vector3 inputScreen = new Vector3(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), 0);
        Vector3 unprojected = cam.unproject(inputScreen);
        return new Vector2(unprojected.x, unprojected.y);
    }


    public void render() {
        Gdx.gl.glClearColor(0,0,0.15f,1400);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.setProjectionMatrix(cam.combined);

        sb.begin();



        int x = GuardOfGreece.V_WIDTH / 2 - BUTTON_WIDTH / 2;
        sb.draw(exit,x ,EXIT_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
            if ( getInputInGameWorld().x < x + BUTTON_WIDTH &&  getInputInGameWorld().x > x && GuardOfGreece.V_HEIGHT - getInputInGameWorld().y < EXIT_BUTTON_Y + BUTTON_HEIGHT && GuardOfGreece.V_HEIGHT - getInputInGameWorld().y > EXIT_BUTTON_Y) {
                if (Gdx.input.isTouched()) Gdx.app.exit();
        }

        sb.draw(twoPlay,x ,TWO_PLAYER_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        if ( getInputInGameWorld().x < x + BUTTON_WIDTH &&  getInputInGameWorld().x > x && GuardOfGreece.V_HEIGHT - getInputInGameWorld().y < TWO_PLAYER_BUTTON_Y+ BUTTON_HEIGHT && GuardOfGreece.V_HEIGHT - getInputInGameWorld().y > TWO_PLAYER_BUTTON_Y) {
            if (Gdx.input.isTouched()){
                Play.twoPlayers = true;
                Play.score = 0;
                gsm.setState(GameStateManager.PLAY);
            }
        }

        sb.draw(play,x ,PLAY_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        if ( getInputInGameWorld().x < x + BUTTON_WIDTH &&  getInputInGameWorld().x > x && GuardOfGreece.V_HEIGHT - getInputInGameWorld().y < PLAY_BUTTON_Y + BUTTON_HEIGHT && GuardOfGreece.V_HEIGHT - getInputInGameWorld().y > PLAY_BUTTON_Y) {

            if (Gdx.input.isTouched()) {
                Play.score = 0;
                gsm.setState(GameStateManager.PLAY);
            }
        }

        sb.draw(score,x ,SCORE_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        if ( getInputInGameWorld().x < x + BUTTON_WIDTH &&  getInputInGameWorld().x > x && GuardOfGreece.V_HEIGHT - getInputInGameWorld().y < SCORE_BUTTON_Y + BUTTON_HEIGHT && GuardOfGreece.V_HEIGHT - getInputInGameWorld().y > SCORE_BUTTON_Y) {

            if (Gdx.input.isTouched()) {
                gsm.setState(GameStateManager.SCORES);
            }
        }


        sb.end();
    }

    public void dispose() {

    }
}
