package de.lfisch.guardofgreece.handlers;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import de.lfisch.guardofgreece.entities.Enemies;


public class MyContactListener implements ContactListener {

    private int numFootContacts;
    private int numFootContactsTwo;
    private int numFootContactsEnemies;
    private Array<Body> coinsToRemove;

    private boolean goalTouched = false;

    private  Array<Body> arrowsToRemove;
    private  Array<Body> powerToRemove;
    private  Array<Body> enemiesToRemove;

    private int life = 3;
    private int lifeTwo = 3;
    private boolean powerUp = false;

    public MyContactListener(){
        super();
        coinsToRemove = new Array<Body>();
        arrowsToRemove = new Array<Body>();
        powerToRemove = new Array<Body>();
        enemiesToRemove = new Array<Body>();
    }

    // called when two fixtures start to collide
    public void beginContact(Contact c) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();


        if (fa == null || fb == null) return; //safety if fa or fb are null

        // checks if player one is on the ground
        if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
            numFootContacts++;
        }

        if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
            numFootContacts++;
        }

        // checks if playertwo one is on the ground
        if (fa.getUserData() != null && fa.getUserData().equals("footTwo")) {
            numFootContactsTwo++;
        }

        if (fb.getUserData() != null && fb.getUserData().equals("footTwo")) {
            numFootContactsTwo++;
        }

        // checks if enemy is on the ground
        if (fa.getUserData() != null && fa.getUserData().equals("enemyfoot")) {
            numFootContactsEnemies++;
        }

        if (fb.getUserData() != null && fb.getUserData().equals("enemyfoot")) {
            numFootContactsEnemies++;
        }

        //on coin contact

        if (fa.getUserData() != null && fa.getUserData().equals("coin")) {
            //remove coin
            coinsToRemove.add(fa.getBody());
        }

        if (fb.getUserData() != null && fb.getUserData().equals("coin")) {
            //remove coin
            coinsToRemove.add(fb.getBody());
        }

        //on arrow contact
        if (fa.getUserData() != null && fa.getUserData().equals("arrow")) {
            //remove arrow
            arrowsToRemove.add(fa.getBody());
        }

        if (fb.getUserData() != null && fb.getUserData().equals("arrow")) {
            //remove arrow
            arrowsToRemove.add(fb.getBody());
        }

        // on power up contact
        if (fa.getUserData() != null && fa.getUserData().equals("powerup")) {
            //remove power up
            powerToRemove.add(fa.getBody());
            powerUp = true;
        }

        if (fb.getUserData() != null && fb.getUserData().equals("powerup")) {
            //remove power up
            powerToRemove.add(fb.getBody());
            powerUp = true;
            }

        // when player collides with enemy he loses 1 life
        if (fa.getFilterData().categoryBits == Box2DVars.BIT_PLAYER){
            if (fb.getFilterData().categoryBits == Box2DVars.BIT_ENEMIES){
                life = life -1;

            }
        } else if (fb.getFilterData().categoryBits == Box2DVars.BIT_PLAYER){
            if (fa.getFilterData().categoryBits == Box2DVars.BIT_ENEMIES){
                life = life -1;

            }
        }


        // when playertwo collides with enemy he loses 1 life
        if (fa.getFilterData().categoryBits == Box2DVars.BIT_PLAYERTWO){
            if (fb.getFilterData().categoryBits == Box2DVars.BIT_ENEMIES){
                lifeTwo = lifeTwo -1;
            }
        } else if (fb.getFilterData().categoryBits == Box2DVars.BIT_PLAYERTWO){
            if (fa.getFilterData().categoryBits == Box2DVars.BIT_ENEMIES){
                lifeTwo = lifeTwo -1;
            }
        }


        // when enemy gets hit by arrow he loses 2 lives
        if (fa.getFilterData().categoryBits == Box2DVars.BIT_ENEMIES){
            if (fb.getFilterData().categoryBits == Box2DVars.BIT_ARROWS){
                ((Enemies)fa.getBody().getUserData()).setLives(2);
                if (((Enemies)fa.getBody().getUserData()).getLives() <= 0){
                    enemiesToRemove.add(fa.getBody());
                }
            }
        }else if (fb.getFilterData().categoryBits == Box2DVars.BIT_ENEMIES){
            if (fa.getFilterData().categoryBits == Box2DVars.BIT_ARROWS){
                ((Enemies)fb.getBody().getUserData()).setLives(2);
                if (((Enemies)fb.getBody().getUserData()).getLives() <= 0){
                    enemiesToRemove.add(fb.getBody());
                }
           }
            }

        // when enemy gets hit by player on head he loses one live
        if (fa.getFilterData().categoryBits == Box2DVars.BIT_ENEMYHEAD){
            if (fb.getFilterData().categoryBits == Box2DVars.BIT_PLAYER) {
                ((Enemies)fa.getBody().getUserData()).setLives(1);
                if (((Enemies)fa.getBody().getUserData()).getLives() <= 0){
                    enemiesToRemove.add(fa.getBody());
                }
            }
                }else if (fb.getFilterData().categoryBits == Box2DVars.BIT_ENEMYHEAD){
                    if (fa.getFilterData().categoryBits == Box2DVars.BIT_PLAYER) {
                    ((Enemies)fb.getBody().getUserData()).setLives(1);
                    if (((Enemies)fb.getBody().getUserData()).getLives() <= 0){
                        enemiesToRemove.add(fb.getBody());
                }
            }
        }


        // when enemy collides with death zone he gets killed
        if (fa.getFilterData().categoryBits == Box2DVars.BIT_ENEMIES){
            if (fb.getFilterData().categoryBits == Box2DVars.BIT_DEATH) {
                    enemiesToRemove.add(fa.getBody());
            }
        }else if (fb.getFilterData().categoryBits == Box2DVars.BIT_ENEMIES){
            if (fa.getFilterData().categoryBits == Box2DVars.BIT_DEATH) {
                    enemiesToRemove.add(fb.getBody());


            }
        }

        // when enemy collides with death zone he gets killed
        if (fa.getFilterData().categoryBits == Box2DVars.BIT_WIN){
            if (fb.getFilterData().categoryBits == Box2DVars.BIT_PLAYER) {
                goalTouched = true;
            }
        }else if (fb.getFilterData().categoryBits == Box2DVars.BIT_WIN){
            if (fa.getFilterData().categoryBits == Box2DVars.BIT_PLAYER) {
                goalTouched = true;
            }
        }



    }



    // called when two fixtures no longer collide
    public void endContact(Contact c ){
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();


        // checks if player one is no longer on the ground
        if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
            numFootContacts--;
        }

        if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
            numFootContacts--;
        }

        // checks if playertwo one is no longer on the ground
        if (fa.getUserData() != null && fa.getUserData().equals("footTwo")) {
            numFootContactsTwo--;
        }

        if (fb.getUserData() != null && fb.getUserData().equals("footTwo")) {
            numFootContactsTwo--;
        }

        // checks if enemy one is no longer on the ground
        if (fa.getUserData() != null && fa.getUserData().equals("enemyfoot")) {
            numFootContactsEnemies--;
        }

        if (fb.getUserData() != null && fb.getUserData().equals("enemyfoot")) {
            numFootContactsEnemies--;
        }

    }

    public boolean isPlayerOnGround() { return  numFootContacts > 0;}
    public boolean isPlayerOnGroundTwo() { return  numFootContactsTwo > 0;}

    public boolean isEnemyOnGround() { return  numFootContactsEnemies > 0;}
    public Array<Body> getCoinsToRemove(){return coinsToRemove;}
    public Array<Body> getArrowsToRemove(){return arrowsToRemove;}

    public Array<Body> getEnemiesToRemove(){return enemiesToRemove;}

    public Array<Body> getpowerToRemove(){return powerToRemove;}
    public void preSolve(Contact c, Manifold m) {
    }
    public void postSolve(Contact c, ContactImpulse ci) {
    }
    public boolean getPower(){return powerUp;}

    public int getDamage(){return life;}
    public int getDamageTwo(){return lifeTwo;}

    public boolean getGoalTouched(){return goalTouched;}
}
