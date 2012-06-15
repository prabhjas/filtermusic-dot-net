package filtermusic.Views;

import filter.music.net.R;
import android.os.Bundle;
import android.view.View;

/*
 * Author Rowan Schischka
 * RS mobile design
 * aboutpage uses aboutpage.xml
 */
public class AboutActivity extends MPBaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutpage);
		View iv = findViewById(R.id.mainlayout);
		if (iv != null) {
			this.setBackground(iv);
		}
	}
}
