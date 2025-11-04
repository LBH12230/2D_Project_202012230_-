package io.jbnu.test.Collision;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class CoinObject {

    public static int CoinWidth = 50;
    public static int CoinHeight = 50;

    public Vector2 position;
    public Sprite sprite;

    // 충돌 판정을 위한 사각 영역
    public Rectangle bounds;

    /**
     * 오브젝트 생성자
     * @param region 오브젝트 텍스처
     * @param startX 시작 X 위치
     * @param startY 시작 Y 위치
     */
    public CoinObject(float startX, float startY,Texture region) {
        this.position = new Vector2(startX, startY);

        this.sprite = new Sprite(region);
        this.sprite.setPosition(position.x, position.y);
        this.sprite.setSize(CoinWidth,CoinHeight);

        // 충돌 영역 초기화
        this.bounds = new Rectangle(position.x, position.y, sprite.getWidth(), sprite.getHeight());
    }

    /**
     * 매 프레임 업데이트
     */
    public void update(float delta) {
        // 스프라이트와 충돌 영역 위치 동기화
        sprite.setPosition(position.x, position.y);
        bounds.setPosition(position.x, position.y);
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
