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

	/* Listener charg� de traiter les r�sultats */
	public interface Listener {
		void onGetFromUrlResult (String result); /* Appel�e avec l'objet JSON r�cup�r� comme param�tre */
		void onGetFromUrlError (Exception e); /* Appel�e en cas d'erreur avec l'Exception survenue en param�tre */
	} // Listener

	private Exception exception = null; /* Exception qui s'est �ventuellement produite dans doInBackground() */
	private Listener listener = null; /* Listener (�ventuel) charg� de traiter les r�sultats */

	/* Constructeur */
	public GetFromUrl (Listener listener) {this.listener = listener;}

	@Override
	/* R�cup�ration de l'object JSON � partir de l'URL donn� en param�tre sous forme d'un String */
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

	/* Le r�sultat est transmis au listener */
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
