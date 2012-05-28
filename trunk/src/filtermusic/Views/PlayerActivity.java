package filtermusic.Views;

import java.io.File;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import filtermusic.net.R;
import filtermusic.Controllers.FileDownloader;
import filtermusic.Controllers.PlayerController;

/*
 * Author: Rowan Schischka
 * RS mobile design www.rsmobiledesign.com
 * activity for the media player UI
 */
public class PlayerActivity extends MPBaseActivity {

	private GoogleAnalyticsTracker tracker;// lets us know what stations are
											// being listened to
	public void sendToBigBrother(String name, String genre) {
		if (tracker == null) {
			tracker = GoogleAnalyticsTracker.getInstance();
			tracker.startNewSession("UA-30787156-1", this);
			tracker.setAnonymizeIp(true);
		}
		tracker.setCustomVar(1, genre, name, 2);
		tracker.trackPageView("player");
		tracker.dispatch();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);
		View iv = findViewById(R.id.mainlayout);
		if(iv !=null){
			this.setBackground(iv);
		}
		
		// setupImage
		if (backgroundImage == null) {
			backgroundImage = FileDownloader.getFile(getCacheDir()
					.getAbsolutePath(), PlayerController
					.getCurrentPlayImageURL());
			if (backgroundImage == null || !backgroundImage.exists()) {
				fileGetter = new FileGetter();
				fileGetter.execute((Void[]) null);
			} else {
				setImage();
			}
		} else {
			setImage();
		}
		adjustButtons();
		View sn = findViewById(R.id.stationName);
		if (sn != null) {
			TextView stationName = (TextView) sn;
			stationName.setText(PlayerController.getCurrentPlayTitle());
		}
		sendToBigBrother(PlayerController.getCurrentPlayTitle(), PlayerController.getRi().getGenre());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (fileGetter != null)
			fileGetter.cancel(true);
		tracker.stopSession();
	}

	Boolean fileLoaded = false;
	FileGetter fileGetter;
	File backgroundImage;

	private class FileGetter extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {

		}

		@Override
		protected Void doInBackground(Void... params) {
			backgroundImage = FileDownloader.getHTTP(PlayerActivity.this
					.getCacheDir().getAbsolutePath(), PlayerController
					.getCurrentPlayImageURL());
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			setImage();
		}
	}

	private void setImage() {
		if (backgroundImage == null) {
			Log.i("IMAGE", "FILE NULL");
			return;
		}
		Bitmap bmp = FileDownloader.fileToBitmap(backgroundImage);
		if (bmp == null) {
			Log.i("IMAGE", "BMP NULL");
			return;
		}
		View iv = findViewById(R.id.radioImage);
		if (iv == null) {
			Log.i("IMAGE", "IV NULL");
			return;
		} else {
			ImageView image = (ImageView) iv;
			image.setImageBitmap(bmp);
			image.setAdjustViewBounds(true);
			iv = findViewById(R.id.stationName);
			if(iv != null){
				iv.setVisibility(View.INVISIBLE);
			}
		}
	}

	@Override
	public void playPause(View v) {
		super.playPause(v);
		adjustButtons();
	}

	private void adjustButtons() {
		Button b = (Button) findViewById(R.id.pause);
		if (b != null) {
			if (isPlaying()) {
				b.setBackgroundResource(R.drawable.pause1);
			} else {
				b.setBackgroundResource(R.drawable.play1);
			}
		}
	}
}
