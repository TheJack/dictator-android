package thejack.dictator.gameplay;

public class Round {
	int round;
	int timeout;
	String word;
	
	public long startedAt = 0;
	public long endedAt = 0;
	public boolean answered = false;
	
	public Round(int num, String word, int timeout) {
		this.round = num;
		this.word = word;
		this.timeout = timeout;
	}
	
	public int getTimeout() {
		return timeout;
	}
	
	public String getWord() {
		return word;
	}
	
	public int getRoundNum() {
		return round;
	}
}
