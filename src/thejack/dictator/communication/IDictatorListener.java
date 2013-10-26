package thejack.dictator.communication;

import java.util.List;

public interface IDictatorListener {
	public void onGameStarted();

	public void onRound(int n, String word);

	public void onUpdateScores(List<Integer> scores);

	public void onUpdateTypingState(List<Boolean> typingStates);

	public void onGameEnd(List<Integer> scores);
}
