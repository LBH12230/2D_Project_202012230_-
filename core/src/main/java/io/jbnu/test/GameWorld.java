package io.jbnu.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;

import io.jbnu.test.Collision.Block;

import io.jbnu.test.Collision.CoinObject;
import io.jbnu.test.Level.Level1;
import io.jbnu.test.Level.Level2;
import io.jbnu.test.Level.Level3;
import io.jbnu.test.Level.LevelBase;

public class GameWorld {
    private Sound effectSound;
    public final float WORLD_GRAVITY = -9.8f * 200; // 초당 중력 값
    public final float FLOOR_LEVEL = 50;          // 바닥의 Y 좌표

    // --- 2. 월드 객체 ---
    private GameCharacter player;       //플레이어
    private LevelBase currentLevel;
    private int coinRemain =5;

    private Array<Rectangle> blocks;

    public GameWorld(Texture playerTexture, float worldWidth, float worldHeight,int level,Sound sound) {
        this.effectSound = sound;
        switch (level) {
            case 1: currentLevel = new Level1(); break;
            case 2: currentLevel = new Level2(); break;
            case 3: currentLevel = new Level3(); break;
        }
        player = new GameCharacter(playerTexture, 100, 100);
    }


    public void update(float delta) {
        player.updateDash(delta);
        if(getCurrnetLevel()==3){
            currentLevel.update(delta);
        }

        // --- 1. 힘 적용 (중력, 저항) ---
        if (!player.isGrounded) {
            player.velocity.y += WORLD_GRAVITY * delta;
        }

        // --- 2. '예상' 위치 계산 ---

        checkMove(delta);

        // --- 3 & 4. 충돌 검사 및 반응 ---

        // 스크린 바닥(FLOOR_LEVEL)과 충돌 검사
        if (player.isGrounded || player.position.y <= FLOOR_LEVEL) {
            player.velocity.y = 0;
            player.isGrounded = true;
            player.canDash();
            //System.out.println("대쉬충전완료");

        }else {
            player.isGrounded = false;
        }
        if(player.position.y <= FLOOR_LEVEL){player.position.set(player.position.x,FLOOR_LEVEL);}


        // --- 5. 최종 위치 확정 ---
        // 모든 충돌 계산이 끝난 '최종' 위치를 반영
        if(!player.isDashing()) {
            player.velocity.x *= 0.9f;
            if(0< player.velocity.x && player.velocity.x <0.1){ player.velocity.x =0; }
            if(-0.1 <player.velocity.x && player.velocity.x<0){ player.velocity.x =0; }
        }
        // --- 6. 그래픽 동기화 ---

        player.syncSpriteToPosition();
    }




