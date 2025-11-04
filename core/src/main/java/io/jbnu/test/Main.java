package io.jbnu.test;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.audio.Music;



/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    public enum GameState {
        MENU,       //레벨 셀렉트
        RUNNING,    // 게임이 활발하게 진행 중인 상태
        PAUSED,
        CLEARED,
        GAMEOVER
    }
    private GameState currentState;

    private Texture menuTexture;    //메뉴텍스처
    private Texture pauseTexture;   //퍼즈텍스처
    private Texture playerTexture;
    private Texture buttonTexture1;  //버튼텍스처
    private Texture buttonTexture2;
    private Texture buttonTexture3;
    private Texture clearTexture;
    private Texture gameOverTexture;
    private SpriteBatch batch;

    Sound effectSound;

    GameWorld world;


    //카메라
    private OrthographicCamera camera;
    private Viewport viewport;
    private BitmapFont scoreFont;

    private final float WORLD_WIDTH = 800;
    private final float WORLD_HEIGHT = 600;

    private int level;
    private Music bgm;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(800,600, camera);
        camera.setToOrtho(false,800,600);

        batch = new SpriteBatch();
        effectSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        menuTexture = new Texture("Menu.jpg");
        pauseTexture = new Texture("pause.png");
        playerTexture = new Texture("t.png");
        buttonTexture1 = new Texture("1.jpg");
        buttonTexture2 = new Texture("2.jpg");
        buttonTexture3 = new Texture("3.jpg");
        clearTexture = new Texture("Clear.png");
        gameOverTexture = new Texture("GameOver.png");
        bgm = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));

        bgm.setLooping(true);   // 반복 재생
        bgm.play();

        currentState = GameState.MENU;      //시작은 메뉴화면
        //world = new GameWorld(playerTexture, WORLD_WIDTH, WORLD_HEIGHT);


    }

    @Override
    public void render() {
        ScreenUtils.clear(1f, 1f, 1f, 1f);
        if(currentState == GameState.MENU){
            renderMenu();
            handleMenuInput();
        }else if(currentState==GameState.RUNNING){
            input();
            logic();
            draw();
            endingCheck();
        }
        else{
            input();
            draw();
        }

    }
    private void renderMenu() {                 //메뉴창 렌더
        camera.position.set(400, 300, 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.draw(menuTexture, 0, 0, 800, 600  );
        batch.draw(buttonTexture1, 100, 250, 150, 150);
        batch.draw(buttonTexture2, 320, 250, 150, 150);
        batch.draw(buttonTexture3, 540, 250, 150, 150);
        batch.end();
    }
    private void handleMenuInput() {            //메뉴창 선택
        if (Gdx.input.justTouched()) {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPos);                   //실제 공간에서의 터치를 뷰포트 안으로 변환해주는 코드

            float x = touchPos.x;
            float y = touchPos.y;

            // 스테이지1 버튼 영역
            if (x > 100 && x < 250 && y > 250 && y < 400) {
                level = 1;
                world = new GameWorld(playerTexture, WORLD_WIDTH, WORLD_HEIGHT,level,effectSound);
                currentState = GameState.RUNNING;
            }
            if (x > 320 && x < 470 && y > 250 && y < 400) {
                level = 2;
                world = new GameWorld(playerTexture, WORLD_WIDTH, WORLD_HEIGHT,level,effectSound);
                currentState = GameState.RUNNING;
            }
            if (x > 540 && x < 690 && y > 250 && y < 400) {
                level = 3;
                world = new GameWorld(playerTexture, WORLD_WIDTH, WORLD_HEIGHT,level,effectSound);
                currentState = GameState.RUNNING;
            }


        }
    }



    private void logic(){
        world.update(Gdx.graphics.getDeltaTime());
        if(currentState!=GameState.MENU) {      //메뉴일때는 화면 고정해하니까
            if (world.getCurrnetLevel() != 2) {
                camera.position.set(world.getPlayer().position.x + playerTexture.getWidth() / 2, 300, 0);
            }
            float cameraX = Math.max(400, Math.min(2600, camera.position.x));

            camera.position.set(cameraX, 300, 0);
        }
        else{ camera.position.set(400,300,0);}
        camera.update();


    }

    private void draw(){
        batch.setProjectionMatrix(viewport.getCamera().combined);
        camera.update();

        scoreFont = new BitmapFont(); // 기본 비트맵 폰트 생성
        scoreFont.getData().setScale(2);
        scoreFont.setColor(0, 0, 0, 1); // 검정색 폰트

        batch.begin();
        world.getPlayer().draw(batch);
        world.drawLevel(batch);
        if(currentState==GameState.PAUSED){
            batch.draw(pauseTexture,camera.position.x-200,camera.position.y-150,400,400);
            writeText("Q : Menu / Enter : Continue");
        }
        if(currentState==GameState.CLEARED){
            batch.draw(clearTexture,camera.position.x-150,camera.position.y-150,300,300);
            if(level!=3){
                writeText("Q : Menu / Enter : Continue");
            }else{writeText("Game End / Q : Menu");}
        }
        if(currentState == GameState.RUNNING){
            if(world.getCurrnetLevel() == 1){
                scoreFont.draw(batch,  "Z : jump / X : Dash",40 , WORLD_HEIGHT - 80);
                scoreFont.draw(batch,  "R : Restart",40 , WORLD_HEIGHT - 120);
            }
            if(world.getCurrnetLevel() == 2){
                scoreFont.draw(batch,  ""+world.getCoinRemain(), WORLD_WIDTH/2, WORLD_HEIGHT/2);
            }
            if (world.getCurrnetLevel() == 3) {
                float remainTime = world.getRemainTime();
                String timeText = String.format("Time: %.1f", remainTime);
                scoreFont.draw(batch, timeText, camera.position.x-380, camera.position.y+280);
            }

        }
        if (currentState == GameState.GAMEOVER) {
            batch.draw(gameOverTexture, camera.position.x - 150, camera.position.y - 100, 300, 300);
            writeText("Q : Menu / R : Restart");
        }
        batch.end();
    }
    private void input() {

        boolean up = Gdx.input.isKeyPressed(Keys.UP);
        boolean down = Gdx.input.isKeyPressed(Keys.DOWN);
        boolean left = Gdx.input.isKeyPressed(Keys.LEFT);
        boolean right = Gdx.input.isKeyPressed(Keys.RIGHT);
        if(currentState==GameState.RUNNING) {
            if (right) {
                world.onPlayerRight();
            }
            if (left) {
                world.onPlayerLeft();
            }

            if (Gdx.input.isKeyPressed(Keys.Z)) {
                world.onPlayerJump();
            }
            if (Gdx.input.isKeyJustPressed(Keys.X)) {
                Vector2 dashDirection = new Vector2(0, 0);
                if (up) {
                    dashDirection.y += 1;
                }
                if (down) {
                    dashDirection.y -= 1;
                }
                if (right) {
                    dashDirection.x += 1;
                }
                if (left) {
                    dashDirection.x -= 1;
                }
                if (dashDirection.isZero()) {
                    return;
                } //아무것도 안눌렀을때는 아무것도 하지않음

                world.onDash(dashDirection);
            }
        }
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            currentState = GameState.PAUSED;
        }
        if(currentState == GameState.PAUSED){
            if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
                currentState = GameState.RUNNING;
            }

        }
        if(currentState == GameState.PAUSED || currentState ==GameState.CLEARED||currentState==GameState.GAMEOVER){
            if (Gdx.input.isKeyJustPressed(Keys.Q)) {
                currentState = GameState.MENU;
            }
        }
        if(currentState == GameState.CLEARED){
            if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
                if(level <3){
                    if (world != null) {
                        world.dispose();
                        world = null;
                    }
                    level++;
                    world = new GameWorld(playerTexture, WORLD_WIDTH, WORLD_HEIGHT,level,effectSound);
                    if (level == 2) {
                        camera.position.set(400, 300, 0);
                    } else {
                        camera.position.set(world.getPlayer().position.x + playerTexture.getWidth() / 2f, 300, 0);
                    }
                    camera.update();

                    currentState = GameState.RUNNING;
                }
                else {
                    currentState = GameState.MENU;}
            }
        }

        if(Gdx.input.isKeyJustPressed(Keys.R)){
            world = new GameWorld(playerTexture, WORLD_WIDTH, WORLD_HEIGHT,level,effectSound);
            currentState = GameState.RUNNING;
        }

    }
    private void writeText(String string){
        scoreFont.draw(batch,string,camera.position.x-150 ,camera.position.y-200);
    }

    private void endingCheck(){
        if(world.isGameClear() && (currentState != GameState.CLEARED)){
            currentState = GameState.CLEARED;
        }
        if (world.isTimeOver() && currentState != GameState.GAMEOVER)
            currentState = GameState.GAMEOVER;
    }




    @Override
    public void dispose() {
        playerTexture.dispose();
        menuTexture.dispose();
        buttonTexture1.dispose();
        buttonTexture2.dispose();
        buttonTexture3.dispose();
        pauseTexture.dispose();
        clearTexture.dispose();
        scoreFont.dispose();
        gameOverTexture.dispose();
        batch.dispose();
    }
    @Override
    public void resize(int width, int height){
        viewport.update(width, height, true);
    }
}
