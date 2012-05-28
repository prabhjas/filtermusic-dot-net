package filtermusic.Views;

import filtermusic.net.R;
import filtermusic.Model.MenuData;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/*
 * Author Rowan Schischka
 * RS mobile design
 * first menu/main menu UI that lists genres
 */
public class GenreListActivity extends MPBaseActivity {
	String[] stringList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (stringList == null)
			stringList = MenuData.getGenreNamesList();
		ListView listView = new ListView(this);
		listView.setId(MenuData.getNextId());
		listView.setAdapter(new ArrayAdapter<String>(this, R.layout.main,
				stringList));
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int viewID,
					long arg3) {
				MenuData.setupGenreView(viewID);
				Intent i = new Intent(GenreListActivity.this,
						RadioListActivity.class);
				startActivity(i);
			}
		});
		//listView.setBackgroundResource(this.getBackground());
		this.setBackground(listView);
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setDivider(this.getResources().getDrawable(R.drawable.hr));
		setContentView(listView);
	}
}