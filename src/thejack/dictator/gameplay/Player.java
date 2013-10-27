package thejack.dictator.gameplay;

public class Player {
	private String name;

	private int score;

	private boolean isTyping;

	public Player(String name) {
		this.name = name;
		score = 0;
		isTyping = false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isTyping() {
		return isTyping;
	}

	public void setTyping(boolean isTyping) {
		this.isTyping = isTyping;
	}
}
