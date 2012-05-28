package filtermusic.Controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

/*
 * Author Rowan Schischka
 * RS mobile design
 * FileDownloader: provides functions for downloading files from http
 */
public class FileDownloader {
	// gets files and translates the filename into one specific for filtermusic
	// app
	public static File getFile(String directory, String name) {
		String filename = name.replaceAll("[\\\\/:*?\"<>|]", "");
		File file = new File(directory, filename);
		return file;
	}

	/*
	 * gets a file from http or if file is on disk returns that directory =
	 * folder where to save/find the file url = remote location of file
	 */
	public static File getHTTP(String directory, String url_) {
		if (url_ == null || url_.length() == 0) {
			return null;
		}
		try {
			URL url = new URL(url_);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.connect();

			File file = getFile(directory, url_);
			FileOutputStream fileOutput = new FileOutputStream(file);
			InputStream inputStream = urlConnection.getInputStream();
			byte[] buffer = new byte[1024];
			int bufferLength = 0;
			while ((bufferLength = inputStream.read(buffer)) > 0) {
				fileOutput.write(buffer, 0, bufferLength);
			}
			fileOutput.close();
			return file;
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return null;
	}

	/*
	 * turns file into bitmap
	 */
	public static Bitmap fileToBitmap(File file) {
		if (file == null)
			return null;
		Uri uri = Uri.fromFile(file);
		return BitmapFactory.decodeFile(uri.getPath());
	}

	/*
	 * gets a remote file and returns an xml object
	 */
	public static Document getDocument(String url, String directory) {
		if (url == null || directory == null)
			return null;
		Document doc = null;

		try {
			File file = FileDownloader.getHTTP(directory, url);
			if (file == null) {
				return null;
			}
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			try {
				db = dbf.newDocumentBuilder();
			} catch (ParserConfigurationException e1) {
				return null;
			}
			doc = db.parse(file);
			return doc;
		} catch (SAXException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}
}
