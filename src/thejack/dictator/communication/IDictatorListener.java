package thejack.dictator.communication;

import java.util.List;

import thejack.dictator.gameplay.Player;

public interface IDictatorListener {
	public void onGameStarted();

	public void onRound(final int n, final String word, final int timeout);

	public void onGameEnd(final List<Player> opponents);

	public void onUpdateScoreBoard(final List<Player> opponents);
}
