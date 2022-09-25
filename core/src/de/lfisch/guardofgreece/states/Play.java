package de.lfisch.guardofgreece.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import de.lfisch.guardofgreece.GuardOfGreece;
import de.lfisch.guardofgreece.entities.*;
import de.lfisch.guardofgreece.handlers.*;

import java.io.FileNotFoundException;

import static de.lfisch.guardofgreece.handlers.Box2DVars.*;

public class Play extends GameState{

    private boolean debug = true;
    private World world;
    private BoundedCamera b2dCam;
    private Box2DDebugRenderer b2dr;
    private MyContactListener cl;
    private TiledMap tileMap;
    private OrthogonalTiledMapRenderer tmr;
    private float tileSize;
    private float tileMapWidth;
    private float tileMapHeight;


    private Player player;
    private Vector2 playerMove;
    private Vector2 playerMoveTwo;
    private long lastShot = 0;
    private long lastShotPlayerTwo = 0;
    private Hud hud;
    private HudTwo hudTwo;

    private Goal goal;

    private long enemyJump = 0;


    private PlayerTwo playerTwo;
    private Array<Enemies> enemies;
    public static boolean twoPlayers;

    private Array<Arrow> arrows;
    private Array<Coin> coins;
    private Array<PowerUp> powerUp;

    public static int score;




    public Play(GameStateManager gsm) {
        super(gsm);
        // create box2d world
        world = new World(new Vector2(0, -9.81f), true); //true for bodys that are asleep get not calculated
        cl = new MyContactListener();
        world.setContactListener(cl);
        b2dr = new Box2DDebugRenderer();

        //create Player
        createPlayer();

        if (twoPlayers){
            createPlayerTwo();
        }

        // create tiles
        createTiles();

        createDeathzone();

        createMapEdge();

        createEnemies();

        createGoal();

        hud = new Hud(player);
        hudTwo = new HudTwo(playerTwo);
        // create coins
        createCoins();
        player.setTotalCoins(coins.size);

        // create powerUp
        createPowerUp();

        // set up box2d cam
        b2dCam = new BoundedCamera();
        b2dCam.setToOrtho(false, GuardOfGreece.V_WIDTH / PPM, GuardOfGreece.V_HEIGHT / PPM);
        b2dCam.setBounds(0, (tileMapWidth * tileSize) / PPM, 0, (tileMapHeight * tileSize) / PPM);


    }


    public void handleInput() {

        long cooldownTime = 1000;
        long time = System.currentTimeMillis();
        playerMove = player.getBody().getLinearVelocity();
        // player jump
        if (MyInput.isPressed(MyInput.BUTTON1)) {
            if(cl.isPlayerOnGround()){
                player.getBody().applyForceToCenter(0, 270f, true);
            }
        }

        if (MyInput.isPressed(MyInput.BUTTON4 )&& player.getPowerUp() == true) {
            if (time > lastShot + cooldownTime) {
                lastShot = time;
                createArrow(player.getBody(), player.getRotation());
            }
        }

        if (MyInput.isDown((MyInput.BUTTON2)) && player.getBody().getLinearVelocity().x < MAX_VELOCITY){
            player.getBody().applyLinearImpulse(1f, 0f, player.getBody().getPosition().x, player.getBody().getPosition().y, true);
            player.setRotation(false);
        }
        else if (MyInput.isDown((MyInput.BUTTON3)) && player.getBody().getLinearVelocity().x > MAX_VELOCITY_LEFT){
            player.getBody().applyLinearImpulse(-1f, 0f, player.getBody().getPosition().x, player.getBody().getPosition().y, true);
            player.setRotation(true);
        } else if((!MyInput.isDown((MyInput.BUTTON2)))){
            stopMovement();}

    }

    public void handeInputPlayerTwo(){
        long cooldownTime = 1000;
        long time = System.currentTimeMillis();

        playerMoveTwo = playerTwo.getBody().getLinearVelocity();



            if (MyInput.isPressed(MyInput.BUTTON7)) {
                if(cl.isPlayerOnGroundTwo()){
                    playerTwo.getBody().applyForceToCenter(0, 270f, true);
                }
            }

            if (MyInput.isPressed(MyInput.BUTTON8 ) && playerTwo.getPowerUp() == true) {
            if (time > lastShotPlayerTwo + cooldownTime) {
                lastShotPlayerTwo = time;
                createArrow(playerTwo.getBody(), playerTwo.getRotation());
            }
        }


            if (MyInput.isDown(MyInput.BUTTON6) && playerTwo.getBody().getLinearVelocity().x < MAX_VELOCITY){
                playerTwo.getBody().applyLinearImpulse(1f, 0f, playerTwo.getBody().getPosition().x, playerTwo.getBody().getPosition().y, true);
                playerTwo.setRotation(false);
            }
            else if (MyInput.isDown(MyInput.BUTTON5) && playerTwo.getBody().getLinearVelocity().x > MAX_VELOCITY_LEFT){
                playerTwo.getBody().applyLinearImpulse(-1f, 0f, playerTwo.getBody().getPosition().x, playerTwo.getBody().getPosition().y, true);
                playerTwo.setRotation(true);
            }else if (!MyInput.isDown(MyInput.BUTTON5) && !MyInput.isDown(MyInput.BUTTON6)){
                stopMovementTwo();
            }

    }

