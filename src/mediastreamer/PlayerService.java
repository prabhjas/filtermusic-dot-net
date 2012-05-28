package mediastreamer;

import java.io.IOException;
import filtermusic.Controllers.PlayerController;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/*
 * Author Rowan Schischka
 * RS mobile design www.rsmobiledesign.com
 * PlayerService: contains a media player for playing streaming music in background service
 */
public class PlayerService extends Service implements
		MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {
	private static MediaPlayer player = null;// media player
	private final IBinder mBinder = new LocalBinder();// for binding player to
	AudioManager audioManager; // views
	//private String LOG_TAG = this.getClass().toString();
	public static boolean preparing = false;// used to prevent calls to player
											// while its busy

	// for views to access media player
	public class LocalBinder extends Binder {
		public PlayerService getService() {
			return PlayerService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		if (player == null)
			player = new MediaPlayer();
		player.setOnPreparedListener(this);
		audioManager = (AudioManager) this
				.getSystemService(Context.AUDIO_SERVICE);
		return mBinder;
	}

	// plays streaming music from given url
	public void play(String url) {
		// Log.i("PLAYERSERVICE", url);
		if (preparing == false) {
			Log.i("PLAYERSERVICE", "preparing == false");
			try {
				preparing = true;
				stop();
				player = new MediaPlayer();
				player.setOnPreparedListener(this);
				player.setAudioStreamType(AudioManager.STREAM_MUSIC);
				player.setDataSource(PlayerController.getCurrentPlayURL());
				player.prepareAsync();
				preparing = false;
			} catch (IllegalArgumentException e) {
				preparing = false;
				e.printStackTrace();
			} catch (IllegalStateException e) {
				preparing = false;
				e.printStackTrace();
			} catch (IOException e) {
				preparing = false;
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onDestroy() {
		try {
			if (player != null) {
				if (player.isPlaying()) {
					player.stop();
				}
				player.release();
			}
		} catch (IllegalStateException e) {
			// Log.i(LOG_TAG, e.getMessage());
		}
		super.onDestroy();
	}

	public void onPrepared(MediaPlayer mp) {
		mp.start();
		preparing = false;
	}

	public boolean onError(MediaPlayer mp, int what, int extra) {
		preparing = false;
		PlayerController.isLoading = false;
		Log.i("PLAYER", "ERROR");
		return false;
	}

	public boolean isPlaying() {
		if (player == null)
			return false;
		try {
			return player.isPlaying();
		} catch (IllegalStateException e) {
			return false;
		}
	}

	public void stop() {
		if (player != null) {
			try {
				if (player.isPlaying()) {
					player.stop();
				}
				player.reset();
				player = null;
			} catch (IllegalStateException e) {
				Log.i("PLAYERSERVICE", "cant stop");
			}
		}
	}

	public void pause() {
		player.pause();
	}
}