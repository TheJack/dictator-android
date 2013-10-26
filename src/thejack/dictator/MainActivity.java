package thejack.dictator;

import java.util.Locale;

import thejack.dictator.communication.TCPClient;
import thejack.dictator.gameplay.GamePlay;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;

@SuppressLint("NewApi")
public class MainActivity extends BaseActivity implements TextToSpeech.OnInitListener {
	private TextToSpeech speech;
	private TCPClient mTcpClient;

	private final static String SERVER_IP = "10.255.1.18";
	private final static int SERVER_PORT = 3001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// connect to the server
		new ConnectTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
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

	public class ConnectTask extends AsyncTask<String, String, TCPClient> {
		@Override
		protected TCPClient doInBackground(String... message) {
			GamePlay gamePlay = GamePlay.getInstance();
			mTcpClient = TCPClient.getInstance();
			mTcpClient.run();

			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
		}
	}
}
