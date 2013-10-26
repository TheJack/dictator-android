package thejack.dictator;

import java.util.List;

import thejack.dictator.communication.IDictatorListener;
import thejack.dictator.communication.TCPClient;
import android.app.Activity;

public class BaseActivity extends Activity implements IDictatorListener {
	@Override
	public void onResume() {
		super.onResume();
		TCPClient.getInstance().addSubscriber(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		TCPClient.getInstance().removeSubscriber(this);
	}

	@Override
	public void onGameStarted() {

	}

	@Override
	public void onRound(int n, String word) {
	}

	@Override
	public void onUpdateScores(List<Integer> scores) {
	}

	@Override
	public void onUpdateTypingState(List<Boolean> typingStates) {
	}

	@Override
	public void onGameEnd(List<Integer> scores) {
	}

}
