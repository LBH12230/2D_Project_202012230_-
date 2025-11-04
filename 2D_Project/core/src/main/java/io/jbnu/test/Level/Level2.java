package io.jbnu.test.Level;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import io.jbnu.test.Collision.Block;
import io.jbnu.test.Collision.CoinObject;
import io.jbnu.test.Collision.Ground;
import io.jbnu.test.GameCharacter;

public class Level2 extends LevelBase {
    private Array<Block> blocks;
    private Array<Ground> grounds;

    private final float LEVEL2_WIDTH = 800f;
    private final float LEVEL2_HEIGHT = 600f;
    private final int TARGET_SCORE = 5;
    private int collectedCount =0;
    private Texture blockTexture; // 모든 블록이 동일한 텍스처를 사용한다고 가정
    private Texture groundTexture;
    private Texture coinTexture;
    private CoinObject coin;
    public Level2() {
        // 텍스처 로드 (실제 경로 사용)
        blockTexture = new Texture("box.png");
        groundTexture = new Texture("Ground.png");
        coinTexture = new Texture("coin.jpg");

        blocks = new Array<>();
        grounds = new Array<>();

        // 초기 레벨 로드 호출
        loadGround(2, 500f);
        spawnCoins();
    }

    public Array<Block> getBlocks(){
        return blocks;
    }
    public Array<Ground> getGrounds() { return grounds; }
    public CoinObject getCoin(){ return coin;}      //충돌체크 불러오기


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
    }

    private void spawnCoins(){

        float x = MathUtils.random(0, LEVEL2_WIDTH - CoinObject.CoinWidth); //코인크기에따라 감소
        float y = MathUtils.random(60, LEVEL2_HEIGHT - CoinObject.CoinHeight);; // 월드 높이 (예시)
        coin = new CoinObject(x, y,coinTexture);
    }

    public void coinCollect() {
        collectedCount++;
        System.out.println(" 코인(" + collectedCount + "/" + TARGET_SCORE + ")");
        if (collectedCount < TARGET_SCORE) {
            spawnCoins();
        } else {
            coin = null; // 다 모았으면 코인 삭제
        }
    }
    public void render(SpriteBatch batch) {
        for (Ground ground : grounds){
            ground.render(batch);
        }

        if(coin !=null) {
            coin.draw(batch);
        }

    }

    public boolean isCleared(GameCharacter player) {
        // 코인을 모두 수집한 경우 클리어
        return  collectedCount>= TARGET_SCORE;
    }
    public int getLevel(){
        return 2;
    }
    @Override
    public void dispose() {
        if (blockTexture != null) blockTexture.dispose();
        if (groundTexture != null) groundTexture.dispose();
        if (coinTexture != null) coinTexture.dispose();
    }
}
