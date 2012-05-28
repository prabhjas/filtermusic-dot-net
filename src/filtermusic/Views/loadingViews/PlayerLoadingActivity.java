package filtermusic.Views.loadingViews;

import filtermusic.Controllers.PlayerController;
import filtermusic.Views.PlayerActivity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
/*
 * Author Rowan Schischka
 * RS mobile design
 * loading screen
 */
public class PlayerLoadingActivity extends LoadingActivity {

	private CountDownTimer timer;
	private boolean timedOut = true;

	@Override
	protected void nextView() {
		Log.i("PlayerLoadingActivity",
				"startActivity(new Intent(this, PlayerActivity.class));");
		startActivity(new Intent(this, PlayerActivity.class));
		super.nextView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (timer != null)
			timer.cancel();
	}

	@Override
	protected void run() {
		PlayerController.play();
		timer = new CountDownTimer(30000, 1000) {
			@Override
			public void onFinish() {
				Log.i("TIMER", "done");
				if (timedOut) {
					PlayerController.resetPreparing();
					networkError();
				}
			}

			@Override
			public void onTick(long millisUntilFinished) {
				if (endCondition()) {
					timedOut = false;
					nextView();
				} else if(!PlayerController.isLoading){
					networkError();
				}
			}
		}.start();
	}

	@Override
	protected boolean endCondition() {
		return PlayerController.isPlaying();
	}
}
