package scenes;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import gameobjects.Ball;
import gameobjects.Block;
import gameobjects.Paddle;

enum GameState {
    IDLE,
    PAUSED,
    PLAYING,
};

public class GameScene extends ApplicationAdapter {
    private Sound gameOverSound;
    private Music backgroundMusic;
    private ShapeRenderer shapeRenderer;
    private Ball ball = null;
    private Paddle paddle;
    private GameState gameState = GameState.IDLE;
    ArrayList<Block> blocks = new ArrayList<>();
    private SpriteBatch batch;
    private BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        shapeRenderer = new ShapeRenderer();
        gameState = GameState.IDLE;
        paddle = new Paddle(10, 75);
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("core/assets/sounds/game over.mp3"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("core/assets/music/bgm.mp3"));

        generateBlocks();
        backgroundMusic.setLooping(true);
    }

    private void start() {
        blocks.clear();
        generateBlocks();
        ball = new Ball();
        gameState = GameState.PLAYING;
        backgroundMusic.play();
    }

    private void update() {
        boolean spacePressed = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);

        if (spacePressed && gameState != null) {
            switch (gameState) {
                case IDLE -> this.start();
                case PAUSED -> this.resume();
                case PLAYING -> this.pause();
                default -> {
                }
            }
        }

        if (gameState == GameState.PAUSED || gameState == GameState.IDLE) {
            return;
        }

        paddle.update();
        if (ball == null)
            return;

        if (blocks.isEmpty() && ball.getY() + ball.getRadii() < Gdx.graphics.getHeight() / 2) {
            generateBlocks();
        }

        ball.update();

        ball.checkPaddleCollision(paddle);
        for (int i = 0; i < blocks.size(); i++) {
            if (ball.checkBlockCollision(blocks.get(i))) {
                blocks.remove(i);
            }
        }

        if (ball.getY() < -ball.getRadii()) {
            ball = null;
            gameOverSound.play(1f);
            backgroundMusic.pause();
            gameState = GameState.IDLE;
        }
    }

    @Override
    public void pause() {
        gameState = GameState.PAUSED;
        backgroundMusic.pause();
    }

    @Override
    public void resume() {
        gameState = GameState.PLAYING;
        backgroundMusic.play();
    }

    @Override
    public void render() {
        update();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        paddle.render(shapeRenderer);

        for (var block : blocks) {
            block.render(shapeRenderer);
        }

        if (gameState == GameState.IDLE) {
            batch.begin();
            font.setColor(Color.WHITE);
            font.draw(batch, "Press space to start.", 10, Gdx.graphics.getHeight() / 4);
            batch.end();
        }

        if (ball == null)
            return;
        ball.render(shapeRenderer);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    private void generateBlocks() {
        float blockWidth = (Gdx.graphics.getWidth() - 6f * 10f) / 5;
        float blockHeight = 20;

        for (float y = Gdx.graphics.getHeight() - blockHeight / 2 - 10; y > Gdx.graphics.getHeight()
                / 2; y -= blockHeight + 10) {
            for (float x = 10 + blockWidth / 2; x < Gdx.graphics.getWidth(); x += blockWidth + 10) {
                blocks.add(new Block(x, y, blockWidth, blockHeight));
            }
        }
    }
}
