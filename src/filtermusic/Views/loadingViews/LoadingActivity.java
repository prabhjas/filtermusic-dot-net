package filtermusic.Views.loadingViews;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import filtermusic.Controllers.PlayerController;

/*
 * Author Rowan Schischka
 * RS mobile design
 * loading screen
 */
public abstract class LoadingActivity extends Activity {

	protected abstract void run();

	protected abstract boolean endCondition();

	public final int NETWORK_ERROR_DIALOG = 0;
	public final int LOADING_DIALOG = 1;
	protected int CURRENT_DIALOG = 1;
	final int NETWORK_REQUEST_CODE = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show loading
		setContentView(filter.music.net.R.layout.loading);
		if (!PlayerController.isLoading) {
			PlayerController.isLoading = true;
			run();
		} else if (endCondition()) {
			nextView();
		}
	}

	protected void networkError() {
		// PlayerController.isLoading = false;
		showDialog(NETWORK_ERROR_DIALOG);
	}

	public void cancel(View v) {
		PlayerController.isLoading = false;
		PlayerController.stop();
		//this.stopService(new Intent(this, PlayerService.class));
		this.finish();
	}

	protected void nextView() {
		PlayerController.isLoading = false;
		this.finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		PlayerController.isLoading = false;
	}

	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		CURRENT_DIALOG = id;
		switch (id) {
		case NETWORK_ERROR_DIALOG:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					"Failed to connect\nDo you want to check your network settings?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									Intent i = new Intent(
											android.provider.Settings.ACTION_WIRELESS_SETTINGS);
									startActivityForResult(i,
											NETWORK_REQUEST_CODE);
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									LoadingActivity.this.finish();
								}
							});
			;
			dialog = builder.create();
			break;
		}
		return dialog;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == NETWORK_REQUEST_CODE) {
			this.run();
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
}
