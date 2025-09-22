package gameobjects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Ball {
    private Sound blockHitSound;
    private Sound paddleHitSound;
    private Sound wallHitSound;
    private final float moveSpeed = 10f;
    private float radii = 50;
    private float x = 50;
    private float y = 50;
    private float vx;
    private float vy;

    public Ball() {
        this.radii = 10f;
        var randGenerator = new Random();
        vx = randGenerator.nextFloat(5);
        vy = (float) Math.sqrt(moveSpeed * moveSpeed - vx * vx);
        paddleHitSound = Gdx.audio.newSound(Gdx.files.internal("core/assets/sounds/paddle hit.mp3"));
        blockHitSound = Gdx.audio.newSound(Gdx.files.internal("core/assets/sounds/block hit.mp3"));
        wallHitSound = Gdx.audio.newSound(Gdx.files.internal("core/assets/sounds/wall hit.mp3"));
    }

    public Ball(float radii) {
        this.radii = radii;

        var randGenerator = new Random();
        vx = randGenerator.nextFloat(5);
        vy = (float) Math.sqrt(moveSpeed * moveSpeed - vx * vx);
    }

    public void update() {
        if (x + radii > Gdx.graphics.getWidth()) {
            vx = -Math.abs(vx);
            wallHitSound.play(1f);
        }
        if (x - radii < 0) {
            vx = Math.abs(vx);
            wallHitSound.play(1f);
        }
        if (y + radii > Gdx.graphics.getHeight()) {
            vy = -Math.abs(vy);
            wallHitSound.play(1f);
        }

        x += vx;
        y += vy;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getRadii() {
        return this.radii;
    }

    public void checkPaddleCollision(Paddle paddle) {
        float xDiff = paddle.getX() - this.x;
        float yDiff = paddle.getY() - this.y;

        float xDist = Math.abs(xDiff) - paddle.width / 2 - this.radii;
        float yDist = Math.abs(yDiff) - paddle.height / 2 - this.radii;

        if (xDist > 0 || yDist > 0) {
            return;
        }

        vx = -5f * xDiff / paddle.width; // 5f : paddle sensitivity
        vy = (float) Math.sqrt(moveSpeed * moveSpeed - vx * vx);

        paddleHitSound.play(1f);
    }

    public boolean checkBlockCollision(Block block) {
        float xDiff = block.x - this.x;
        float yDiff = block.y - this.y;

        float xDist = Math.abs(xDiff) - block.width / 2 - this.radii;
        float yDist = Math.abs(yDiff) - block.height / 2 - this.radii;

        if (xDist <= 0 && yDist <= 0) {
            if (xDist < yDist) {
                vy = Math.abs(vy) * (yDiff > 0 ? -1 : 1);
            } else {
                vx = Math.abs(vx) * (xDiff > 0 ? -1 : 1);
            }

            blockHitSound.play(0.75f);

            return true;
        }

        return false;
    }

    public void invertVx() {
        vx = -vx;
    }

    public void invertVy() {
        vy = -vy;
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GOLD);
        shapeRenderer.circle(x, y, radii);
        shapeRenderer.end();
    }
}