    public void stopMovement(){
        playerMove.x = 0f;
        player.getBody().setLinearVelocity(playerMove);
        }

    public void stopMovementTwo(){
        playerMoveTwo.x = 0f;
        playerTwo.getBody().setLinearVelocity(playerMoveTwo);
    }

    public void enemyMovement(){
        long JumpTime = 1000;
        long timeJump = System.currentTimeMillis();
        if (timeJump > enemyJump + JumpTime) {
            enemyJump = timeJump;

            int max = 1;
            int min = -1;
            int range = max - min + 1;

            int e = (int) (Math.random() * range) * min;
            switch (e){
                case -2: e = 1; break;
                case 0: e = -1; break;
            }
            for (int i = 0; i < enemies.size; i++) {
                Body en = enemies.get(i).getBody();
                Enemies eb = enemies.get(i);
                if (cl.isEnemyOnGround()) {
                    en.applyForceToCenter(0, 250f, true);
                    en.applyLinearImpulse((float) e,0,0,0,false);
                    switch (e){
                        case 1: eb.setRotation(false); break;
                        case -1: eb.setRotation(true); break;
                    }
                }
            }
        }
    }


    public void update(float dt) {
        //check input
        handleInput();


        enemyMovement();


        if (twoPlayers){
            handeInputPlayerTwo();
        }

        player.update(dt);
        if (twoPlayers){
            playerTwo.update(dt);
        }

        goal.update(dt);

        if(player.getBody().getPosition().y < 0) {
            twoPlayers = false;
            score = player.getScore();
            Save.gd.setTenativeScore(player.getScore());
            gsm.setState(GameStateManager.GAMEOVER);
        }

        if(twoPlayers){
            if(playerTwo.getBody().getPosition().y < 0) {
                twoPlayers = false;
                score = player.getScore();
                Save.gd.setTenativeScore(player.getScore());
                gsm.setState(GameStateManager.GAMEOVER);
            }
        }
        player.setPowerUp(cl.getPower());

        if (twoPlayers){
            playerTwo.setPowerUp(cl.getPower());
        }

            player.setLives(cl.getDamage());

         if (player.getLives() <= 0){
             twoPlayers = false;
             Save.gd.setTenativeScore(player.getScore());
             gsm.setState(GameStateManager.GAMEOVER);

         }

         if (twoPlayers){
             playerTwo.setLives(cl.getDamageTwo());
             if (playerTwo.getLives() <= 0){
                 twoPlayers = false;
                 Save.gd.setTenativeScore(score);
                 gsm.setState(GameStateManager.GAMEOVER);
             }
         }

        // update box2d world
        world.step(GuardOfGreece.STEP, 1, 1);

        // remove coins
        Array<Body> removeCoin = cl.getCoinsToRemove();
        for (int i = 0; i < removeCoin.size; i++){
            Body b = removeCoin.get(i);
            coins.removeValue((Coin) b.getUserData(), true);
            world.destroyBody(b);
            player.collectCoin();
            player.setScore(100);
        }
        removeCoin.clear();

        Array<Body> removeArrow = cl.getArrowsToRemove();
         for (int i = 0; i < removeArrow.size; i++){
            Body d = removeArrow.get(i);
            arrows.removeValue((Arrow) d.getUserData(), true);
            world.destroyBody(removeArrow.get(i));
        }
            removeArrow.clear();

        Array<Body> removePower = cl.getpowerToRemove();
        for (int i = 0; i < removePower.size; i++){
            Body p = removePower.get(i);
            powerUp.removeValue((PowerUp) p.getUserData(), true);
            world.destroyBody(removePower.get(i));
            player.setScore(500);
        }
        removePower.clear();

        // remove Enemy when live ist 0
        Array<Body> removeEnemy = cl.getEnemiesToRemove();
        for (int i = 0; i < removeEnemy.size; i++){
            Body e = removeEnemy.get(i);
            System.out.println(e.getUserData());
            enemies.removeValue((Enemies) e.getUserData(), true);
            world.destroyBody(e);
            player.setScore(50);
        }
        removeEnemy.clear();




        for (int i = 0; i < powerUp.size; i++){
            powerUp.get(i).update(dt);
        }

        for (int i = 0; i < coins.size; i++){
            coins.get(i).update(dt);
        }

        for (int i = 0; i < enemies.size; i++) {
            enemies.get(i).update(dt);
        }

        if (cl.getGoalTouched()){
            player.setScore(1000);
            twoPlayers = false;
            score = player.getScore();
            Save.gd.setTenativeScore(player.getScore());
            gsm.setState(GameStateManager.GAMEOVER);
        }



        if (arrows == null){return;
        }else {
            for (int i = 0; i < arrows.size; i++){
                arrows.get(i).update(dt);
            }
        }



    }


