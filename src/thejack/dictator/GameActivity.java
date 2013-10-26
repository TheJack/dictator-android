package thejack.dictator;

import java.util.Locale;

import thejack.dictator.gameplay.SoundDictator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("NewApi")
public class GameActivity extends BaseActivity implements TextToSpeech.OnInitListener {
	private TextToSpeech speech;

	private EditText textField;

	private SoundDictator dictator;

	private TextView myScoreTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		// Remove title bar
		textField = (EditText) findViewById(R.id.wordField);
		textField.requestFocus();

		myScoreTextView = (TextView) findViewById(R.id.myScoreTextView);

		speech = new TextToSpeech(this, this);

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(textField, InputMethodManager.SHOW_IMPLICIT);
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

	public void onRound(int n, final String text) {
		myScoreTextView.post(new Runnable() {
			@Override
			public void run() {
				speech.speak(text, TextToSpeech.QUEUE_ADD, null);
			}
		});
	}
}
