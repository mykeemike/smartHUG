/**
 * GREP - smartHUG
 * 
 * Récupération des Tweets 
 *
 * @author GREP
 * @version Version 1.0
 * 
 */
package hug_communiquent.traitement;

import java.io.*;

import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.json.*;

import android.app.ProgressDialog;
import android.net.http.*;
import android.os.*;

/**
 * Tâche de fond chargée de récupérer un l'objet JSON retournée par un WebService dont 
 * l'URL est fournie sous forme d'un String.
 * 
 * Lorsque la tâche se termine, l'une des deux méthodes du Listener est appelée, suivant si la tâche
 * s'est terminée correctement ou en erreur:
 * - onGetFromUrlResult (JSONObject json) : appelée avec l'objet JSON récupéré comme paramètre.
 * - onGetFromUrlError (Exception e)      : appelée en cas d'erreur avec l'Exception survenue en paramètre.
 * 
 * @author Peter DAEHNE - HEG-Genève
 * @version Version 1.0
 */
public class GetTweets extends AsyncTask <String, Void, JSONArray> {

	/* Listener chargé de traiter les résultats */
	public interface Listener {
		void onGetFromUrlResult (JSONArray json);  /* Appelée avec l'objet JSON récupéré comme paramètre */
		void onGetFromUrlError (Exception e);      /* Appelée en cas d'erreur avec l'Exception survenue en paramètre */
	} //  Listener 

	private Exception exception = null; /* Exception qui s'est éventuellement produite dans doInBackground() */ 
	private Listener listener = null;   /* Listener (éventuel) chargé de traiter les résultats */

	/* Constructeur */
	public GetTweets (Listener listener) {this.listener = listener;}

	@Override
	/* Récupération de l'object JSON à partir de l'URL donné en paramètre sous forme d'un String */
	protected JSONArray doInBackground (String... params) {
		String url = params[0];
		AndroidHttpClient httpClient = AndroidHttpClient.newInstance("Android");
		HttpGet httpGet = new HttpGet(url);
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
			JSONArray wrapper = new JSONArray(result.toString());
			JSONObject json = wrapper.getJSONObject(0);
			return new JSONArray(result.toString());
		} catch (Exception e) {
			exception = e;
			return null;
		} finally {
			httpClient.close();
		}
	} // doInBackground

	/* Le résultat est transmis au listener */
	protected void onPostExecute (JSONArray result) {

		if (listener == null) {return;}
		if (result != null) {listener.onGetFromUrlResult(result);} else {listener.onGetFromUrlError(exception);}
	} // onPostExecute

} // GetFromURL

