/**
 * GREP - smartHUG
 * 
 * Adapter personnalisé pour les tweets 
 *
 * @author GREP
 * @version Version 1.0
 * 
 */
package hug_communiquent.traitement;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;

import noyau.presentation.R;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import android.content.*;
import android.os.AsyncTask;
import android.text.util.Linkify;
import android.text.util.Linkify.TransformFilter;
import android.util.Patterns;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class TweetAdapter extends BaseAdapter {

	//private ArrayList<Tweet> data;      /* Informations à représenter */
	private ArrayList<Object> data;      /* Informations à représenter */
	private LayoutInflater inflater; /* Outil de création d'une vue "layout" à partir de sa description XML */
	private Boolean connected;
	private Context context;
	private Twitter twitter;
	private int pos;

	public TweetAdapter (Context context, ArrayList<Object> data, Boolean connected, Twitter twitter) {this.twitter=twitter;this.context=context;this.connected=connected;this.data = data; inflater = LayoutInflater.from(context);}
	
	/** Retourne le nombre d'items à représenter */ 
	public int getCount () {return data.size();}

	/** Retourne l'item d'indice position */
	public Object getItem (int position) {return data.get(position);}

	/** Retourne un identifiant pour l'item d'indice position */
	public long getItemId (int position) {return position;}

	private static class ViewHolder {
		TextView tvTweetName, tvTweetUsername,tvTweetAge,tvTweetText,tvTweetLink,tvTweetRetweet,tvTweetFavorites;
		Button btnTweetRT ,btnTweetFav ;
		Tweet tweet;
	} // ViewHolder
	
	private class Retweet extends AsyncTask <Integer, Void, JSONArray> {

		@Override
		protected JSONArray doInBackground(Integer... params) {
			try {
				twitter.retweetStatus(((twitter4j.Status) data.get(params[0])).getId());
			} catch (TwitterException e) {
				e.printStackTrace();
			}
			return null;
		}}
	
	private class Favorite extends AsyncTask <Integer, Void, JSONArray> {

		@Override
		protected JSONArray doInBackground(Integer... params) {
			try {
				twitter.createFavorite(((twitter4j.Status) data.get(params[0])).getId());
			} catch (TwitterException e) {
				e.printStackTrace();
			}
			return null;
		}}
			

	private void retweet(int pos){new Retweet().execute(pos);}
	private void favorite(int pos){new Favorite().execute(pos);}
		
		
	public String getAge(Date date){
		long now = Calendar.getInstance().getTime().getTime();
		long diff=(now-date.getTime())/1000;
		String finalDate="";
		
		if(diff<60) finalDate=diff+"s";
		else if(diff<3600) finalDate=diff/60+"m";
		else if(diff<216000) finalDate=diff/3600+"h";
		else finalDate=diff/3600/24+"d";
		
		return  finalDate;
	}
	
	
	/** Retourne la vue qui affiche l'item d'indice position */
	public View getView (int position, View convertView, ViewGroup parent) {
		
		ViewHolder vH;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.un_tweet, null);
			vH = new ViewHolder();
			vH.tvTweetName = (TextView)convertView.findViewById(R.id.tvTweetName);
			vH.tvTweetUsername = (TextView)convertView.findViewById(R.id.tvTweetUsername);
			vH.tvTweetAge = (TextView)convertView.findViewById(R.id.tvTweetAge);
			vH.tvTweetText = (TextView)convertView.findViewById(R.id.tvTweetText);
			vH.tvTweetLink = (TextView)convertView.findViewById(R.id.tvTweetLink);
			vH.tvTweetRetweet = (TextView)convertView.findViewById(R.id.tvTweetRetweet);
			vH.tvTweetFavorites = (TextView)convertView.findViewById(R.id.tvTweetFavorites);
			vH.btnTweetRT = (Button)convertView.findViewById(R.id.btnTweetRT);
			vH.btnTweetRT.setTag(position);
			vH.btnTweetFav = (Button)convertView.findViewById(R.id.btnTweetFav);
			vH.btnTweetFav.setTag(position);
			convertView.setTag(vH);
		} else {
			vH = (ViewHolder)convertView.getTag();
		}
	
		TransformFilter filter = new TransformFilter() {
		    public final String transformUrl(final Matcher match, String url) {
		        return match.group();
		    }
		};
		pos=position;
		if(connected){
			twitter4j.Status s = (Status) data.get(position);
			vH.btnTweetRT.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					Toast.makeText(context, "Retweet effectué!", Toast.LENGTH_LONG).show();
					retweet((Integer) v.getTag());
					v.setEnabled(false);
				}
			});
			vH.btnTweetRT.setEnabled(!s.isRetweetedByMe());
			vH.btnTweetFav.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(context, "Ajouté aux Favoris", Toast.LENGTH_LONG).show();
					favorite((Integer) v.getTag());
					v.setEnabled(false);
				}
			});
			vH.tvTweetName.setText(s.getUser().getName());
			vH.tvTweetUsername.setText(s.getUser().getScreenName());
			vH.tvTweetAge.setText(getAge(s.getCreatedAt()));
			vH.tvTweetText.setText(s.getText());
			vH.tvTweetLink.setText("");//s.getURLEntities()[0].getDisplayURL());

			
			//Linkify.addLinks(vH.tvTweetText, Linkify.WEB_URLS);
			int nbRT= (int) s.getRetweetCount();
			if (nbRT == 0) {vH.tvTweetRetweet.setText("");}else{vH.tvTweetRetweet.setText(nbRT+ (nbRT>1?" retweets":" retweet"));}
		}else{
			Tweet t= (Tweet) data.get(position);
			vH.tvTweetName.setText(t.getName());
			vH.tvTweetUsername.setText("@"+t.getUsername());
			vH.tvTweetAge.setText(t.getAge());
			vH.tvTweetText.setText(t.getText());
			Linkify.addLinks(vH.tvTweetText, Linkify.WEB_URLS);
			
			vH.tvTweetLink.setText("");//t.getUrl());

			int nbRT= t.getRetweets();
			int nbFav= t.getFavorites();
			if (nbRT != 0) {vH.tvTweetRetweet.setText(nbRT+ (nbRT>1?" retweets":" retweet"));}
			if (nbFav!= 0) {vH.tvTweetFavorites.setText(nbFav +(nbFav>1?" favoris":" favori"));}
		}

		if(connected){
			vH.btnTweetFav.setVisibility(View.VISIBLE);
			vH.btnTweetRT.setVisibility(View.VISIBLE);
		} else{
			vH.btnTweetFav.setVisibility(View.GONE);
			vH.btnTweetRT.setVisibility(View.GONE);
		}
		
		Pattern mentionPattern = Pattern.compile("@([A-Za-z0-9_-]+)");
		String mentionScheme = "http://www.twitter.com/";
		Linkify.addLinks(vH.tvTweetText, mentionPattern, mentionScheme, null, filter);

		Pattern hashtagPattern = Pattern.compile("#([A-Za-z0-9_-]+)");
		String hashtagScheme = "http://www.twitter.com/search/";
		Linkify.addLinks(vH.tvTweetText, hashtagPattern, hashtagScheme, null, filter);

		Pattern urlPattern = Patterns.WEB_URL;
		Linkify.addLinks(vH.tvTweetText, urlPattern, null, null, filter);
		
		return convertView;
	} // getView
} // TweetAdapter