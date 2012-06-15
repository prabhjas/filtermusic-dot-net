package filtermusic.Views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import filter.music.net.R;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
/*
 * Author Rowan Schischka
 * RS mobile design
 * shows license information
 */
public class LisenceActivity extends MPBaseActivity {
	StringBuilder text = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lisence);
		View iv = findViewById(R.id.mainlayout);
		if(iv !=null){
			this.setBackground(iv);
		}
		if (text == null) {
			text = new StringBuilder();
			InputStream is = this.getResources().openRawResource(R.raw.gpl);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			try {
				while ((line = br.readLine()) != null) {
					text.append(line + '\n');
				}
				is.close();
				br.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		TextView tv = (TextView) findViewById(R.id.lisencetext);
		tv.setText(text);
	}
}
