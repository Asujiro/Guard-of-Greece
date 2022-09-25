package de.lfisch.guardofgreece;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.lfisch.guardofgreece.handlers.*;

import java.io.FileNotFoundException;


public class GuardOfGreece extends Game {

	public static final String TITLE = "Guard of Greece";
	public static final int V_WIDTH = 720;
	public static final int V_HEIGHT = 480;
	public static final int SCALE = 2;
	public static final float STEP = 1 / 60f;


	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;

	private GameStateManager gsm;

	public static Content res;

	public void create () {

		Gdx.input.setInputProcessor(new MyInputProcessor());

		//load textures
		try {
			res = new Content();
			res.loadTexture("images/player.png", "player");
			res.loadTexture("images/player_two.png", "playertwo");
			res.loadTexture("images/coin.png", "coin");
			res.loadTexture("images/exit_button.png", "exit_button");
			res.loadTexture("images/play_button.png", "play_button");
			res.loadTexture("images/2players_button.png", "2player_button");
			res.loadTexture("images/bow.png", "bow");
			res.loadTexture("images/arrow.png", "arrow");
			res.loadTexture("images/hud.png", "hud");
			res.loadTexture("images/enemy.png", "enemy");
			res.loadTexture("images/goal.png", "goal");
			res.loadTexture("images/score_button.png", "score_button");
		}catch (NullPointerException e) {
			System.out.println(e);
			Gdx.app.exit();
		}


		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		gsm = new GameStateManager(this);
	}

	public void render() {

		Gdx.graphics.setTitle(TITLE + " FPS:" + Gdx.graphics.getFramesPerSecond());

		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render();
		MyInput.update();

	}

	public void dispose() {
	}

	public SpriteBatch getSpriteBatch(){return sb;}
	public OrthographicCamera getCamera() {return cam;}
	public OrthographicCamera getHudCamera() {return hudCam;}

	public void resize(int w, int h) {
	}

	public void pause() {}

	public void resume() {}

}
