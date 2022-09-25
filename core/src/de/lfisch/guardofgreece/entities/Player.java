package de.lfisch.guardofgreece.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import de.lfisch.guardofgreece.GuardOfGreece;

public class Player extends B2DSprites{

    private int numCoins;
    private int totalCoins;
    private boolean powerUp = false;
    private int lives;
    private boolean rotation;

    private int score;
    public Player(Body body) {
        super(body);
        this.lives = 3;
        this.score = 0;
        Texture tex = GuardOfGreece.res.getTexture("player");
        TextureRegion[] sprites = TextureRegion.split(tex, 32, 64)[0];

        setAnimation(sprites, 1 / 12f);

    }

    public void collectCoin() {
        numCoins++;
    }

    public int getNumCoins() {
        return numCoins;
    }

    public void setTotalCoins(int i) {
        totalCoins = i;
    }

    public int getTotalCoins() {
        return totalCoins;
    }

    public boolean getPowerUp() {
        return powerUp;
    }

    public void setPowerUp(boolean i) {
        powerUp = i;
    }

    public void setLives(int i){ lives = i;
    }

    public void setRotation(boolean rotation) {
        this.rotation = rotation;
    }
    public boolean getRotation(){return rotation;}

    public int getLives(){
        return lives;

    }
    public int getScore(){
        return score;
    }
    public void setScore(int s){
        score = score + s;
    }
}
