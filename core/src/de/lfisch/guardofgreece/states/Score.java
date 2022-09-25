package de.lfisch.guardofgreece.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import de.lfisch.guardofgreece.GuardOfGreece;
import de.lfisch.guardofgreece.handlers.GameStateManager;
import de.lfisch.guardofgreece.handlers.Save;

public class Score extends GameState {
    private static GlyphLayout glyphLayout = new GlyphLayout();
    private BitmapFont font;
    private long[] highScores;
    private String[] names;

    public Score(GameStateManager gsm) {
        super(gsm);
        font = new BitmapFont();
        cam.setToOrtho(false, GuardOfGreece.V_WIDTH, GuardOfGreece.V_HEIGHT);

        Save.load();
        highScores = Save.gd.getHighScores();
        names = Save.gd.getNames();
    }

    public void handleInput() {

    }


    public void update(float dt) {

    }


    public void render() {
        Gdx.gl.glClearColor(0,0,0.15f,1400);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.setProjectionMatrix(cam.combined);

        sb.begin();

        String s;
        float w;

        s = "High Scores";
        glyphLayout.setText(font, s);
        w = glyphLayout.width;
        font.draw(sb, glyphLayout, (GuardOfGreece.V_WIDTH - w) / 2, 300);

        for (int i = 0; i < highScores.length; i++){
            s = String.format(
                    "%2d. %7s %s", i + 1, highScores[i], names[i]
            );
            glyphLayout.setText(font,s);
            w = glyphLayout.width;
            font.draw(sb, s, (GuardOfGreece.V_WIDTH - w) / 2, 270 - 20 * i);
        }

        sb.end();
    }


    public void dispose() {

    }
}
