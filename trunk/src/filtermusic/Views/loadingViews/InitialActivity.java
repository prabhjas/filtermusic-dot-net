package filtermusic.Views.loadingViews;

import filtermusic.Controllers.PlayerController;
import filtermusic.Model.MenuData;
import filtermusic.Views.GenreListActivity;
import android.content.Intent;
import android.os.AsyncTask;


/*
 * Author Rowan Schischka
 * RS mobile design
 * initial loading screen that is displayed on application start
 * loads up xml file from filtermusic and generates menus from it
 */
public class InitialActivity extends LoadingActivity {
	getFile fileGetter;

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (fileGetter != null)
			fileGetter.cancel(true);
	}
	Boolean fileLoaded = false;
	private class getFile extends AsyncTask<String, Void, Boolean> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Boolean doInBackground(String... params) {
			return MenuData.init(params[0]);
		}

		@Override
		protected void onPostExecute(Boolean initSuccessful) {
			fileLoaded = initSuccessful;
			if (initSuccessful){
				PlayerController.recallLast();
				nextView();
			}else {
				showDialog(NETWORK_ERROR_DIALOG);
			}
		}
	}

	@Override
	protected void nextView() {
		startActivity(new Intent(this, GenreListActivity.class));
		super.nextView();
	}

	@Override
	protected void run() {
		fileGetter = new getFile();
		// start retrieving menu data in background
		fileGetter.execute(new String[] { getCacheDir().getAbsolutePath() });
	}

	@Override
	protected boolean endCondition() {
		return fileLoaded;
	}
}