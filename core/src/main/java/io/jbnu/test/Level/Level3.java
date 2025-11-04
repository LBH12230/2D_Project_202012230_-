package io.jbnu.test.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import io.jbnu.test.Collision.Block;
import io.jbnu.test.Collision.Ground;
import io.jbnu.test.Collision.Flag;
import io.jbnu.test.GameCharacter;

public class Level3 extends LevelBase {

    private Texture flagTexture;
    private Array<Block> blocks;
    private Array<Ground> grounds;
    private Flag flag;


    private float timeLimit = 30f; // 제한 시간 (초)
    private float currentTime = 0f;
    private boolean timeOver = false;
    private Texture blockTexture; // 모든 블록이 동일한 텍스처를 사용한다고 가정
    private Texture groundTexture;
    public Level3() {
        // 텍스처 로드 (실제 경로 사용)
        blockTexture = new Texture("box.png");
        groundTexture = new Texture("Ground.png");
        flagTexture = new Texture("Flag.png");

        blocks = new Array<>();
        grounds = new Array<>();




        // 초기 레벨 로드 호출
        loadGround(6, 500f);
        loadBlock();
    }

    public Flag getFlag(){
        return flag;
    }

    public Array<Block> getBlocks(){
        return blocks;
    }
    public Array<Ground> getGrounds() { return grounds; }

    // 로드 로직 구현
    private void loadGround(int numberOfGround, float spaceSize) {
        float startX = 0; // 첫 번째 블록이 시작할 X 좌표
        float startY = 0; // 바닥이 깔릴 Y 좌표

        for (int i = 0; i < numberOfGround; i++) {
            // 블록을 옆으로 나란히 배치: X 좌표는 이전 블록 + 블록 크기
            float x = startX + (i * spaceSize);
            // 새 Block 인스턴스를 생성하고 리스트에 추가
            grounds.add(new Ground(x, startY, groundTexture));

        }
        flag = new Flag(grounds.get(numberOfGround-1).getBound().x + 300f,50, flagTexture);

    }
    private void loadBlock() {

        float x;
        float y;
        for (int i = 0; i < 4; i++) {
            for(int j = 3; j>=i; j--){
                x = 300 + (j * 100);
                y = 55+(i*100);
                blocks.add(new Block(x, y, blockTexture));
            }
        }
        for (int i = 0; i < 4; i++) {
            for(int j = 0; j<4-i; j++){
                x = 1000 + (i * 100);
                y = 55+(j*100);
                blocks.add(new Block(x, y, blockTexture));
            }
        }
        for (int i = 0; i < 3; i++) {
            x = 1600;
            y = 55 + (i * 100);
            blocks.add(new Block(x, y, blockTexture));
        }
        for (int i = 0; i < 3; i++) {
            x = 1900;
            y = 500 -(i * 100);
            blocks.add(new Block(x, y, blockTexture));
        }


        for (int i = 0; i < 4; i++) {
            for(int j = 0; j<4; j++){
                x = 2300 + (j * 100);
                y = 55+(i*100);
                blocks.add(new Block(x, y, blockTexture));
            }

        }

    }
    public void update(float delta) {
        currentTime += delta;
        if (currentTime >= timeLimit) {
            timeOver = true;
        }
    }

    public void render(SpriteBatch batch) {
        for (Ground ground : grounds){
            ground.render(batch);
        }
        for (Block block : blocks){
            block.render(batch);
        }
        flag.render(batch);
    }
    public boolean isCleared(GameCharacter player) {
        if (timeOver) { return false;} // 타임오버면 실패
        Rectangle playerBounds = player.sprite.getBoundingRectangle();
        return playerBounds.overlaps(flag.getBound());
    }
    @Override
    public int getLevel(){
        return 3;
    }
    @Override
    public boolean isTimeOver() { return timeOver; }
    public float getRemainTime() { return Math.max(0, timeLimit - currentTime); }//최소 0초까지만


    @Override
    public void dispose() {
        blockTexture.dispose();
        groundTexture.dispose();
    }

}
