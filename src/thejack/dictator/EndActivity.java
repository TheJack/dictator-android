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

		List<Player> players = GamePlay.getInstance().getOpponents();

		Comparator<Player> comparator = new Comparator<Player>() {
			public int compare(Player p1, Player p2) {
				return p1.getScore() - p2.getScore(); // use your logic
			}
		};

		Collections.sort(players, comparator);

		StringBuilder scoreBoardString = new StringBuilder();
		for (int i = 0; i < players.size(); i++) {
			scoreBoardString.append(String.format("#%-4s  ", (i + 1) + "."));
			scoreBoardString.append(String.format("%-15s", players.get(i).getName()));
			scoreBoardString.append("    " + players.get(i).getScore() + " points");
			scoreBoardString.append("\n");
		}

		scoreBoardView.setText(scoreBoardString.toString());
	}
}
