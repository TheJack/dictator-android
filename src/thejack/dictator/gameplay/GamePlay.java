package thejack.dictator.gameplay;

import java.util.ArrayList;
import java.util.List;

import thejack.dictator.communication.IDictatorListener;

public class GamePlay {
	private Player me;

	private int currentRound;

	private String currentWord;

	private boolean isRunning;

	private static GamePlay instance;

	private List<Player> opponents;

	List<IDictatorListener> subscribers;

	public void addSubscriber(IDictatorListener dictatorListener) {
		subscribers.add(dictatorListener);
	}

	public void removeSubscriber(IDictatorListener dictatorListener) {
		subscribers.remove(dictatorListener);
	}

	public static GamePlay getInstance() {
		if (instance == null) {
			instance = new GamePlay();
		}
		return instance;
	}

	private GamePlay() {
		currentRound = -1;
		isRunning = false;
		opponents = new ArrayList<Player>();
		me = new Player("pesho");
		subscribers = new ArrayList<IDictatorListener>();
	}

	public void setMyName(String name) {
		me.setName(name);
	}

	public void startGame(List<String> players) {
		opponents.clear();
		for (String playerName : players) {
			Player opponent = new Player(playerName);
			opponents.add(opponent);
		}

		isRunning = true;
		currentRound = 1;

		for (IDictatorListener listener : subscribers) {
			listener.onGameStarted();
		}
		updateScoreBoard();
	}

	public void updateScores(List<Integer> scores) {
		for (int i = 0; i < scores.size(); i++) {
			Player opponent = opponents.get(i);
			opponent.setScore(scores.get(i));
		}
		updateScoreBoard();
	}

	public void updateTyping(List<Boolean> playersTyping) {
		for (int i = 0; i < playersTyping.size(); i++) {
			Player opponent = opponents.get(i);
			opponent.setTyping(playersTyping.get(i));
		}
		updateScoreBoard();
	}

	public void endGame(List<Integer> scores) {
		updateScores(scores);
		isRunning = false;
	}

	private void updateScoreBoard() {
		for (IDictatorListener listener : subscribers) {
			listener.onUpdateScoreBoard(opponents);
		}
	}

	public void playWord(int round, String word, int timeout) {
		currentRound = round;
		currentWord = word;

		for (IDictatorListener listener : subscribers) {
			listener.onRound(round, word, timeout);
		}
	}

	public boolean isRunning() {
		return isRunning;
	}

	public int getCurrentRound() {
		return currentRound;
	}

	public void updateMyScore(int myScore) {
		me.setScore(myScore);
	}
}
