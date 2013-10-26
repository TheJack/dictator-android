package thejack.dictator;

import java.util.Locale;

import thejack.dictator.communication.ClientRequestReceiver;
import thejack.dictator.communication.TCPClient;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {
	private TextToSpeech speech;
	private TCPClient mTcpClient;

	private final static String SERVER_IP = "10.0.249.100";
	private final static int SERVER_PORT = 3001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		speech = new TextToSpeech(this, this);

		// connect to the server
		new connectTask().execute("");

		// Button speakButton = (Button) findViewById(R.id.button1);
		// speakButton.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // speech.setPitch(0.5f);
		// // speech.speak("I'm the Dictator.", TextToSpeech.QUEUE_ADD,
		// // null);
		// // speech.playSilence(100, TextToSpeech.QUEUE_ADD, null);
		// // speech.setPitch(3.0f);
		// // speech.setSpeechRate(3.0f);
		// // speech.speak("Ok, dude!", TextToSpeech.QUEUE_ADD, null);
		//
		// // sends the message to the server
		// if (mTcpClient != null) {
		// mTcpClient.sendMessage("ebasi!");
		// Log.e("tcp", "ebasi");
		// } else {
		// Log.e("tcp", "fuck");
		// }
		// }
		// });
	}

	public void onClickCompetativeButton(View v) {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			int result = speech.setLanguage(Locale.US);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			}
		}
	}

	public class connectTask extends AsyncTask<String, String, TCPClient> {
		@Override
		protected TCPClient doInBackground(String... message) {
			mTcpClient = new TCPClient(SERVER_IP, SERVER_PORT, new ClientRequestReceiver());
			mTcpClient.run();

			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);

			// in the arrayList we add the messaged received from server
			// arrayList.add(values[0]);
			// notify the adapter that the data set has changed. This means that
			// new message received
			// from server was added to the list
			// mAdapter.notifyDataSetChanged();
		}
	}
}
