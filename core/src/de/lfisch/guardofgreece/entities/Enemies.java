package de.lfisch.guardofgreece.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import de.lfisch.guardofgreece.GuardOfGreece;

public class Enemies extends B2DSprites{

    private int lives = 3;
    private boolean rotation;
    public Enemies(Body body) {
        super(body);
        Texture tex = GuardOfGreece.res.getTexture("enemy");
        TextureRegion[] sprites = TextureRegion.split(tex, 32,64)[0];
        setAnimation(sprites, 1 / 8f);
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int damage){
        lives = lives - damage;
    }

    public void setRotation(boolean rotation) {
        this.rotation = rotation;
    }
    public boolean getRotation(){return rotation;}
}
