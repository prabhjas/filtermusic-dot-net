package filtermusic.Views;

import filtermusic.net.R;
import filtermusic.Controllers.PlayerController;
import filtermusic.Model.MenuData;
import filtermusic.Model.RadioItem;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/*
 * Author Rowan Schischka
 * RS mobile design
 * lists radio stations and on click launches the player
 */
public class RadioListActivity extends MPBaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int currentRadioGroup = MenuData.getCurrentGenre();
		ListView listView = new ListView(this);
		listView.setId(MenuData.getNextId());
		RadioItem ri = MenuData.getRadioItemList().get(currentRadioGroup);
		String[] titles = ri.getTitles();
		this.setTitle(ri.getGenre());
		listView.setAdapter(new ArrayAdapter<String>(this, R.layout.main,
				titles));
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				PlayerController.loadNewStation(position,
						RadioListActivity.this);
			}
		});
		this.setBackground(listView);
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setDivider(this.getResources().getDrawable(R.drawable.hr));
		setContentView(listView);
	}
}
