package cat.flx.sprite;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        setContentView(R.layout.activity_main);
        GameView gameView = (GameView) findViewById(R.id.view);
        game = new Game(this);
        gameView.setGame(game);
    }

    @Override public void onResume() {
        super.onResume();
        game.getAudio().startMusic();
    }

    @Override public void onPause() {
        game.getAudio().stopMusic();
        super.onPause();
    }

    @Override public boolean dispatchKeyEvent(KeyEvent event) {
        boolean down = (event.getAction() == KeyEvent.ACTION_DOWN);
        switch(event.getKeyCode()) {
            case KeyEvent.KEYCODE_A:
                game.keyLeft(down); break;
            case KeyEvent.KEYCODE_D:
                game.keyRight(down); break;
            case KeyEvent.KEYCODE_W:
                game.keyJump(down); break;
        }
        return true;
    }
}
