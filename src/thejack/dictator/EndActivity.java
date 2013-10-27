package thejack.dictator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import thejack.dictator.gameplay.GamePlay;
import thejack.dictator.gameplay.Player;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class EndActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_end);

		TextView scoreBoardView = (TextView) findViewById(R.id.scoreBoardView);
		TextView scoreBoardPointsView = (TextView) findViewById(R.id.scorePointsView);

		List<Player> players = GamePlay.getInstance().getOpponents();

		Comparator<Player> comparator = new Comparator<Player>() {
			public int compare(Player p1, Player p2) {
				return p2.getScore() - p1.getScore(); // use your logic
			}
		};

		Collections.sort(players, comparator);

		StringBuilder scoreBoardBuilder = new StringBuilder();
		for (int i = 0; i < players.size(); i++) {
			StringBuilder rowStringBuilder = new StringBuilder();
			rowStringBuilder.append("#" + (i + 1) + ".");
			while (rowStringBuilder.length() < 6) {
				rowStringBuilder.append(" ");
			}

			rowStringBuilder.append(players.get(i).getName());
			while (rowStringBuilder.length() < 20 + 6) {
				rowStringBuilder.append(" ");
			}
			rowStringBuilder.append("\n");
			scoreBoardBuilder.append(rowStringBuilder);
		}
		StringBuilder scoreBoardPointsBuilder = new StringBuilder();
		for (int i = 0; i < players.size(); i++) {
			StringBuilder rowStringBuilder = new StringBuilder();
			rowStringBuilder.append(players.get(i).getScore() + " points");
			rowStringBuilder.append("\n");

			scoreBoardPointsBuilder.append(rowStringBuilder);
		}

		scoreBoardView.setText(scoreBoardBuilder.toString());
		scoreBoardPointsView.setText(scoreBoardPointsBuilder.toString());
	}
}
