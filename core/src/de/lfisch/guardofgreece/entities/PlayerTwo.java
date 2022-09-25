package de.lfisch.guardofgreece.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import de.lfisch.guardofgreece.GuardOfGreece;

public class PlayerTwo extends B2DSprites{

    private boolean powerUp = false;
    private int lives;

    private boolean rotation;

    public PlayerTwo(Body body) {
        super(body);
        this.lives = 3;
        Texture tex = GuardOfGreece.res.getTexture("playertwo");
        TextureRegion[] sprites = TextureRegion.split(tex, 32, 64)[0];

        setAnimation(sprites, 1 / 12f);

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

    public int getLives() {return lives;
    }
}
