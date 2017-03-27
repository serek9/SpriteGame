package cat.flx.sprite;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.util.SparseArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

class BitmapSet {

    // Structure for bitmap data storage (read from file)
    private class BitmapData {
        int x, y, w, h;
        boolean flip;
        Bitmap bmp;
        String description;
    }
    // Map where we handle the bitmap data
    private SparseArray<BitmapData> bitmaps;

	Bitmap getBitmap(int i) { return bitmaps.get(i).bmp; }
    int getSpriteWidth(int i) { return bitmaps.get(i).w; }
    int getSpriteHeight(int i) { return bitmaps.get(i).h; }

    private Paint paint;
    void drawBitmap(Canvas canvas, int x, int y, int i) {
        Bitmap sprite = getBitmap(i);
        canvas.drawBitmap(sprite, x, y, paint);
    }

	BitmapSet(Resources res) {
        paint = new Paint();
        // Prepare the bitmap decoder, splitter and flipper
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;
        Bitmap bitmapsBMP =
                BitmapFactory.decodeResource(res, R.raw.bonk, opts);
        Matrix rot1 = new Matrix();
        Matrix rot2 = new Matrix(); rot2.setScale(-1, 1);

        bitmaps = new SparseArray<>();
		InputStream is = res.openRawResource(R.raw.sheetinfo);
		BufferedReader reader = new BufferedReader(
                new InputStreamReader(is));
		try {
			String line;
			while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;
                String[] parts = line.split(":");
                if (parts.length != 3) continue;
                int index = Integer.parseInt(parts[0].trim());
                String[] numbers = parts[1].trim().split(",");
                if (numbers.length != 5) continue;
                BitmapData data = new BitmapData();
                data.x = Integer.parseInt(numbers[0].trim());
                data.y = Integer.parseInt(numbers[1].trim());
                data.w = Integer.parseInt(numbers[2].trim());
                data.h = Integer.parseInt(numbers[3].trim());
                data.flip = (Integer.parseInt(numbers[4].trim()) == 1);
                data.description = parts[2].trim();
                data.bmp = Bitmap.createBitmap(bitmapsBMP,
                        data.x, data.y, data.w, data.h,
                        data.flip ? rot2 : rot1, true);
                bitmaps.put(index, data);
            }
            reader.close();
		}
		catch(Exception ioe) {
            Log.d("flx", "Malformed sheet info file: " + ioe.getMessage());
        }
        finally{
            bitmapsBMP.recycle();
        }
	}
}
