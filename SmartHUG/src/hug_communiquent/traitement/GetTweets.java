/**
 * GREP - smartHUG
 * 
 * R�cup�ration des Tweets 
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
 * T�che de fond charg�e de r�cup�rer un l'objet JSON retourn�e par un WebService dont 
 * l'URL est fournie sous forme d'un String.
 * 
 * Lorsque la t�che se termine, l'une des deux m�thodes du Listener est appel�e, suivant si la t�che
 * s'est termin�e correctement ou en erreur:
 * - onGetFromUrlResult (JSONObject json) : appel�e avec l'objet JSON r�cup�r� comme param�tre.
 * - onGetFromUrlError (Exception e)      : appel�e en cas d'erreur avec l'Exception survenue en param�tre.
 * 
 * @author Peter DAEHNE - HEG-Gen�ve
 * @version Version 1.0
 */
public class GetTweets extends AsyncTask <String, Void, JSONArray> {

	/* Listener charg� de traiter les r�sultats */
	public interface Listener {
		void onGetFromUrlResult (JSONArray json);  /* Appel�e avec l'objet JSON r�cup�r� comme param�tre */
		void onGetFromUrlError (Exception e);      /* Appel�e en cas d'erreur avec l'Exception survenue en param�tre */
	} //  Listener 

	private Exception exception = null; /* Exception qui s'est �ventuellement produite dans doInBackground() */ 
	private Listener listener = null;   /* Listener (�ventuel) charg� de traiter les r�sultats */

	/* Constructeur */
	public GetTweets (Listener listener) {this.listener = listener;}

	@Override
	/* R�cup�ration de l'object JSON � partir de l'URL donn� en param�tre sous forme d'un String */
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

	/* Le r�sultat est transmis au listener */
	protected void onPostExecute (JSONArray result) {

		if (listener == null) {return;}
		if (result != null) {listener.onGetFromUrlResult(result);} else {listener.onGetFromUrlError(exception);}
	} // onPostExecute

} // GetFromURL

