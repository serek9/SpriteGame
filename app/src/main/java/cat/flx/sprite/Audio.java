package cat.flx.sprite;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

class Audio {

    // media player for background music
    private MediaPlayer mediaPlayer;
    // sound pool for sound effects
    private SoundPool soundPool;
    private int[] fx;
    // sound effects to load
    private static final int[] fxRes = {
            R.raw.coin,
            R.raw.die,
            R.raw.pause
    };

    Audio(Activity activity) {
        // Prepping the media player
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mediaPlayer = MediaPlayer.create(activity, R.raw.music);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(0.25f, 0.25f);

        // Prepping the sound pool
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        fx = new int[fxRes.length];
        for(int i = 0; i < fxRes.length; i++) {
            fx[i] = soundPool.load(activity, fxRes[i], 1);
        }
    }

    // Start & stop music
    void startMusic() { mediaPlayer.start(); }
    void stopMusic() { mediaPlayer.pause(); }

    private void playEffect(int n) { soundPool.play(fx[n], 1, 1, 1, 0, 1); }

    // Useful methods
    void coin() { playEffect(0); }
    void die() { playEffect(1); }
    void pause() { playEffect(2); }
}
