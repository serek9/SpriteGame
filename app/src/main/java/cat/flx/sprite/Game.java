package cat.flx.sprite;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class Game {
    private Context context;

    private BitmapSet bitmapSet;
    private Scene scene;
    private Bonk bonk;
    private Audio audio;
    private List<Coin> coins;
    private List<Enemy> enemies;
    private int screenOffsetX, screenOffsetY;
    private int count = 0;

    Game(Activity activity) {
        this.context = activity;
        bitmapSet = new BitmapSet(context.getResources());
        audio = new Audio(activity);
        scene = new Scene(this);
        bonk = new Bonk(this);
        coins = new ArrayList<>();
        enemies = new ArrayList<>();
        scene.loadFromFile(R.raw.scene);
        bonk.x = 16 * 10;
        bonk.y = 0;
        count = 0;
    }
    Context getContext() { return context; }
    Resources getResources() { return context.getResources(); }

    BitmapSet getBitmapSet() { return bitmapSet; }
    Scene getScene() { return scene; }
    Audio getAudio() { return audio; }
    Bonk getBonk() { return bonk; }

    void addCoin(Coin coin) {
        coins.add(coin);
    }
    List<Coin> getCoins() { return coins; }

    void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }
    List<Enemy> getEnemies() { return enemies; }

    void physics() {
        bonk.physics();
        for(Enemy enemy : enemies) {
            enemy.physics();
            if (bonk.state == 3) continue;
            if (enemy.getCollisionRect().intersect(bonk.getCollisionRect())) {
                if (bonk.vy > 0) {
                    enemy.x = -1000;
                    enemy.y = -1000;
                }
                else {
                    audio.die();
                    bonk.state = 3;
                }
            }
        }
        for(Coin coin : coins) {
            coin.physics();
            if (coin.getCollisionRect().intersect(bonk.getCollisionRect())) {
                audio.coin();
                coin.x = -1000;
                coin.y = -1000;
                count++;
            }
        }
    }

    private float sc;
    private int scX, scY;

    void draw(Canvas canvas) {
        if (canvas.getWidth() == 0) return;
        if (sc == 0) {
            scY = 16 * 16;
            sc = canvas.getHeight() / (float) scY;
            scX = (int) (canvas.getWidth() / sc);
        }
        screenOffsetX = Math.min(screenOffsetX, bonk.x - 100);
        screenOffsetX = Math.max(screenOffsetX, bonk.x - scX + 100);
        screenOffsetX = Math.max(screenOffsetX, 0);
        screenOffsetX = Math.min(screenOffsetX, scene.getWidth() - scX - 1);
        screenOffsetY = Math.min(screenOffsetY, bonk.y - 50);
        screenOffsetY = Math.max(screenOffsetY, bonk.y - scY + 75);
        screenOffsetY = Math.max(screenOffsetY, 0);
        screenOffsetY = Math.min(screenOffsetY, scene.getHeight() - scY);
        canvas.scale(sc, sc);
        canvas.translate(-screenOffsetX, -screenOffsetY);
        scene.draw(canvas);
        bonk.draw(canvas);
        LinearLayout layout = new LinearLayout(context);

        TextView textView = new TextView(context);
        textView.setVisibility(View.VISIBLE);
        textView.setText(count+"");
        layout.addView(textView);

        layout.measure(canvas.getWidth(), canvas.getHeight());
        layout.layout(0, 0, canvas.getWidth(), canvas.getHeight());

        canvas.translate(10, 5);

        layout.draw(canvas);
        for(Enemy enemy : enemies) {
            enemy.draw(canvas);
        }
        for(Coin coin : coins) {
            coin.draw(canvas);
        }
    }

    private int keyCounter = 0;
    private boolean keyLeft, keyRight, keyJump;
    void keyLeft(boolean down) { keyCounter = 0; if (down) keyLeft = true; }
    void keyRight(boolean down) { keyCounter = 0; if (down) keyRight = true; }
    void keyJump(boolean down) { keyCounter = 0; if (down) keyJump = true; }

    private boolean left, right, jump;
    void left(boolean down) {
        if (left && !down) left = false;
        else if (!left && down) left = true;
    }
    void right(boolean down) {
        if (right && !down) right = false;
        else if (!right && down) right = true;
    }
    void jump(boolean down) {
        if (jump && !down) jump = false;
        else if (!jump && down) jump = true;
    }

    void events() {
        if (++keyCounter > 2) {
            keyCounter = 0;
            keyLeft = keyRight = keyJump = false;
        }
        if (keyLeft || left) { bonk.left(); }
        if (keyRight || right) { bonk.right(); }
        if (keyJump || jump) { bonk.jump(); }
    }
}
