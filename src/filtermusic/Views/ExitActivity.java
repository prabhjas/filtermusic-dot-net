package filtermusic.Views;

import mediastreamer.PlayerService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ExitActivity extends MPBaseActivity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			stopService(new Intent(this, PlayerService.class));
		} catch (IllegalStateException e) {
			Log.i("EXIT", e.getMessage());
		}
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		this.finish();
	}
}
