package thejack.dictator;

import thejack.dictator.communication.GameNetworkRequests;
import thejack.dictator.communication.TCPClient;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends BaseActivity {
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mUsername;

	// UI references.
	private EditText mUsernameView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		// Set up the login form.
		mUsernameView = (EditText) findViewById(R.id.username);
		mUsernameView.setText(mUsername);

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	@SuppressLint("NewApi")
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mUsernameView.setError(null);

		// Store values at the time of the login attempt.
		mUsername = mUsernameView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mUsername)) {
			mUsernameView.setError("Invalid username.");
			focusView = mUsernameView;
			cancel = true;
		} else if (mUsername.length() < 4) {
			mUsernameView.setError("Invalid username.");
			focusView = mUsernameView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);

			mAuthTask = new UserLoginTask();
			mAuthTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	private void showProgress(final boolean show) {
		// The ViewPropertyAnimator APIs are not available, so simply show
		// and hide the relevant UI components.
		mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
		mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			GameNetworkRequests gnr = new GameNetworkRequests(TCPClient.getInstance());
			gnr.sendSetName(mUsername);
			gnr.sendPlay();

			return false;
		}
	}

	public void onGameStarted() {
		mLoginFormView.post(new Runnable() {
			@Override
			public void run() {
				mAuthTask = null;
				showProgress(false);

				Intent intent = new Intent(LoginActivity.this, GameActivity.class);
				startActivity(intent);
			}
		});
	}
}