    public void render() {
        // clear screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //set camera to player
        cam.position.set(player.getPosition().x * PPM + GuardOfGreece.V_WIDTH / 4, GuardOfGreece.V_HEIGHT / 2, 0);
        cam.update();

        // draw tile map
        tmr.setView(cam);
        tmr.render();

        // draw hud
        sb.setProjectionMatrix(hudCam.combined);
        hud.render(sb);

        if (twoPlayers){
            sb.setProjectionMatrix(hudCam.combined);
            hudTwo.render(sb);
        }


        //draw player
        sb.setProjectionMatrix(cam.combined);
        player.render(sb, player.getRotation());

        if (twoPlayers){
            playerTwo.render(sb ,playerTwo.getRotation());
        }

        goal.render(sb, false);

        for (int i = 0; i < enemies.size; i++){
            enemies.get(i).render(sb, enemies.get(i).getRotation());
        }


        //draw coins
        for (int i = 0; i < coins.size; i++){
            coins.get(i).render(sb, false);
        }


        for (int i = 0; i < powerUp.size; i++){
            powerUp.get(i).render(sb, false);
        }

        // draw box2d
        if (debug){
            b2dCam.setPosition(player.getPosition().x + GuardOfGreece.V_WIDTH / 4 / PPM, GuardOfGreece.V_HEIGHT / 2 / PPM);
            b2dCam.update();
            b2dr.render(world, b2dCam.combined);
        }
        if (arrows == null) {
            return;
        }else {for (int i = 0; i < arrows.size; i++){
            arrows.get(i).render(sb, player.getRotation());
        }}



    }

    public void dispose() {

    }

    private void createPlayer(){
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        //create Player
        bdef.position.set(160 / PPM, 200 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);

        shape.setAsBox(13 / PPM,29 / PPM);
        fdef.shape = shape;
        fdef.filter.maskBits = BIT_GROUND | BIT_COINS | BIT_ENEMIES | BIT_ENEMYHEAD | BIT_DEATH | BIT_WIN;
        fdef.filter.categoryBits = BIT_PLAYER;
        body.createFixture(fdef);

        // create foot sensor
        shape.setAsBox(11 / PPM, 2 / PPM, new Vector2(0, -29 / PPM),0);
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_PLAYER;
        fdef.filter.maskBits = BIT_GROUND | BIT_DEATH | BIT_ENEMYHEAD | BIT_WIN;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("foot");

        // create Player
        player = new Player(body);
        body.setUserData("Player");

    }

    private void createPlayerTwo(){
            BodyDef bdef = new BodyDef();
            FixtureDef fdef = new FixtureDef();
            PolygonShape shape = new PolygonShape();


            //create Player
            bdef.position.set(130 / PPM, 200 / PPM);
            bdef.type = BodyDef.BodyType.DynamicBody;
            Body body = world.createBody(bdef);

            shape.setAsBox(13 / PPM,29 / PPM);
            fdef.shape = shape;
            fdef.filter.maskBits = BIT_GROUND | BIT_COINS | BIT_ENEMIES | BIT_ENEMYHEAD | BIT_DEATH | BIT_WIN;
            fdef.filter.categoryBits = BIT_PLAYER;
            body.createFixture(fdef);

            // create foot sensor
            shape.setAsBox(11 / PPM, 2 / PPM, new Vector2(0, -29 / PPM),0);
            fdef.shape = shape;
            fdef.filter.categoryBits = BIT_PLAYER;
            fdef.filter.maskBits = BIT_GROUND | BIT_DEATH | BIT_ENEMYHEAD | BIT_WIN;
            fdef.isSensor = true;
            body.createFixture(fdef).setUserData("footTwo");

            // create Player
            playerTwo = new PlayerTwo(body);
            body.setUserData("PlayerTwo");


    }

