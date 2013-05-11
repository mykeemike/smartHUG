package hug_communiquent.traitement;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.json.*;

import android.net.http.*;
import android.os.*;

public class GetFromUrl extends AsyncTask <String, Void, String> {

	/* Listener chargé de traiter les résultats */
	public interface Listener {
		void onGetFromUrlResult (String result); /* Appelée avec l'objet JSON récupéré comme paramètre */
		void onGetFromUrlError (Exception e); /* Appelée en cas d'erreur avec l'Exception survenue en paramètre */
	} // Listener

	private Exception exception = null; /* Exception qui s'est éventuellement produite dans doInBackground() */
	private Listener listener = null; /* Listener (éventuel) chargé de traiter les résultats */

	/* Constructeur */
	public GetFromUrl (Listener listener) {this.listener = listener;}

	@Override
	/* Récupération de l'object JSON à partir de l'URL donné en paramètre sous forme d'un String */
	protected String doInBackground (String... params) {
		URL url = convertToURLEscapingIllegalCharacters(params[0]);
		AndroidHttpClient httpClient = AndroidHttpClient.newInstance("Android");
		HttpGet httpGet = new HttpGet(url.toString());
		try {
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream in = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder result = new StringBuilder();
			String line = reader.readLine();
			while (line != null) {
				result.append(line + "\n");
				line = reader.readLine();
			}
			in.close();
			return result.toString();
		} catch (Exception e) {
			exception = e;
			return null;
		} finally {
			httpClient.close();
		}
	} // doInBackground

	/* Le résultat est transmis au listener */
	protected void onPostExecute (String result) {
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
