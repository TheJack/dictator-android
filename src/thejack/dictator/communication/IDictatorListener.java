package thejack.dictator.communication;

import java.util.List;

import thejack.dictator.gameplay.Player;

public interface IDictatorListener {
	public void onGameStarted();

	public void onGameEnd();

	public void onUpdateScoreBoard(final List<Player> opponents);
}