    public void createEnemies(){
        enemies = new Array<Enemies>();

        MapLayer layer = tileMap.getLayers().get("enemies");
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        for (MapObject mo : layer.getObjects()) {

            PolygonShape shape = new PolygonShape();
            float x = (float) mo.getProperties().get("x") / PPM;
            float y = (float) mo.getProperties().get("y") / PPM;
            //create Player
            bdef.position.set(x, y);
            bdef.type = BodyDef.BodyType.DynamicBody;
            Body body = world.createBody(bdef);

            shape.setAsBox(13 / PPM, 29 / PPM);
            fdef.shape = shape;
            fdef.filter.maskBits = BIT_GROUND | BIT_COINS | BIT_PLAYER | BIT_ARROWS | BIT_DEATH;
            fdef.filter.categoryBits = BIT_ENEMIES;
            body.createFixture(fdef);

            shape.setAsBox(11 / PPM, 2 / PPM, new Vector2(0, -29 / PPM),0);
            fdef.shape = shape;
            fdef.filter.categoryBits = BIT_PLAYER;
            fdef.filter.maskBits = BIT_GROUND | BIT_DEATH | BIT_ENEMYHEAD;
            fdef.isSensor = true;
            body.createFixture(fdef).setUserData("enemyfoot");


            shape.setAsBox(13 / PPM, 6 / PPM, new Vector2(0, 32 / PPM), 0);
            fdef.shape = shape;
            fdef.filter.categoryBits = BIT_ENEMYHEAD;
            fdef.filter.maskBits = BIT_GROUND | BIT_DEATH | BIT_PLAYER | BIT_ARROWS;
            fdef.isSensor = false;
            body.createFixture(fdef).setUserData("head");

            body.setLinearVelocity(new Vector2(-1f,0));

            Enemies e = new Enemies(body);
            body.setUserData("enemies");
            enemies.add(e);
            body.setUserData(e);
        }
    }

    private void createCoins(){
        coins = new Array<Coin>();

        MapLayer layer = tileMap.getLayers().get("coins");
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        for (MapObject mo : layer.getObjects()){

            bdef.type = BodyDef.BodyType.StaticBody;

            float x = (float) mo.getProperties().get("x") / PPM;
            float y = (float) mo.getProperties().get("y") / PPM;
            bdef.position.set(x, y);
            CircleShape cshape = new CircleShape();
            cshape.setRadius(8 / PPM);
            fdef.shape = cshape;
            fdef.isSensor = true;
            fdef.filter.categoryBits = BIT_COINS;
            fdef.filter.maskBits = BIT_PLAYER;

            Body body = world.createBody(bdef);
            body.createFixture(fdef).setUserData("coin");

            Coin c = new Coin(body);
            coins.add(c);

            body.setUserData(c);


        }
    }

    private void createPowerUp(){
        powerUp = new Array<PowerUp>();

        MapLayer layer = tileMap.getLayers().get("powerup");
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        for (MapObject mo : layer.getObjects()){

            bdef.type = BodyDef.BodyType.StaticBody;

            float x = (float) mo.getProperties().get("x") / PPM;
            float y = (float) mo.getProperties().get("y") / PPM;
            bdef.position.set(x, y);
            CircleShape cshape = new CircleShape();
            cshape.setRadius(8 / PPM);
            fdef.shape = cshape;
            fdef.isSensor = true;
            fdef.filter.categoryBits = BIT_COINS;
            fdef.filter.maskBits = BIT_PLAYER;

            Body body = world.createBody(bdef);
            body.createFixture(fdef).setUserData("powerup");

            PowerUp p = new PowerUp(body);
            powerUp.add(p);

            body.setUserData(p);


        }
    }

    private void createTiles(){
        try{
            tileMap = new TmxMapLoader().load("maps/test.tmx");
        } catch (NullPointerException e){
            System.out.println(e);
            Gdx.app.exit();
        }



        tmr = new OrthogonalTiledMapRenderer(tileMap);
        tileSize = (int) tileMap.getProperties().get("tilewidth");
        tileMapWidth = (int) tileMap.getProperties().get("width");
        tileMapHeight = (int) tileMap.getProperties().get("height");


        TiledMapTileLayer layer;
        layer = (TiledMapTileLayer) tileMap.getLayers().get("layer1");
        createGround(layer, BIT_GROUND);


    }

