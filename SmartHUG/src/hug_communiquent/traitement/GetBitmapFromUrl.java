package hug_communiquent.traitement;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.*;

public class GetBitmapFromUrl extends AsyncTask <String, Void, Bitmap> {

	/* Listener chargé de traiter les résultats */
	public interface Listener {
		void onGetFromUrlResult (Bitmap result); /* Appelée avec l'objet JSON récupéré comme paramètre */
		void onGetFromUrlError (Exception e); /* Appelée en cas d'erreur avec l'Exception survenue en paramètre */
	} // Listener

	private Exception exception = null; /* Exception qui s'est éventuellement produite dans doInBackground() */
	private Listener listener = null; /* Listener (éventuel) chargé de traiter les résultats */

	/* Constructeur */
	public GetBitmapFromUrl (Listener listener) {this.listener = listener;}

	@Override
	/* Récupération de l'object JSON à partir de l'URL donné en paramètre sous forme d'un String */
	protected Bitmap doInBackground (String... params) {
		String imageUrl = params[0];
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream((InputStream)new URL(imageUrl).getContent());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	} // doInBackground

	/* Le résultat est transmis au listener */
	protected void onPostExecute (Bitmap result) {
		if (listener == null) {return;}
		if (result != null) {listener.onGetFromUrlResult(result);} else {listener.onGetFromUrlError(exception);}
	} // onPostExecute

	public URL convertToURLEscapingIllegalCharacters(String string){
		try {
			String decodedURL = URLDecoder.decode(string, "UTF-8");
			URL url = new URL(decodedURL);
			URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
			return uri.toURL();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
} // GetFromURL