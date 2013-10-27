package thejack.dictator;

import java.util.List;

import thejack.dictator.communication.IDictatorListener;
import thejack.dictator.gameplay.GamePlay;
import thejack.dictator.gameplay.Player;
import android.app.Activity;

public class BaseActivity extends Activity implements IDictatorListener {
	@Override
	public void onResume() {
		super.onResume();
		GamePlay.getInstance().addSubscriber(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		GamePlay.getInstance().removeSubscriber(this);
	}

	@Override
	public void onGameStarted() {

	}

	@Override
	public void onGameEnd() {
	}

	@Override
	public void onUpdateScoreBoard(List<Player> opponents) {
	}

}
