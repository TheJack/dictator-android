package thejack.dictator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import thejack.dictator.gameplay.GamePlay;
import thejack.dictator.gameplay.Player;
import thejack.dictator.gameplay.SoundDictator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class GameActivity extends BaseActivity implements TextToSpeech.OnInitListener {
	private TextToSpeech speech;

	private EditText textField;

	private SoundDictator dictator;

	private TextView myScoreTextView;

	private TextView scoresTextView;

	private TextView countDownTimerTextView;

	private GamePlay gamePlay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		// Remove title bar
		textField = (EditText) findViewById(R.id.wordField);
		textField.requestFocus();

		scoresTextView = (TextView) findViewById(R.id.scoresTextView);

		myScoreTextView = (TextView) findViewById(R.id.myScoreTextView);

		countDownTimerTextView = (TextView) findViewById(R.id.countDownTimerTextView);

		speech = new TextToSpeech(this, this);

		gamePlay = GamePlay.getInstance();

		// InputMethodManager imm = (InputMethodManager)
		// getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.showSoftInput(textField, InputMethodManager.SHOW_FORCED);

		textField.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				boolean handled = false;
				if (actionId == EditorInfo.IME_ACTION_SEND) {
					String word = v.getText().toString();
					gamePlay.sendAnswer(word);
					v.setText("");
				}
				return handled;
			}
		});

		textField.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// / Implement typing state update here.
				return false;
			}
		});

		List<Player> opponents = GamePlay.getInstance().getOpponents();
		updateScoreBoard(opponents);
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

	@Override
	public void onRound(final int n, final String word, final int timeout) {
		myScoreTextView.post(new Runnable() {
			@Override
			public void run() {
				Log.e("onRound", Integer.toString(timeout));
				new CountDownTimer(timeout * 1000, 1000) {
					public void onTick(long millisUntilFinished) {
						Date date = new Date(millisUntilFinished);
						DateFormat formatter = new SimpleDateFormat("mm:ss");
						String dateFormatted = formatter.format(date);
						if (millisUntilFinished < 5000) {
							countDownTimerTextView.setTextColor(Color.RED);
						} else {
							countDownTimerTextView.setTextColor(Color.BLACK);
						}

						countDownTimerTextView.setText(dateFormatted);
					}

					public void onFinish() {
						countDownTimerTextView.setText("00:00");
					}
				}.start();
				speech.speak(word, TextToSpeech.QUEUE_ADD, null);
			}
		});
	}

	@Override
	public void onUpdateScoreBoard(final List<Player> opponents) {
		scoresTextView.post(new Runnable() {
			@Override
			public void run() {
				updateScoreBoard(opponents);
			}
		});
	}

	private void updateScoreBoard(final List<Player> opponents) {
		StringBuilder scoresText = new StringBuilder();
		for (Player opponent : opponents) {
			scoresText.append(opponent.getName());
			scoresText.append(": ");
			scoresText.append(opponent.getScore());
			scoresText.append(" points");
			if (opponent.isTyping()) {
				scoresText.append(" (typing)");
			}
		}
		scoresTextView.setText(scoresText.toString());
	}
}
