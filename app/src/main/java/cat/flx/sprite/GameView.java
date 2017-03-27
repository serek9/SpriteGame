package cat.flx.sprite;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


class GameView extends View {

    Game game;

    public GameView(Context context) { this(context, null, 0); }
    public GameView(Context context, AttributeSet attrs) { this(context, attrs, 0); }
    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    void setGame(Game game) { this.game = game; }

    @Override public void onMeasure(int widthSpec, int heightSpec) {
        int w = MeasureSpec.getSize(widthSpec);
        int h = MeasureSpec.getSize(heightSpec);
        setMeasuredDimension(w, h);
    }

    private boolean locked = false;
    @Override public void onDraw(Canvas canvas) {
        this.postInvalidateDelayed(50);
        if (locked) return;
        locked = true;
        if (game == null) return;
        game.events();
        game.physics();
        game.draw(canvas);
        locked = false;
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        int act = event.getActionMasked();
        int i = event.getActionIndex();
        boolean down = (act != MotionEvent.ACTION_UP) &&
                (act != MotionEvent.ACTION_POINTER_UP) &&
                (act != MotionEvent.ACTION_CANCEL);
        int x = (int)(event.getX(i)) * 100 / getWidth();
        int y = (int)(event.getY(i)) * 100 / getHeight();
        if (y < 75) {         // VERTICAL DEAD-ZONE
            game.left(false);
            game.right(false);
        }
        else if (x < 17) {    // LEFT
            if (down) game.right(false);
            game.left(down);
        } else if (x < 33) {  // RIGHT
            if (down) game.left(false);
            game.right(down);
        } else if (x < 83) {  // DEAD-ZONE
            game.left(false);
            game.right(false);
        } else if (x > 83) {  // JUMP
            game.jump(down);
        }
        return true;
    }
}
