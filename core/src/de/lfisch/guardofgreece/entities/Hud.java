package de.lfisch.guardofgreece.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.lfisch.guardofgreece.GuardOfGreece;

public class Hud {

    private Player player;

    private TextureRegion coin;
    private TextureRegion container;
    private TextureRegion[] font;
    private TextureRegion[] score;
    private TextureRegion bow;

    private TextureRegion heart;
    public Hud(Player player) {
        this.player = player;

        Texture tex = GuardOfGreece.res.getTexture("hud");

        container = new TextureRegion(tex, 0, 18, 32, 32);
        coin = new TextureRegion(tex,54,0,24,24);

        font = new TextureRegion[11];
        for (int i = 0; i < 6; i++) {
            font[i] = new TextureRegion(tex, 0 + i * 9,0,9,9);
        }
        for (int i = 0; i < 5; i++){
            font[i + 6] = new TextureRegion(tex, 0 + i * 9, 9,9,9);
        }
        bow = new TextureRegion(tex,32,32,26,26);

        heart = new TextureRegion(tex, 32, 18, 13,12);

        score = new TextureRegion[11];
        for (int i = 0; i < 6; i++) {
            score[i] = new TextureRegion(tex, 0 + i * 9,0,9,9);
        }
        for (int i = 0; i < 5; i++){
            score[i + 6] = new TextureRegion(tex, 0 + i * 9, 9,9,9);
        }
    }

    public void render(SpriteBatch sb){
        sb.begin();
        // draw item container
        sb.draw(container, 32, 420);

        if (player.getPowerUp()){
            sb.draw(bow, 35, 423);}


        // draw coin
        sb.draw(coin, 680, 425);
        // draw crystal amount
        drawString(sb, player.getNumCoins() + " / " + player.getTotalCoins(), 610, 432);
        drawHeart(sb, player.getLives(), 70, 420);
        drawScore(sb, "score" + player.getScore(), 610, 400);
        sb.end();

    }

    private void drawString(SpriteBatch sb, String s, float x, float y) {
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(c == '/') c = 10;
            else if(c >= '0' && c <= '9') c -= '0';
            else continue;
            sb.draw(font[c], x + i * 9, y);
        }}

        private void drawHeart(SpriteBatch sb, int s, float x, float y) {
            for(int i = 0; i < s; i++) {
                sb.draw(heart, x + i * 9, y);

    }}

    private void drawScore(SpriteBatch sb, String s, float x, float y) {
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(c == '/') c = 10;
            else if(c >= '0' && c <= '9') c -= '0';
            else continue;
            sb.draw(score[c], x + i * 9, y);
        }}

}
