package cat.flx.sprite;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

abstract class Character {
    Game game;
    int x, y;
    int vx, vy;
    int state, frame;
    int padLeft, padTop, colWidth, colHeight;
    private Paint debugPaint = new Paint();

    Character(Game game) {
        this.game = game;
        state = frame = 0;
        padLeft = padTop = colWidth = colHeight = 0;
        debugPaint.setColor(Color.YELLOW);
        debugPaint.setStyle(Paint.Style.STROKE);
    }

    abstract int[][] getStates();
    abstract void physics();

    private Rect colRect = new Rect();
    Rect getCollisionRect() {
        colRect.set(x + padLeft, y + padTop, x + padLeft + colWidth, y + padTop + colHeight);
        return colRect;
    }

    private int spriteIdx;
    void draw(Canvas canvas) {
        if (frame >= this.getStates()[state].length) frame = 0;
        spriteIdx = this.getStates()[state][frame];
        game.getBitmapSet().drawBitmap(canvas, x, y, spriteIdx);
        canvas.drawRect(getCollisionRect(), debugPaint);
        frame++;
    }
}
