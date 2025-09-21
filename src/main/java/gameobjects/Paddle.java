package gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Paddle {
    public final float height;
    public final float width;
    private final float y;
    private float x;
    
    public Paddle(float height, float width) {
        this.height = height;
        this.width = width;
        this.y = height / 2 + 20;
    }

    public void update() {
        x = Gdx.input.getX();
    }
    
    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(x - width / 2, y - height / 2, width, height);
        shapeRenderer.end();
    }
}
