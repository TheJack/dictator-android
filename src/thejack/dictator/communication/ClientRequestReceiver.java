package thejack.dictator.communication;

import thejack.dictator.gameplay.GamePlay;
import android.util.Log;

public class ClientRequestReceiver {
	private GamePlay currentGamePlay;

	public ClientRequestReceiver(GamePlay gamePlay) {
		currentGamePlay = gamePlay;
	}

	private void log(String s) {
		Log.w(this.getClass().toString(), s);
	}
}
