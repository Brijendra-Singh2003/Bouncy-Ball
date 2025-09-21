import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import scenes.GameScene;

public class Main extends ApplicationAdapter {
    private static final int HEIGHT = 720;
    private static final int WIDTH = HEIGHT * 9 / 16;

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("Bouncy ball");
        config.setWindowedMode(WIDTH, HEIGHT);
        new Lwjgl3Application(new GameScene(), config);
    }
}