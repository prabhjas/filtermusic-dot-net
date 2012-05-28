package filtermusic.Views;

import java.util.Random;

import mediastreamer.PlayerService;
import mediastreamer.PlayerService.LocalBinder;
import filtermusic.net.R;
import filtermusic.Controllers.PlayerController;
import filtermusic.Views.loadingViews.PlayerLoadingActivity;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;

/*
 * Author Rowan Schischka
 * RS mobile design
 * base activity class for this application. Provides a standard behaviour for all activities
 */
public class MPBaseActivity extends Activity {
	ServiceConnection playerConnection = null;
	PowerManager.WakeLock wl = null;
	protected String LOG_TAG = this.getClass().toString();
	private static ComponentName playerComponent = null;
	private static PlayerService playerService = null;

	public ServiceConnection bindPlayerService(Activity activity) {
		if (playerComponent == null) {
			playerComponent = activity.startService(new Intent(activity,
					PlayerService.class));
		}
		ServiceConnection playerConnection = getPlayerConnection();
		activity.bindService(new Intent(activity, PlayerService.class),
				playerConnection, Context.BIND_AUTO_CREATE);
		return playerConnection;
	}

	public void play() {
		if (PlayerController.getCurrentPlayURL() == null)
			return;
		if (playerConnection == null)
			playerConnection = bindPlayerService(this);
		playerService.play(PlayerController.getCurrentPlayURL());
		startActivity(new Intent(this, PlayerLoadingActivity.class));
		this.finish();
	}

	public void stop() {
		playerService.stop();
	}

	public boolean isPlaying() {
		if (playerService == null)
			return false;
		return playerService.isPlaying();
	}

	public void stopService() {
		try {
			playerService.stop();
		} catch (Exception e) {
			Log.i("BASE", e.getMessage());
		}
		playerComponent = null;
		Intent intent = new Intent(getBaseContext(), ExitActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		this.finish();
	}

	public void pause() {
		playerService.pause();
	}

	public ServiceConnection getPlayerConnection() {
		ServiceConnection playerConnection = new ServiceConnection() {
			public void onServiceConnected(ComponentName name, IBinder service) {
				LocalBinder binder = (LocalBinder) service;
				playerService = binder.getService();
				PlayerController.setPlayer(playerService);
			}

			public void onServiceDisconnected(ComponentName name) {
				MPBaseActivity.this.playPause(MPBaseActivity.this
						.getCurrentFocus());

			}
		};
		return playerConnection;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (wl == null) {
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "filtermusic");
			wl.acquire();
		}
		if (playerConnection == null)
			playerConnection = bindPlayerService(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (playerConnection != null)
			try {
				unbindService(playerConnection);
				if (wl != null)
					wl.release();
			} catch (IllegalArgumentException e) {
				Log.i("onDestroy", "unbind illegal argument");
			}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		return true;
	}

	public void cancel(View view) {
		this.finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Class<? extends MPBaseActivity> thisClass = this.getClass();
		switch (item.getItemId()) {
		case R.id.Home:
			if (thisClass != GenreListActivity.class)
				startActivity(new Intent(this, GenreListActivity.class));
			break;
		case R.id.Player:
			if (thisClass != PlayerActivity.class)
				startActivity(new Intent(this, PlayerActivity.class));
			break;
		case R.id.Pause:
			playPause(this.getCurrentFocus());
			break;
		case R.id.About:
			if (thisClass != AboutActivity.class)
				startActivity(new Intent(this, AboutActivity.class));
			break;
		case R.id.exit:
			stopService();
			this.finish();
		}
		return false;
	}

	public void stop(View v) {
		stop();
		this.finish();
	}

	public void playPause(View v) {
		if (isPlaying()) {
			pause();
		} else {
			play();
		}
	}

	public void next(View v) {
		stop();
		PlayerController.next(this);
		this.finish();
	}

	public void prev(View v) {
		stop();
		PlayerController.prev(this);
		this.finish();
	}

	protected void goToWeb(String url) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_BROWSABLE);
		intent.setData(Uri.parse(url));
		startActivity(intent);
	}

	public void filtermusiclink(View v) {
		goToWeb("http://www.filtermusic.net");
	}

	public void websynergylink(View v) {
		goToWeb("http://www.websynergy.gr");
	}

	public void rslink(View v) {
		goToWeb("http://www.rsmobiledesign.com");
	}

	public void lisense(View v) {
		startActivity(new Intent(this, LisenceActivity.class));
	}

	public void sourcecode(View v) {
		goToWeb("http://www.rsmobiledesign.com/portfolio.html");
	}

	public void reportBug(View v) {
		goToWeb("http://www.rsmobiledesign.com/contact.php");
	}

	private int[] backgroundColours = new int[] { Color.rgb(200, 0, 0),
			Color.rgb(100, 200, 0), Color.rgb(0, 100, 200),
			Color.rgb(190, 0, 255), 	Color.rgb(170, 255, 0) };
	Random random = new Random();
	private GradientDrawable.Orientation[] orientations = new GradientDrawable.Orientation[] {
			GradientDrawable.Orientation.BL_TR,
			GradientDrawable.Orientation.BOTTOM_TOP,
			GradientDrawable.Orientation.BR_TL,
			GradientDrawable.Orientation.LEFT_RIGHT,
			GradientDrawable.Orientation.RIGHT_LEFT,
			GradientDrawable.Orientation.TL_BR,
			GradientDrawable.Orientation.TOP_BOTTOM,
			GradientDrawable.Orientation.TR_BL };

	protected void setBackground(View changingView) {
		int colour1 = random.nextInt(backgroundColours.length);
		int colour2 = random.nextInt(backgroundColours.length);
		if (colour1 == colour2) {
			colour2 = (colour1 + 1) % backgroundColours.length;
		}

		GradientDrawable gradientDrawable = new GradientDrawable(
				orientations[random.nextInt(orientations.length)],
				new int[] { backgroundColours[colour1],
						backgroundColours[colour2] });
		changingView.setBackgroundDrawable(gradientDrawable);
	}
}
