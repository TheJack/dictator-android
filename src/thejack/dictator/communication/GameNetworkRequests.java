package thejack.dictator.communication;

import thejack.dictator.gameplay.Player;

public class GameNetworkRequests {
	private static void sendRequest(String request) {
		TCPClient.getInstance().sendMessage(request + "\n");
	}

	public static void sendSetName(Player player) {
		sendSetName(player);
	}

	public static void sendSetName(String playerName) {
		sendRequest("set_name," + playerName);
	}

	public static void sendPlay() {
		sendRequest("play");
	}

	public static void sendAnswer(int round, String word) {
		sendRequest("answer," + round + "," + word);
	}

	public static void sendTypingState(boolean isTyping) {
		sendRequest("update_typing_state," + (isTyping ? "1" : 0));
	}
}
