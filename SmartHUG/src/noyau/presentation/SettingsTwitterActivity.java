package noyau.presentation;

import hug_communiquent.traitement.Constantes;
import org.json.JSONObject;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SettingsTwitterActivity extends Activity {
	private Twitter twitter;
	private Button btnTwLogin, btnTwLogout;
	private RequestToken requestToken;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_twitter);
		btnTwLogin=(Button)findViewById(R.id.btnTwLogin);
		btnTwLogout=(Button)findViewById(R.id.btnTwLogout);
		
		SharedPreferences settings = getApplicationContext().getSharedPreferences("your_app_prefs", 0);
		Boolean connected = settings.getBoolean("user_logged_in", false);
		
		btnTwLogin.setEnabled(!connected);
		btnTwLogout.setEnabled(connected);
		
	}	
		
	private class OAuthLogin extends AsyncTask <String, Void, JSONObject> {
		
		final String CALLBACKURL = "myapp://mainactivity"; 
		
    	@Override
		protected JSONObject doInBackground(String... params) {
    		try {
    			twitter = new TwitterFactory().getInstance();
    			twitter.setOAuthConsumer(Constantes.CONSUMER_KEY, Constantes.CONSUMER_SECRET);
    			requestToken = twitter.getOAuthRequestToken(CALLBACKURL);
    			String authUrl = requestToken.getAuthenticationURL();
    			Intent intent= new Intent(Intent.ACTION_VIEW, Uri
    					.parse(authUrl));
    			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    			startActivity(intent);
    		} catch (TwitterException ex) {
    			Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
    			Log.e("in Main.OAuthLogin", ex.getMessage());
    		}
			return null;
    	} 
    }
	
	private class OAuthValidation extends AsyncTask <Uri, Void, JSONObject> {
		User user=null;
		@Override
		protected JSONObject doInBackground(Uri... params) {
			try {
				String verifier = params[0].getQueryParameter("oauth_verifier");
				AccessToken accessToken = twitter.getOAuthAccessToken(requestToken,verifier);
				String token = accessToken.getToken(), secret = accessToken.getTokenSecret();
				long userID = accessToken.getUserId();
				
				 // Save user_key and user_secret in user preferences and return
	            SharedPreferences settings = getBaseContext().getSharedPreferences("your_app_prefs", 0);
	            SharedPreferences.Editor editor = settings.edit();
	            editor.putString("user_key", token);
	            editor.putBoolean("user_logged_in", true);
	            
	            editor.putString("user_secret", secret);
	            editor.commit();
				 user = twitter.showUser(userID);
			} catch (TwitterException ex) {
				Log.e("Main.onNewIntent", "" + ex.getMessage());
			}
			return null;
		}
		
		protected void onPostExecute(JSONObject result) {
			Toast.makeText(getApplicationContext(), "Vous êtes loggué en tant que "+user.getScreenName(), Toast.LENGTH_LONG).show();
		}	    	
	}
	
	public void logoutTwitter(View v){
		twitter =null;
		btnTwLogin.setEnabled(true);
		SharedPreferences settings = getBaseContext().getSharedPreferences("your_app_prefs", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("user_logged_in", false);
        editor.commit();
		Toast.makeText(getApplicationContext(), "Vous vous êtes déloggué avec succès!", Toast.LENGTH_LONG).show();
		btnTwLogout.setEnabled(false);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Uri uri = intent.getData();
		new OAuthValidation().execute(uri);
		btnTwLogout.setEnabled(true);
		btnTwLogin.setEnabled(false);
	}
	
	public void loginTwitter(View v) {		
		new OAuthLogin().execute();
	}
}
