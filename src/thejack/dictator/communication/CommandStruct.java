package thejack.dictator.communication;

import java.util.List;

public class CommandStruct {
	private String commandType;
	private List<String> commandArguments;

	public CommandStruct(String type, List<String> args) {
		commandType = type;
		commandArguments = args;
	}

	public String getCommandType() {
		return commandType;
	}

	public List<String> getCommandArguments() {
		return commandArguments;
	}
}
