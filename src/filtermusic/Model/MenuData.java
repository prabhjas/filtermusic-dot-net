package filtermusic.Model;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.text.Html;
import android.util.Log;
import filtermusic.Controllers.FileDownloader;

/*
 * Author: Rowan Schischka
 * RS mobile design www.rsmobiledesign.com
 * holds radio station lists
 */
public class MenuData {

	private static int currentRadioGroup = 0;

	public static void setCurrentRadioGroup(int currentRadioGroup) {
		MenuData.currentRadioGroup = currentRadioGroup;
	}

	public static int getCurrentRadioGroup() {
		return currentRadioGroup;
	}

	private static List<RadioItem> radioItemList = new ArrayList<RadioItem>();

	public static void setRadioItemList(List<RadioItem> radioItemList) {
		MenuData.radioItemList = radioItemList;
	}

	public static List<RadioItem> getRadioItemList() {
		return radioItemList;
	}

	private static String[] genreNamesList = null;

	public static String[] getGenreNamesList() {
		return genreNamesList;
	}

	public static void setGenreNamesList(String[] genreNamesList) {
		MenuData.genreNamesList = genreNamesList;
	}

	public static Document doc = null;
	private static int nextId = 100;
	private static boolean initStarted = false;

	public static void setInitStarted(boolean initialised) {
		MenuData.initStarted = initialised;
	}

	public static boolean isInitStarted() {
		return initStarted;
	}

	public static void resetInitialised() {
		initStarted = false;
		initComplete = false;
	}

	// sets up menu data from xml file from filtermusic, returns false is theres
	// a download error

	public static boolean init(String directory) {
		initStarted = true;
		// retreive xml from filtermusic if dont already have it
		if (doc == null) {
			doc = FileDownloader.getDocument(
					"http://www.filtermusic.net/xml/list.2.0.xml", directory);
		}
		if (doc == null) {
			return false;
		}
		if (genreNamesList == null) {
			NodeList genres = doc.getElementsByTagName("g");
			NodeList stations;
			Node station;
			RadioItem ri;
			String genre;
			String http = "http://";
			String title;
			String url;
			String imageURL;
			String reducedImageURL = doc.getElementsByTagName("r").item(0)
					.getTextContent();
			/*
			 * <r> www.filtermusic.net/sites/default/files/imagecache/
			 * android_350x196_greyscale/ </r> <g id="Downtempo / Ambient"> <t
			 * id="SomaFM Space station"> <u>205.188.215.227:8014</u>
			 * <i>somafm-space-station.jpg</i>
			 */
			genreNamesList = new String[genres.getLength()];
			for (int i = 0; i < genres.getLength(); i++) {
				genre = Html.fromHtml(
						genres.item(i).getAttributes().getNamedItem("id")
								.getNodeValue()).toString();
				genreNamesList[i] = genre;
				ri = new RadioItem(genre);
				radioItemList.add(ri);
				Log.i("GENRE", genreNamesList[i]);
				stations = genres.item(i).getChildNodes();
				for (int a = 0; a < stations.getLength(); a++) {
					station = stations.item(a);
					title = Html.fromHtml(
							station.getAttributes().getNamedItem("id")
									.getNodeValue()).toString();
					NodeList items = station.getChildNodes();
					url = Html.fromHtml(http + items.item(0).getTextContent())
							.toString();
					imageURL = Html.fromHtml(
							http + reducedImageURL
									+ items.item(1).getTextContent())
							.toString();
					Log.i("STATION INFO", title + " : " + url + " : "
							+ imageURL);
					ri.add(title, url, "", imageURL);
					title = genre = url = imageURL = null;
				}
				/*
				 * current = stations.item(a); if
				 * (current.getNodeName().equals("musicgenre")) { genre =
				 * Html.fromHtml(current.getTextContent()) .toString();//
				 * debug(genre); } else if
				 * (current.getNodeName().equals("title")) { title =
				 * Html.fromHtml(current.getTextContent()) .toString();//
				 * debug(title); } else if
				 * (current.getNodeName().equals("serverURL")) { url =
				 * Html.fromHtml(current.getTextContent()) .toString(); } else
				 * if (current.getNodeName().equals("imageURL")) { imageURL =
				 * Html.fromHtml(current.getTextContent()) .toString(); } } //
				 * got all radio station info now add to genre list if
				 * (!genreNamesList_.contains(genre)) {
				 * genreNamesList_.add(genre); radioItemList.add(new
				 * RadioItem(genre)); } int index =
				 * genreNamesList_.indexOf(genre);
				 * radioItemList.get(index).add(title, url, "", imageURL); title
				 * = genre = url = imageURL = null;
				 */
			}
		}
		initComplete = true;
		return true;
	}

	private static boolean initComplete = false;

	public static boolean isInitComplete() {
		return initComplete;
	}

	// counter for view id's
	public static int getNextId() {
		return nextId++;
	}

	public static void setNextId(int nextId) {
		MenuData.nextId = nextId;
	}

	public static void setupGenreView(int viewID) {
		currentGenre = viewID;
	}

	static int currentGenre = 0;

	public static void setCurrentGenre(int currentGenre) {
		MenuData.currentGenre = currentGenre;
	}

	public static int getCurrentGenre() {
		return currentGenre;
	}

}