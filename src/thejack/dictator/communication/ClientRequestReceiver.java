package thejack.dictator.communication;

import java.util.List;

import thejack.dictator.communication.TCPClient.OnMessageReceived;
import android.util.Log;

public class ClientRequestReceiver implements OnMessageReceived {
	@Override
	public void messageReceived(String message) {
		CommandStruct request = RequestParser.parse(message);

		log("Received request!");
		String commandType = request.getCommandType();
		log("Command type: " + commandType);
		if (commandType.equals("start")) {
			List<String> commandArguments = request.getCommandArguments();
			for (int i = 1; i < commandArguments.size(); i++) {
				log("Player named: " + commandArguments.get(i));
			}
		}
	}

	private void log(String s) {
		Log.w(this.getClass().toString(), s);
	}
}
