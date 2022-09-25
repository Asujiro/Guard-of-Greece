package de.lfisch.guardofgreece.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.lfisch.guardofgreece.GuardOfGreece;
import de.lfisch.guardofgreece.handlers.GameStateManager;
import de.lfisch.guardofgreece.handlers.MyInput;
import de.lfisch.guardofgreece.handlers.Save;


public class GameOver extends GameState{

    private TextureRegion[] score;

    private static final int BUTTON_HEIGHT = 74;
    private static final int BUTTON_WIDTH = 192;
    private static final int EXIT_BUTTON_Y = 50;
    private static final int SCORE_Y = 130;

    private static final int SCORE_FONT_Y = 250;
    private static final int GAME_OVER_Y = 300;
    private BitmapFont font;

    private boolean newHighScore;
    private char[] newName;
    private int currentChar;
    private ShapeRenderer sr;

    private static GlyphLayout glyphLayout = new GlyphLayout();
    public GameOver(GameStateManager gsm){
        super(gsm);

        cam.setToOrtho(false, GuardOfGreece.V_WIDTH, GuardOfGreece.V_HEIGHT);
        Texture tex = GuardOfGreece.res.getTexture("hud");
        score = new TextureRegion[11];
        for (int i = 0; i < 6; i++) {
            score[i] = new TextureRegion(tex, 0 + i * 9,0,9,9);
        }
        for (int i = 0; i < 5; i++){
            score[i + 6] = new TextureRegion(tex, 0 + i * 9, 9,9,9);
        }

        font = new BitmapFont();

        newHighScore = Save.gd.isHighScore(Save.gd.getTentativeScore());
        if (newHighScore){
            newName = new char[] {'A', 'A','A'};
            currentChar = 0;
        }
        sr = new ShapeRenderer();
    }

    /**
     * input
     */

    public void update(float dt) {
        handleInput();


    }

    /**
     * draws the screen
     */

    public void render() {
        Gdx.gl.glClearColor(0,0,0.15f,1400);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.setProjectionMatrix(cam.combined);

        sb.begin();

        String s;
        float w;

        s = "Game Over";
        glyphLayout.setText(font, s);
        w = glyphLayout.width;

        font.draw(sb, glyphLayout, (GuardOfGreece.V_WIDTH - w) / 2, GAME_OVER_Y);

        if (!newHighScore){
            sb.end();
            return;
        }
        s = "New High Score: " + Save.gd.getTentativeScore();
        glyphLayout.setText(font, s);
        w = glyphLayout.width;
        font.draw(sb, glyphLayout, (GuardOfGreece.V_WIDTH - w) / 2, SCORE_FONT_Y);

        for (int i = 0; i < newName.length; i++){
            font.draw(
                    sb,
                    Character.toString(newName[i]),
                    230 + 14 * i,
                    120
            );
        }
        sb.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.line(230 + 24 * currentChar, 100, 244 + 14 * currentChar,100);
        sr.end();
    }


    /**
     * Handles the in put for the Name
     */

    public void handleInput() {
        if (MyInput.isPressed(MyInput.BUTTON1)){
            if (newHighScore){
                Save.gd.addHighScore(Save.gd.getTentativeScore(), new String(newName));
                Save.save();
            }
            gsm.setState(GameStateManager.MENU);
        }
        if (MyInput.isPressed(MyInput.BUTTON9)){
            if (newName[currentChar] == ' ') {
                newName[currentChar] = 'Z';
            }else {
                newName[currentChar]--;
                if (newName[currentChar] < 'A'){
                    newName[currentChar] = ' ';
                }
            }
        }

        if (MyInput.isPressed(MyInput.BUTTON10)){
            if (newName[currentChar] == ' ') {
                newName[currentChar] = 'A';
            }else {
                newName[currentChar]++;
                if (newName[currentChar] > 'Z'){
                    newName[currentChar] = ' ';
                }
            }
        }

        if (MyInput.isPressed(MyInput.BUTTON2)){
            if (currentChar < newName.length - 1) {
                currentChar++;
            }
        }
        if (MyInput.isPressed(MyInput.BUTTON3)){
            if(currentChar > 0) {
                currentChar--;
            }
        }


    }
    public void dispose() {

    }
}
