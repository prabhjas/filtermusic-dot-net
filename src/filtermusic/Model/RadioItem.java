package filtermusic.Model;

import java.util.ArrayList;

/*
 * Author Rowan Schischka
 * RS mobile design
 * holds radio station information
 */
public class RadioItem {

	private ArrayList<String> titles;
	public String[] getTitles() {
		return (String[]) titles.toArray(new String[0]);
	}

	private ArrayList<String> urls;
	public String[] getURLs() {
		// TODO Auto-generated method stub
		return (String[]) urls.toArray(new String[0]);
	}
	
	private ArrayList<String> descriptions;
	public String[] getDescriptions() {
		return (String[]) descriptions.toArray(new String[0]);
	}
	
	private ArrayList<String> imageURLs;
	public String[] getImageURLs() {
		return (String[]) imageURLs.toArray(new String[0]);
	}

	private String genre;
	public String getGenre() {
		return genre;
	}

	public RadioItem(String genre) {
		this.genre = genre;
		titles = new ArrayList<String>();
		urls = new ArrayList<String>();
		descriptions = new ArrayList<String>();
		imageURLs = new ArrayList<String>();
	}

	public void add(String title, String url, String description,
			String imageURL) {
		this.titles.add(title);
		this.urls.add(url);
		this.descriptions.add(description);
		this.imageURLs.add(imageURL);
	}
}
