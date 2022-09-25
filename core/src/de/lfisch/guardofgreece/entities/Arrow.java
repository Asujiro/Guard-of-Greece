package de.lfisch.guardofgreece.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import de.lfisch.guardofgreece.GuardOfGreece;

public class Arrow extends B2DSprites{
    public Arrow(Body body) {
        super(body);

        Texture tex = GuardOfGreece.res.getTexture("arrow");
        TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];
        setAnimation(sprites, 1 / 8f);
    }
}
