package thejack.dictator.gameplay;

import java.util.List;

public class GamePlay {
	private Player me;

	private int currentRound;

	private boolean isRunning;

	private static GamePlay instance;

	private List<Player> opponents;

	private SoundDictator dictator;

	public static GamePlay getInstance() {
		if (instance == null) {
			instance = new GamePlay();
		}
		return instance;
	}

	private GamePlay() {
		currentRound = -1;
		isRunning = false;
		me = new Player("pesho");
	}

	public void setMyName(String name) {
		me.setName(name);
	}

	public void newGame(List<String> players) {
		if (!isRunning) {
			opponents.clear();
			for (String playerName : players) {
				Player opponent = new Player(playerName);
				opponents.add(opponent);
			}

			isRunning = true;
			currentRound = -1;
		}
	}

	public void updateScores(List<Integer> scores) {
		for (int i = 0; i < scores.size(); i++) {
			Player opponent = opponents.get(i);
			opponent.setScore(scores.get(i));
		}
	}

	public void updateTyping(List<Boolean> playersTyping) {
		for (int i = 0; i < playersTyping.size(); i++) {
			Player opponent = opponents.get(i);
			opponent.setTyping(playersTyping.get(i));
		}
	}

	public void endGame(List<Integer> scores) {
		updateScores(scores);
		isRunning = false;
	}


	public void playWord(int round, String word) {
		// game logic to play the word and so on
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

	public void setDicatator(SoundDictator dictator) {
		this.dictator = dictator;
	}
}
