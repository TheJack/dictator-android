package thejack.dictator.gameplay;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import thejack.dictator.communication.GameNetworkRequests;
import thejack.dictator.communication.IDictatorListener;

public class GamePlay {
	private Player me;

	private boolean isRunning;

	private static GamePlay instance;

	private List<Player> opponents;

	private Queue<Round> rounds;

	private Round currentRound;

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
		isRunning = false;
		opponents = new ArrayList<Player>();
		me = new Player("pesho");
		subscribers = new ArrayList<IDictatorListener>();
		rounds = new LinkedList<Round>();
	}

	public void setMyName(String name) {
		me.setName(name);
	}

	public void startGame(List<String> players) {
		opponents.clear();
		rounds.clear();
		currentRound = null;

		for (String playerName : players) {
			Player opponent = new Player(playerName);
			opponents.add(opponent);
		}

		isRunning = true;

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
		for (IDictatorListener listener : subscribers) {
			listener.onGameEnd();
		}
		isRunning = false;
	}

	private void updateScoreBoard() {
		for (IDictatorListener listener : subscribers) {
			listener.onUpdateScoreBoard(opponents);
		}
	}

	public synchronized void pushRound(int round, String word, int timeout) {
		rounds.add(new Round(round, word, timeout));
	}

	public synchronized Round getNextRound() {
		if (rounds.size() > 0) {
			Round nextRound = rounds.remove();
			currentRound = nextRound;
			return nextRound;
		} else {
			return null;
		}
	}

	public Round getCurrentRound() {
		return currentRound;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void updateMyScore(int myScore) {
		me.setScore(myScore);
	}

	public List<Player> getOpponents() {
		return opponents;
	}

	public void sendAnswer(String answer) {
		currentRound.answered = true;
		GameNetworkRequests.sendAnswer(currentRound.getRoundNum(), answer);
	}

	public void sendUpdateTypingState(boolean isTyping) {
		GameNetworkRequests.sendTypingState(isTyping);
	}

	public void sendSetName() {
		GameNetworkRequests.sendSetName(me.getName());
	}

	public void sendPlay() {
		GameNetworkRequests.sendPlay();
	}
}
