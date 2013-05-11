/**
 * GREP - smartHUG
 * 
 * Proxy du pilier communication
 *
 * @author HARTM
 * @version Version 1.0
 * 
 */
package hug_communiquent.traitement;

import hug_communiquent.presentation.NewsActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import noyau.presentation.R;

public class ProxyHugCommuniquent {
	private final int RS_CHOIX = 1;
	private final int RS_FACEBOOK = 0;
	private final int RS_TWITTER = 1;
	private final int RS_YOUTUBE = 2;
	private final int RS_RSS = 3;
	
	private Context context;
	private Activity activity;
	
	public ProxyHugCommuniquent (Context context, Activity activity){
		this.context = context;
		this.activity = activity;
	}
	
	public void displayFacebook(){
		Intent intent = new Intent(context, NewsActivity.class);
		intent.putExtra("RS", RS_FACEBOOK);
		activity.startActivity(intent);
	}
	
	public void displayTwitter(){
		Intent intent = new Intent(context, NewsActivity.class);
		intent.putExtra("RS", RS_TWITTER);
		activity.startActivity(intent);
	}

	public void displayYoutube(){
		Intent intent = new Intent(context, NewsActivity.class);
		intent.putExtra("RS", RS_YOUTUBE);
		activity.startActivity(intent);
	}

	public void displayRSS(){
		Intent intent = new Intent(context, NewsActivity.class);
		intent.putExtra("RS", RS_RSS);
		activity.startActivity(intent);
	}
	
	
}
