package io.jbnu.test.Moving;

import com.badlogic.gdx.math.Vector2;

public class Dash {
    private boolean isDashing = false;      //대쉬하고 있는지
    private boolean dashPoint = true;       //대쉬1회제한
    private float dashTime = 0f;       // 대시시간 받는 곳
    private final float DASH_DURATION = 0.2f; // 대시 지속 시간 (초)
    private final float DASH_SPEED = 1500f; // 대시 속도
    private Vector2 dashDirection = new Vector2();  //8방향 대쉬를 위한 벡터

    public void startDash(Vector2 dashDirection, boolean isGrounded) {
        if (isDashing) { // 대쉬중일 때 2중대쉬 안되게
            return;
        }
        if (!dashPoint){ //대쉬포인트가 없으면 리턴
            return;
        }
        isDashing = true;
        dashTime = 0f;
        this.dashDirection.set(dashDirection).nor().scl(DASH_SPEED); // nor은 벡터의 크기를 1로 aksemfrh
        //scl은 대쉬속도만큼 스케일에 곱한다.


    }
    public void update(float delta, Vector2 velocity) {
        if (!isDashing) return;     //대쉬중이 아니라면 컷

        dashTime += delta;  //대쉬지속시간
        if(dashTime < DASH_DURATION){
            velocity.x = dashDirection.x;
            velocity.y = dashDirection.y;
            dashPoint = false;                 //대쉬한번 하면 땅에 닿기 전까지 대쉬 X
        }


        if (dashTime >= DASH_DURATION) {
            isDashing = false;
            velocity.scl(0.2f);
        }
    }

    public boolean isDashing() {
        return isDashing;
    }
    public void canDash(){
        dashPoint = true;
    }
}
