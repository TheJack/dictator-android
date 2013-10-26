package thejack.dictator.communication;

import java.util.ArrayList;
import java.util.List;

public class RequestParser {
	public static CommandStruct parse(String message) {
		String[] splittedMessage = message.split(" ");
		String commandType = splittedMessage[0];

		if (commandType.equals("start")) {
			return parseStartCommand(splittedMessage);
		} else if (commandType.equals("round")) {
			return parseRoundCommand(splittedMessage);
		} else if (commandType.equals("update_scores")) {
			return parseUpdateScoresCommand(splittedMessage);
		} else if (commandType.equals("update_typing_state")) {
			return parseUpdateTypingStateCommand(splittedMessage);
		} else if (commandType.equals("end")) {
			return parseEndCommand(splittedMessage);
		} else {
			return null;
		}
	}

	public static CommandStruct parseStartCommand(String[] splittedMessage) {
		String commandType = splittedMessage[0];
		String numberOfPlayers = splittedMessage[1];
		String playersNames = splittedMessage[2];
		String[] playersNamesSplitted = playersNames.split(",");

		List<String> argumentsList = new ArrayList<String>();

		argumentsList.add(numberOfPlayers);
		for (String playerName : playersNamesSplitted) {
			argumentsList.add(playerName);
		}

		return new CommandStruct(commandType, argumentsList);
	}

	public static CommandStruct parseRoundCommand(String[] splittedMessage) {
		String commandType = splittedMessage[0];
		String roundNumber = splittedMessage[1];

		StringBuilder word = new StringBuilder();
		for (int i = 2; i < splittedMessage.length; i++) {
			word.append(splittedMessage[i]);
		}

		List<String> argumentsList = new ArrayList<String>();
		argumentsList.add(roundNumber);
		argumentsList.add(word.toString());

		return new CommandStruct(commandType, argumentsList);
	}

	public static CommandStruct parseUpdateScoresCommand(String[] splittedMessage) {
		String commandType = splittedMessage[0];
		String scores = splittedMessage[1];
		String[] splittedScores = scores.split(",");

		List<String> argumentsList = new ArrayList<String>();

		for (String playerScores : splittedScores) {
			argumentsList.add(playerScores);
		}

		return new CommandStruct(commandType, argumentsList);
	}

	public static CommandStruct parseUpdateTypingStateCommand(String[] splittedMessage) {
		String commandType = splittedMessage[0];
		String typingStates = splittedMessage[1];
		String[] splittedTypingStates = typingStates.split(",");

		List<String> argumentsList = new ArrayList<String>();

		for (String typingState : splittedTypingStates) {
			argumentsList.add(typingState);
		}

		return new CommandStruct(commandType, argumentsList);
	}

	public static CommandStruct parseEndCommand(String[] splittedMessage) {
		String commandType = splittedMessage[0];
		String scores = splittedMessage[1];
		String[] splittedScores = scores.split(",");

		List<String> argumentsList = new ArrayList<String>();

		for (String playerScores : splittedScores) {
			argumentsList.add(playerScores);
		}

		return new CommandStruct(commandType, argumentsList);
	}
}
