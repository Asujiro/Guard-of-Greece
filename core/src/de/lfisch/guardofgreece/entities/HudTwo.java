package de.lfisch.guardofgreece.entities;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.lfisch.guardofgreece.GuardOfGreece;

public class HudTwo {

    private PlayerTwo playerTwo;

    private TextureRegion heart;
    public HudTwo(PlayerTwo playerTwo) {
        this.playerTwo = playerTwo;

        Texture tex = GuardOfGreece.res.getTexture("hud");
        heart = new TextureRegion(tex, 58, 46, 13,12);
    }

    public void render(SpriteBatch sb){
        sb.begin();
        drawHeart(sb, playerTwo.getLives(), 70, 405);
        sb.end();

    }
    private void drawHeart(SpriteBatch sb, int s, float x, float y) {
        for(int i = 0; i < s; i++) {
            sb.draw(heart, x + i * 9, y);

        }}

}
