package thejack.dictator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import thejack.dictator.gameplay.GamePlay;
import thejack.dictator.gameplay.Player;
import thejack.dictator.gameplay.Round;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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

	private TextView myScoreTextView;

	private TextView scoresTextView;

	private TextView countDownTimerTextView;

	private GamePlay gamePlay;

	private GameCycleTask gameTask;

	private boolean speakingInited = false;

	private TypingStateTask sendNotTypingTask;

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

		speech.setSpeechRate(0.4f);
		speech.setPitch(0.1f);

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
					changeTimerColor(Color.GREEN);
					v.setText("");
				}
				return handled;
			}
		});

		textField.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				gamePlay.sendUpdateTypingState(true);
				sendNotTyping();
				return false;
			}
		});

		List<Player> opponents = GamePlay.getInstance().getOpponents();
		updateScoreBoard(opponents);

		Log.w("GameActivity", "OnCreated.");
	}

	@Override
	public void onInit(int status) {
		Log.w("GameActivity", "onInit");
		if (status == TextToSpeech.SUCCESS) {
			int result = speech.setLanguage(Locale.US);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			}
		}
		speakingInited = true;
	}

	@Override
	public void onResume() {
		super.onResume();
		gameTask = new GameCycleTask();
		gameTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
	}

	@Override
	public void onPause() {
		super.onPause();
		if (gameTask != null) {
			gameTask.cancel(true);
		}
	}

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
							countDownTimerTextView.setTextColor(Color.LTGRAY);
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

	public class GameCycleTask extends android.os.AsyncTask<String, Void, Void> {
		private Round currentRound;

		@Override
		protected Void doInBackground(String... params) {
			currentRound = GamePlay.getInstance().getCurrentRound();
			Log.w("GameCycleTask", "Started.");
			while (!isCancelled()) {
				if (speakingInited) {
					Log.w("GameCycleTask", "Current round: " + currentRound);
					if (currentRound == null) {
						currentRound = GamePlay.getInstance().getNextRound();
						if (currentRound != null) {
							currentRound.startedAt = System.currentTimeMillis();
							Log.w("GameCycleTask", "Current word: " + currentRound.getWord());
							speech.speak(currentRound.getWord(), TextToSpeech.QUEUE_ADD, null);
						}
					}
					if (currentRound != null) {
						if (currentRound.endedAt > 0) {
							if ((System.currentTimeMillis() - currentRound.endedAt) > 1000) {
								currentRound = null;
							}
						} else if (currentRound.startedAt + 1000 * currentRound.getTimeout() < System
								.currentTimeMillis()) {
							countDownTimerTextView.post(new Runnable() {
								@Override
								public void run() {
									countDownTimerTextView.setText("00:00");
								}
							});

							currentRound.endedAt = System.currentTimeMillis();
							// TimerTextBlock.Foreground = new
							// SolidColorBrush(Colors.Red);
						} else {
							final long millisUntilFinished = currentRound.startedAt + 1000
									* currentRound.getTimeout() - System.currentTimeMillis();
							countDownTimerTextView.post(new Runnable() {
								@Override
								public void run() {
									Date date = new Date(millisUntilFinished);
									DateFormat formatter = new SimpleDateFormat("mm:ss");
									String dateFormatted = formatter.format(date);

									countDownTimerTextView.setText(dateFormatted);
									if (millisUntilFinished < 5000 && !currentRound.answered) {
										asyncChangeTimeColor(Color.RED);
									} else if (!currentRound.answered) {
										asyncChangeTimeColor(Color.LTGRAY);
									}
								}
							});
						}
					}

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						//
					}
				}
			}
			return null;
		}

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
			scoresText.append("\n");
		}
		scoresTextView.setText(scoresText.toString());
	}

	private void asyncChangeTimeColor(final int color) {
		countDownTimerTextView.post(new Runnable() {
			@Override
			public void run() {
				changeTimerColor(color);
			}
		});
	}

	private void changeTimerColor(int color) {
		countDownTimerTextView.setTextColor(color);
	}

	private void sendNotTyping() {
		if (sendNotTypingTask == null || sendNotTypingTask.isCancelled()) {
			sendNotTypingTask = new TypingStateTask();
			sendNotTypingTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
		} else {
			sendNotTypingTask.cancel(true);
			sendNotTypingTask = new TypingStateTask();
			sendNotTypingTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
		}
	}

	public class TypingStateTask extends android.os.AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... arg0) {
			try {
				Thread.sleep(1500);
				if (!isCancelled()) {
					gamePlay.sendUpdateTypingState(false);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public void onGameEnd(final List<Player> opponents) {
		countDownTimerTextView.post(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(GameActivity.this, EndActivity.class);
				startActivity(intent);
			}
		});
	}
}
