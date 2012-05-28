package filtermusic.Controllers;

import mediastreamer.PlayerService;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import filtermusic.Model.MenuData;
import filtermusic.Model.RadioItem;
import filtermusic.Views.loadingViews.PlayerLoadingActivity;

/*
 * Author Rowan Schischka
 * RS mobile design
 * sends commands to playerService
 */
public class PlayerController {
	/*
	 * values to display on the player screen
	 */
	private static String currentPlayImageURL = "";

	public static String getCurrentPlayImageURL() {
		return currentPlayImageURL;
	}

	private static String currentPlayTitle = "";

	public static String getCurrentPlayTitle() {
		return currentPlayTitle;
	}

	private static String currentPlayURL = null;

	public static String getCurrentPlayURL() {
		return currentPlayURL;
	}

	private static RadioItem ri = null;

	public static RadioItem getRi() {
		return ri;
	}

	private static int position = 0;// position of current station in
									// radiostation list

	public static int getPosition() {
		return position;
	}

	/*
	 * player service controls
	 */
	public static void next(Activity activity) {
		loadNewStation((position + 1) % ri.getTitles().length, activity);
	}

	public static void prev(Activity activity) {
		loadNewStation(Math.abs((position - 1)) % ri.getTitles().length,
				activity);
	}

	/*
	 * loads & plays new station given: position in current radiolist activity
	 * called from
	 */
	public static void loadNewStation(int position, Activity activity) {
		PlayerController.position = position;
		ri = MenuData.getRadioItemList().get(MenuData.getCurrentGenre());
		currentPlayImageURL = ri.getImageURLs()[position];
		currentPlayTitle = ri.getTitles()[position];
		currentPlayURL = ri.getURLs()[position];
		activity.startActivity(new Intent(activity, PlayerLoadingActivity.class));
	}

	/*
	 * reloads last used radio station attributes for the display
	 */
	public static void recallLast() {
		ri = MenuData.getRadioItemList().get(MenuData.getCurrentGenre());
		currentPlayImageURL = ri.getImageURLs()[position];
		currentPlayTitle = ri.getTitles()[position];
		currentPlayURL = ri.getURLs()[position];
	}

	public static boolean isLoading = false;// makes async tasks in loading
											// screens singleton
	static PlayerService playerService;// the radio player

	public static void setPlayer(PlayerService playerService_) {
		playerService = playerService_;
	}

	public static void play() {
		if (playerService != null) {
			playerService.play(PlayerController.currentPlayURL);
			Log.i("PLAYER_CONTROLLER", "playing "
					+ PlayerController.currentPlayURL);
		} else {
			Log.i("PLAYER_CONTROLLER", "player = null");
		}
	}

	// check player service is running and playing
	public static boolean isPlaying() {
		if (playerService == null)
			return false;
		return playerService.isPlaying();
	}

	// used to prevent calls to playerservice when its not ready
	public static void resetPreparing() {
		if (playerService == null)
			PlayerService.preparing = false;
	}

	public static void stop() {
		try {
			if (playerService != null) {
				playerService.stop();
			}
		} catch (IllegalStateException e) {

		}
	}
}
