package de.lfisch.guardofgreece.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import de.lfisch.guardofgreece.handlers.Animation;

import static de.lfisch.guardofgreece.handlers.Box2DVars.PPM;

/**
 * defines all entities
 */
public class B2DSprites {

    protected Body body;
    protected Animation animation;
    protected float width;
    protected float height;

    public B2DSprites(Body body){
        this.body = body;
        animation = new Animation();
    }

    public void setAnimation(TextureRegion[] reg, float delay){
        animation.setFrames(reg, delay);
        width = reg[0].getRegionWidth();
        height = reg[0].getRegionHeight();
    }

    public void update(float dt) {
        animation.update(dt);
    }


    /**
     *
     *
     *
     */

    public void render(SpriteBatch sb, boolean flip) {
        sb.begin();
        if (flip){
            sb.draw(animation.getFrame(),
                    (body.getPosition().x * PPM - width / 2 - (-width)),
                    (body.getPosition().y * PPM - height / 2), -width, height);
        }else{
            sb.draw(animation.getFrame(),
                (body.getPosition().x * PPM - width / 2),
                (body.getPosition().y * PPM - height / 2));}
        sb.end();
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition(){
        return body.getPosition();
    }

    public float getWidth() {
        return width;}

    public float getHeight() {
        return height;}

}
