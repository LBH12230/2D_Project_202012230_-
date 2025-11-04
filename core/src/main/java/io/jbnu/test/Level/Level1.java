package io.jbnu.test.Level;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;


import io.jbnu.test.Collision.Block;
import io.jbnu.test.Collision.Ground;
import io.jbnu.test.Collision.Flag;
import io.jbnu.test.GameCharacter;

public class Level1 extends LevelBase {
    private Flag flag;
    private Array<Block> blocks;
    private Array<Ground> grounds;
    private Texture flagTexture;
    private Texture blockTexture; // 모든 블록이 동일한 텍스처를 사용한다고 가정
    private Texture groundTexture;
    public Level1() {
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

        for (int i = 0; i < 5; i++) {
            float x = 800 + (i*300);
            float y = 55;;
            // 새 Block 인스턴스를 생성하고 리스트에 추가
            blocks.add(new Block(x, y, blockTexture));
        }

        for (int i = 0; i < 2; i++) {
            float x = 1700+(i*300);
            float y = 55+100;;
            // 새 Block 인스턴스를 생성하고 리스트에 추가
            blocks.add(new Block(x, y, blockTexture));
        }
        blocks.add(new Block(2000,255,blockTexture));
    }

    public void render(SpriteBatch batch) {
        for (Ground ground : grounds){
            ground.render(batch);
        }

        for (Block block : blocks) {
            block.render(batch);
        }

        flag.render(batch);

    }

    @Override
    public boolean isCleared(GameCharacter player) {
        if (flag == null) return false;
        Rectangle playerBounds = player.sprite.getBoundingRectangle();
        return flag.getBound().overlaps(playerBounds);
    }

    public int getLevel(){
        return 1;
    }
    @Override
    public void dispose() {
        if (blockTexture != null) blockTexture.dispose();
        if (groundTexture != null) groundTexture.dispose();
    }
}
