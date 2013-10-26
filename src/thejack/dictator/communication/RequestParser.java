package thejack.dictator.communication;

import java.util.ArrayList;
import java.util.List;

public class RequestParser {
	public static CommandStruct parse(String message) {
		String[] splittedMessage = message.split(",");
		String commandType = splittedMessage[0];

		List<String> argumentsList = new ArrayList<String>();
		for (int i = 1; i < splittedMessage.length; i++) {
			argumentsList.add(splittedMessage[i]);
		}

		return new CommandStruct(commandType, argumentsList);
	}
}