    private void checkMove(float delta) {
        final float MIN_VELOCITY = 10f;

        float expectedX = player.position.x + player.velocity.x * delta;
        float expectedY = player.position.y + player.velocity.y * delta;

        boolean land = false;
        boolean collisionX = false;
        boolean collisionY = false;
        player.position.y = expectedY;
        Rectangle playerBounds = player.sprite.getBoundingRectangle();
        playerBounds.setPosition(player.position.x,player.position.y); //미리 옮겨봤을때 충돌이 난다면
        for(Block block : currentLevel.getBlocks()) {
            if (playerBounds.overlaps(block.getBound())) {
                if (player.velocity.y > 0) {

                    // 점프하다 천장의 충돌났을때 /  플레이어 탑 <= 블럭 y
                    // 플레이어의 Y 위치를 블록 아래쪽 경계에 맞춥니다.
                    player.position.y = block.getBound().y - player.sprite.getHeight()-0.5f;
                    player.velocity.y = 0;
                    //System.out.println("y+충돌"+ ", " + player.position.x+", "+player.position.y);

                } else if (player.velocity.y < 0) {
                    // 플레이어의 Y 위치를 블록 위쪽 경계에 맞춥니다.
                    player.position.y = block.getBound().y + block.getHeight()+0.5f;
                    collisionY = true;
                    player.velocity.y = 0;
                    //System.out.println("y-충돌" + ", " + player.position.x+", "+player.position.y);
                    land = true;

                }
                playerBounds.setPosition(player.position.x, player.position.y);
            }
        }
        player.position.x = expectedX;
        playerBounds.setPosition(player.position.x, player.position.y);
        for(Block block : currentLevel.getBlocks()) {


            if (playerBounds.overlaps(block.getBound())) {
                if (player.velocity.x > 0) {
                    // 오른쪽으로 이동 중 충돌 (블록의 왼쪽 면에 부딪힘)
                    // 플레이어의 X 위치를 블록 왼쪽 경계에 맞춥니다.
                    player.position.x = block.getBound().x - player.sprite.getWidth()-0.5f;
                    player.velocity.x = 0;

                } else if (player.velocity.x < 0) {

                        // 왼쪽으로 이동 중 충돌 (블록의 오른쪽 면에 부딪힘)
                        // 플레이어의 X 위치를 블록 오른쪽 경계에 맞춥니다.
                        player.position.x = block.getBound().x + Block.getWidth()+0.5f;
                        player.velocity.x = 0;

                } // x축의 중심-블럭 중심 > y축의 중심 - 블럭 중심 클경우 = x축먼저 move체크
            }
            playerBounds.setPosition(player.position.x, player.position.y);
        }
        if (Math.abs(player.velocity.x) < MIN_VELOCITY) player.velocity.x = 0;


        player.isGrounded = land;

        if (currentLevel.getLevel() == 2) {
            Level2 level2 = (Level2) currentLevel;
            CoinObject coin =level2.getCoin();

            if (coin != null) {
                playerBounds = player.sprite.getBoundingRectangle();
                Rectangle coinBounds = coin.bounds;

                if (playerBounds.overlaps(coinBounds)) {
                    level2.coinCollect();
                    effectSound.play();
                    coinRemain--;
                }
            }
            if (player.position.x < 0) player.position.x = 0;
            if (player.position.x + player.sprite.getWidth() > 800)
                player.position.x = 800 - player.sprite.getWidth();

            if (player.position.y < FLOOR_LEVEL)
                player.position.y = FLOOR_LEVEL;
            if (player.position.y + player.sprite.getHeight() > 600)
                player.position.y = 600 - player.sprite.getHeight();

        }
        //경계 제한 (플레이어가 월드 밖으로 나가지 않게)
        if (player.position.x < 0)
            player.position.x = 0;
        if (player.position.x + player.sprite.getWidth() > 3000)
            player.position.x = 3000 - player.sprite.getWidth();


        player.sprite.setPosition(player.position.x, player.position.y);
    }

    public boolean isGameClear() {
        return currentLevel.isCleared(player);
    }
    public boolean isTimeOver() {
        if (currentLevel.getLevel() == 3) {
            return currentLevel.isTimeOver();
        }
        return false;
    }
    public float getRemainTime(){
        if (currentLevel instanceof io.jbnu.test.Level.Level3) {
            return ((io.jbnu.test.Level.Level3) currentLevel).getRemainTime(); //즉시 불러오익
        }
        return -1f; // 다른 레벨은 시간 개념이 없으므로 음수 반환
    }

    // GameScreen으로부터 '점프' 입력을 받음
    public void onPlayerJump() {
        player.jump();
    }

    public void onPlayerLeft() {
        player.moveLeft();
    }

    public void onPlayerRight() {
        player.moveRight();
    }
    public void onDash(Vector2 dashDirection){
        player.dash(dashDirection);
    }


    // GameScreen이 그릴 수 있도록 객체를 제공
    public GameCharacter getPlayer() {
        return player;
    }

    public int getCurrnetLevel(){
        if (currentLevel.getLevel() == 1)
            return 1;
        else if(currentLevel.getLevel() == 2)
            return 2;
        else if(currentLevel.getLevel()==3)
            return 3;
        else
            return 0;

    }

    public int getCoinRemain() {
        return coinRemain;
    }

    public void drawLevel(SpriteBatch batch) {
        this.currentLevel.render(batch);

    }
    public void dispose() {
        if (currentLevel != null) {
            currentLevel.dispose();
            currentLevel = null;
        }

    }

}
