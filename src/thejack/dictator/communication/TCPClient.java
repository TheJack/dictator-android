package thejack.dictator.communication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import thejack.dictator.gameplay.GamePlay;
import android.util.Log;

public class TCPClient {
	private String serverMessage;
	private String serverIp = "10.0.249.107";
	private int serverPort = 3001;

	private boolean mRun = false;

	List<IDictatorListener> subscribers;

	PrintWriter out;
	BufferedReader in;

	private static TCPClient instance = new TCPClient("10.255.1.18", 3001);

	public static TCPClient getInstance() {
		return instance;
	}

	/**
	 * Constructor of the class. OnMessagedReceived listens for the messages
	 * received from server
	 */
	private TCPClient(String serverIp, int serverPort) {
		this.serverIp = serverIp;
		this.serverPort = serverPort;

		subscribers = new ArrayList<IDictatorListener>();
	}

	/**
	 * Sends the message entered by client to the server
	 * 
	 * @param message
	 *            text entered by client
	 */
	public void sendMessage(String message) {
		if (out != null && !out.checkError()) {
			out.println(message);
			out.flush();
		} else {
			Log.e("TCPClient", "Not sending...");
		}
	}

	public void stopClient() {
		mRun = false;
	}

	public void run() {

		mRun = true;

		try {
			// here you must put your computer's IP address.
			InetAddress serverAddr = InetAddress.getByName(serverIp);

			Log.e("TCP Client", "C: Connecting...");

			// create a socket to make the connection with the server
			Socket socket = new Socket(serverAddr, serverPort);

			try {

				// send the message to the server
				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
						socket.getOutputStream())), true);

				Log.e("TCP Client", "C: Sent.");

				Log.e("TCP Client", "C: Done.");

				// receive the message which the server sends back
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				// in this while the client listens for the messages sent by the
				// server
				while (mRun) {
					serverMessage = in.readLine();

					if (serverMessage != null) {
						// call the method messageReceived from MyActivity class
						messageReceived(serverMessage);
					}
					serverMessage = null;

				}

				Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");

			} catch (Exception e) {

				Log.e("TCP", "S: Error", e);

			} finally {
				// the socket must be closed. It is not possible to reconnect to
				// this socket
				// after it is closed, which means a new socket instance has to
				// be created.
				socket.close();
			}

		} catch (Exception e) {

			Log.e("TCP", "C: Error", e);

		}

	}

	public void addSubscriber(IDictatorListener dictatorListener) {
		subscribers.add(dictatorListener);
	}

	public void removeSubscriber(IDictatorListener dictatorListener) {
		subscribers.remove(dictatorListener);
	}

	public void messageReceived(String message) {
		CommandStruct request = RequestParser.parse(message);
		String commandType = request.getCommandType();
		List<String> args = request.getCommandArguments();

		if (commandType.equals("start")) {
			List<String> players = args.subList(1, args.size());
			GamePlay.getInstance().startGame(players);

			for (IDictatorListener listener : subscribers) {
				listener.onGameStarted();
			}
		} else if (commandType.equals("round")) {
			int n = Integer.parseInt(args.get(0));
			String word = args.get(1);
			int timeout = Integer.parseInt(args.get(2));
			GamePlay.getInstance().playWord(n, word, timeout);

			for (IDictatorListener listener : subscribers) {
				listener.onRound(n, word, timeout);
			}
		} else if (commandType.equals("update_scores")) {
			List<Integer> scores = new ArrayList<Integer>();

			for (int i = 0; i < args.size(); i++) {
				scores.add(Integer.parseInt(args.get(i)));
			}
			GamePlay.getInstance().updateScores(scores);
		} else if (commandType.equals("update_typing_state")) {
			List<Boolean> typingStates = new ArrayList<Boolean>();

			for (int i = 0; i < args.size(); i++) {
				typingStates.add(Boolean.parseBoolean(args.get(i)));
			}
			GamePlay.getInstance().updateTyping(typingStates);

			for (IDictatorListener listener : subscribers) {
				listener.onUpdateTypingState(typingStates);
			}
		} else if (commandType.equals("end")) {
			List<Integer> scores = new ArrayList<Integer>();

			for (int i = 0; i < args.size(); i++) {
				scores.add(Integer.parseInt(args.get(i)));
			}
			GamePlay.getInstance().endGame(scores);

			for (IDictatorListener listener : subscribers) {
				listener.onGameEnd(scores);
			}
		}
	}
}