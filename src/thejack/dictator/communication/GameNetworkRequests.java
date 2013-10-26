package thejack.dictator.communication;

import thejack.dictator.gameplay.Player;

public class GameNetworkRequests {
	private TCPClient tcpClient;

	public GameNetworkRequests(TCPClient client) {
		tcpClient = client;
	}

	private void sendRequest(String request) {
		tcpClient.sendMessage(request + "\n");
	}

	public void sendSetName(Player player) {
		sendSetName(player);
	}

	public void sendSetName(String playerName) {
		sendRequest("set_name," + playerName);
	}

	public void sendPlay() {
		sendRequest("play");
	}

	public void sendAnswer(int round, String word) {
		sendRequest("answer," + round + "," + word);
	}

	public void sendTyping(boolean isTyping) {
		sendRequest("update_typing_state," + isTyping);
	}
}