    private void createGround(TiledMapTileLayer layer, short bits) {
        float ts = layer.getTileWidth();

        for(int row = 0; row < layer.getHeight(); row++) {
            for(int col = 0; col < layer.getWidth(); col++) {

                // get cell
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);

                // check that there is a cell
                if(cell == null) continue;
                if(cell.getTile() == null) continue;

                // create body from cell
                BodyDef bdef = new BodyDef();
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((col + 0.5f) * ts / PPM, (row + 0.5f) * ts / PPM);
                ChainShape cshape = new ChainShape();
                Vector2[] v = new Vector2[4];
                v[0] = new Vector2(-ts / 2 / PPM, -ts / 2 / PPM);
                v[1] = new Vector2(-ts / 2 / PPM, ts / 2 / PPM);
                v[2] = new Vector2(ts / 2 / PPM, ts / 2 / PPM);
                v[3] = new Vector2(ts/ 2 / PPM, -ts / 2 / PPM);
                cshape.createChain(v);
                FixtureDef fdef = new FixtureDef();
                fdef.friction = 0;
                fdef.shape = cshape;
                fdef.filter.categoryBits = bits;
                fdef.filter.maskBits = BIT_PLAYER | BIT_ARROWS | BIT_ENEMIES;
                world.createBody(bdef).createFixture(fdef);
                cshape.dispose();

            }
        }
    }

    public void createDeathzone(){

        MapLayer layer = tileMap.getLayers().get("death");
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        for (MapObject mo : layer.getObjects()){

            bdef.type = BodyDef.BodyType.StaticBody;
            float x = (float) mo.getProperties().get("x") / PPM;
            float y = (float) mo.getProperties().get("y") / PPM;
            bdef.position.set(x, y);
            shape.setAsBox(2240 /PPM , tileSize /PPM );
            fdef.shape = shape;
            fdef.isSensor = true;
            fdef.filter.categoryBits = BIT_DEATH;
            fdef.filter.maskBits = BIT_PLAYER | BIT_ARROWS | BIT_ENEMIES;
            Body body = world.createBody(bdef);
            body.createFixture(fdef).setUserData("death");
        }
    }

    public void createMapEdge(){
        MapLayer layer = tileMap.getLayers().get("edge");
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        for (MapObject mo : layer.getObjects()){

            bdef.type = BodyDef.BodyType.StaticBody;
            float x = (float) mo.getProperties().get("x") / PPM;
            float y = (float) mo.getProperties().get("y") / PPM;
            bdef.position.set(x, y);
            shape.setAsBox(tileSize /PPM , 480 /PPM );
            fdef.shape = shape;
            fdef.filter.categoryBits = BIT_GROUND;
            fdef.filter.maskBits = BIT_PLAYER | BIT_ARROWS | BIT_ENEMIES;
            fdef.friction = 0;
            Body body = world.createBody(bdef);
            body.createFixture(fdef).setUserData("ground");
        }
    }

    private void createGoal(){
        MapLayer layer = tileMap.getLayers().get("win");
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        for (MapObject mo : layer.getObjects()){

            bdef.type = BodyDef.BodyType.StaticBody;

            float x = (float) mo.getProperties().get("x") / PPM;
            float y = (float) mo.getProperties().get("y") / PPM;
            bdef.position.set(x, y);
            CircleShape cshape = new CircleShape();
            cshape.setRadius(8 / PPM);
            fdef.shape = cshape;
            fdef.isSensor = true;
            fdef.filter.categoryBits = BIT_WIN;
            fdef.filter.maskBits = BIT_PLAYER;

            Body body = world.createBody(bdef);
            body.createFixture(fdef).setUserData("goal");

            goal = new Goal(body);
            body.setUserData(goal);

        }
    }


    public void createArrow(Body play, boolean rotation){
        arrows = new Array<Arrow>();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();


        bdef.type = BodyDef.BodyType.DynamicBody;
        if (rotation == false){
            bdef.position.set((play.getPosition().x + 5 / PPM), (play.getPosition().y));
        }else {
            bdef.position.set((play.getPosition().x - 5 / PPM), (play.getPosition().y));
        }

        bdef.bullet = true;

        shape.setAsBox(5 / PPM,2 / PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_ARROWS;
        fdef.filter.maskBits = BIT_GROUND | BIT_DEATH | BIT_ENEMIES;
        fdef.density = 22f;
        Body body = world.createBody(bdef);
        body.createFixture(fdef).setUserData("arrow");
        if (rotation == false){
            body.applyLinearImpulse(1f , 0.1f, 0,0, true);
        }else {
            body.applyLinearImpulse(-1f , 0.1f, 0,0, true);
        }


        Arrow c = new Arrow(body);
        arrows.add(c);

        body.setUserData(c);

    }



}
