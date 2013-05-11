/**
 * GREP - smartHUG
 * 
 * Accès aux différentes sources d'informations 
 *
 * @author HARTM
 * @version Version 1.0
 * 
 */
package hug_communiquent.presentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSItem;
import org.mcsoxford.rss.RSSReader;

import com.facebook.Session;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import noyau.presentation.R;
import noyau.presentation.SettingsTwitterActivity;
import noyau.traitement.GeoTools;
import noyau.traitement.ProxyNoyau;
import hug_communiquent.traitement.*;

import android.support.v4.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.*;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class NewsActivity extends TabCompatActivity {
	/* Bouton sur lequel l'utilisateur a cliqué */
	private final int RS_FACEBOOK = 0;
	private final int RS_TWITTER = 1;
	private final int RS_YOUTUBE = 2;
	private final int RS_RSS = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);

		TabHelper tabHelper = getTabHelper();
		/* Création des tabs avec leurs fragments respectifs */
		CompatTab facebookTab = tabHelper.newTab("Facebook")
				.setText(R.string.stFacebook)
				.setIcon(R.drawable.facebook_icon_small)
				.setTabListener(new InstantiatingTabListener(this, FacebookFragment.class));
		tabHelper.addTab(facebookTab);     

		CompatTab twitterTab = tabHelper.newTab("Twitter")
				.setText(R.string.stTwitter)
				.setIcon(R.drawable.twitter_icon_small)
				.setTabListener(new InstantiatingTabListener(this, TwitterFragment.class));
		tabHelper.addTab(twitterTab);   

		CompatTab youtubeTab = tabHelper.newTab("Youtube")
				.setText(R.string.stYoutube)
				.setIcon(R.drawable.youtube_small_icon)
				.setTabListener(new InstantiatingTabListener(this, YoutubeFragment.class));
		tabHelper.addTab(youtubeTab);    

		CompatTab rssTab = tabHelper.newTab("Flux RSS")
				.setText(R.string.stRSS)
				//.setIcon(R.drawable.rss_icon)
				.setTabListener(new InstantiatingTabListener(this, RSSFragment.class));
		tabHelper.addTab(rssTab);       

		/* Récupère la sélection de l'utilisateur */
		switch(getIntent().getIntExtra("RS", 0)){
		case RS_FACEBOOK: break;
		case RS_TWITTER: break;
		case RS_YOUTUBE: break;
		case RS_RSS: break;
		}

		/* ********************************* */
	}

	public static class InstantiatingTabListener implements CompatTabListener {

		private final TabCompatActivity mActivity;
		private final Class mClass;

		/**
		 * Constructor used each time a new tab is created.
		 *
		 * @param activity The host Activity, used to instantiate the fragment
		 * @param cls      The class representing the fragment to instantiate
		 */
		public InstantiatingTabListener(TabCompatActivity activity, Class<? extends ListFragment> cls) {
			mActivity = activity;
			mClass = cls;
		}

		/* The following are each of the ActionBar.TabListener callbacks */
		@Override
		public void onTabSelected(CompatTab tab, FragmentTransaction ft) {
			// Check if the fragment is already initialized
			Fragment fragment = tab.getFragment();
			if (fragment == null) {
				// If not, instantiate and add it to the activity
				fragment = Fragment.instantiate(mActivity, mClass.getName());
				tab.setFragment(fragment);
				ft.add(android.R.id.tabcontent, fragment, tab.getTag());
			} else {
				// If it exists, simply attach it in order to show it
				ft.attach(fragment);
			}
		}

		@Override
		public void onTabUnselected(CompatTab tab, FragmentTransaction ft) {
			Fragment fragment = tab.getFragment();
			if (fragment != null) {
				// Detach the fragment, because another one is being attached
				ft.detach(fragment);
			}
		}

		@Override
		public void onTabReselected(CompatTab tab, FragmentTransaction ft) {
			// User selected the already selected tab. Do nothing.
		}

	}
	/* 
	 * Classe statique qui modélise le fragment Facebook
	 * */
	public static class FacebookFragment extends ListFragment {
		/*
		String facebook_arStr[] = new String[]{
				"FACEBOOK 1",
				"FACEBOOK 2",
				"FACEBOOK 3",
				"FACEBOOK 4",
				"FACEBOOK 5",
				"FACEBOOK 6",
				"FACEBOOK 7",
				"FACEBOOK 8"
		};

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, facebook_arStr);



			setListAdapter(adapter);


			return super.onCreateView(inflater, container, savedInstanceState);
		}

		START*/
		/* Constantes */
		private static final String URL = "https://graph.facebook.com/hopitaux.universitaires.geneve/posts?";
		/* Variables globales */
		TextView tvStatus;
		FacebookAdapter adapter;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			String requestToken = "https://graph.facebook.com/oauth/access_token?client_id=542368772462635&client_secret=715605faab5c7e1a137bd7a00187d95f&grant_type=client_credentials";
			new GetToken(requestToken);			
			return super.onCreateView(inflater, container, savedInstanceState);
		}

		private void chargerListe(ArrayList<Publication> publications){
			int status = 0;
			Session session = Session.getActiveSession();
			if (session != null && !session.isClosed()){
				status = 1;
			}      
			if (publications == null) {System.out.println("error");return;}
			adapter = new FacebookAdapter(getActivity(), publications, status);
			setListAdapter(adapter);  		
		}

		/* Listener de la tâche GetFromUrl: l'objet JSON a été récupéré */
		public void displayJSON (JSONObject json) throws JSONException {	    	
			FacebookInfo infos = null;
			try {
				infos = new FacebookInfo(json);
				chargerListe(infos.getPublications()); 
			} catch (JSONException e) {System.out.println("erreur"); return;}   		 
		} // displayJSON

		/* Classe membre qui implémente le Listener de la classe insynchrone */
		public class GetToken implements GetFromUrl.Listener {
			//private NetworkProvider networkProvider;
			private String url;
			public GetToken(String url) {
				this.url = url;
				new GetFromUrl(this).execute(url);
			}

			@Override
			public void onGetFromUrlResult(String result) {
				// TODO Auto-generated method stub
				//System.out.println(URL+result);
				new GetJSON(URL+result);			
			}

			@Override
			public void onGetFromUrlError(Exception e) {
				// TODO Auto-generated method stub

			}
		} //GetToken

		/* Classe membre qui implémtente de Listener de la classe insynchrone */
		public class GetJSON implements GetFromUrl.Listener {
			public GetJSON(String url) {
				new GetFromUrl(this).execute(url);
			}

			@Override
			public void onGetFromUrlResult(String result) {
				// TODO Auto-generated method stub
				try {
					displayJSON(new JSONObject(result));
				} catch(Exception e){


				}
			}

			@Override
			public void onGetFromUrlError(Exception e) {
				// TODO Auto-generated method stub
			}
		} //GetFromUrlResult			

	}

	/* 
	 * Classe statique qui modélise le fragment Twitter
	 * */
	public static class TwitterFragment extends ListFragment  implements GetTweets.Listener {
		private static final int NB_TWEETS=10;
		public Twitter twitter=null;

		ArrayList<Status> alStatus=new ArrayList<Status>();
		RequestToken requestToken;

		Boolean connected;
		ArrayList<Object> lsTweet=new ArrayList<Object>();
		AsyncTask<String, Void, JSONArray> at =null;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			//setListShown(true);
			//setListShownNoAnimation(true);
			new GetConnection().execute(this);
			Log.e("TW",">>>");

			/** Creating array adapter to set data in listview */
			TweetAdapter adapter = new TweetAdapter(getActivity(), lsTweet, connected,twitter);
			setListAdapter(adapter);
			return super.onCreateView(inflater, container, savedInstanceState);
		}

		private class GetConnection extends AsyncTask <TwitterFragment, Void, JSONArray> {

			TwitterFragment t;
			
			ProgressDialog dialog;
			/*protected void onPreExecute() {
				dialog = new ProgressDialog(getActivity());
				dialog.setCancelable(true);
				dialog.setMessage("Retrieving tweets...");
				dialog.show();
			}*/
			
			@Override
			protected JSONArray doInBackground(TwitterFragment... params) {
				t=params[0];
				SharedPreferences settings = getActivity().getSharedPreferences("your_app_prefs", 0);
				connected = settings.getBoolean("user_logged_in", false);
				
				if(connected){
					try {
						String userKey = settings.getString("user_key", "");

						Log.e("TW","userKey:"+userKey);
						String userSecret = settings.getString("user_secret", "");
						Log.e("TW","secretKey:"+userSecret);

						ConfigurationBuilder builder = new ConfigurationBuilder();
						builder.setOAuthConsumerKey(Constantes.CONSUMER_KEY);
						builder.setOAuthConsumerSecret(Constantes.CONSUMER_SECRET);

						AccessToken accessToken = new AccessToken(userKey, userSecret);
						twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
						
					} catch (Exception e) { 
						Log.e("TW","Exception:"+e);
					}
				}
				return null;
			}

			protected void onPostExecute (JSONArray result) {
				//dialog.cancel();
				if(!connected) {
					at = new GetTweets(t).execute(Constantes.URL_API);

				}else{
					at = new GetTweetsConnected().execute();

				}
				/*TweetAdapter adapter = new TweetAdapter(getActivity(), lsTweet,connected,twitter);
				setListAdapter(adapter);*/
			}
		}

		private class GetTweetsConnected extends AsyncTask <String, Void, JSONArray> {
			ProgressDialog dialog;
			/*protected void onPreExecute() {
				dialog = new ProgressDialog(getActivity());
				dialog.setCancelable(true);
				dialog.setMessage("Retrieving tweets...");
				dialog.show();

			}*/

			@Override
			protected JSONArray doInBackground(String... params) {
				try {
					alStatus=(ArrayList<twitter4j.Status>) twitter.getUserTimeline(Constantes.TWITTER_ACCOUNT,new Paging(1,NB_TWEETS));
					lsTweet.clear();
					lsTweet.addAll(alStatus);
				} catch (TwitterException e) {
					e.printStackTrace();
				}
				return null;
			} 
			protected void onPostExecute (JSONArray result) {
				//dialog.cancel();
				TweetAdapter adapter = new TweetAdapter(getActivity(), lsTweet,connected,twitter);
				setListAdapter(adapter);
			}
		}

		@Override
		public void onDestroy(){
			super.onDestroy();
			at.cancel(true);
		}

		@Override
		public void onGetFromUrlResult(JSONArray json) {
			lsTweet = new ArrayList<Object>();
			String twText="", twName="", twUsername="", twLink="";
			String twDate = null;
			int twRT=0, twFav=0;
			ArrayList<String> alUrls;
			ArrayList<String> twTags;

			for(int i=0;i<json.length();i++){
				try {
					JSONObject obj=json.getJSONObject(i);
					JSONObject user=obj.getJSONObject("user");
					twDate=obj.getString("created_at");
					twText=obj.getString("text");
					twName=user.getString("name");
					twUsername=user.getString("screen_name");
					twRT= obj.getInt("retweet_count");
					twFav=obj.getInt("favorite_count");
					twLink=(String) obj.getJSONObject("entities").getJSONArray("urls").getJSONObject(0).getString("url");

					Tweet t = new Tweet(twName, twUsername, twText,twLink,twDate, twRT, twFav);
					lsTweet.add(t);
				} catch (JSONException e) {
					e.printStackTrace();
				}			
			}		
			TweetAdapter adapter = new TweetAdapter(getActivity(), lsTweet,connected,twitter);
			setListAdapter(adapter);
		}
		@Override
		public void onGetFromUrlError(Exception e) {
			Toast.makeText(getActivity(), "Une erreur est survenue", Toast.LENGTH_LONG).show();
		}
	}

	/* 
	 * Classe statique qui modélise le fragment Youtube
	 * */
	public static class YoutubeFragment extends ListFragment  implements GetFromUrl.Listener {
		private YoutubeAdapter adapter = null;

		private LayoutInflater inflater;
		private ArrayList<Video> data=new ArrayList<Video>();
		private AsyncTask <String, Void, String> at;
		private static class ViewHolder {
			TextView tvIdVideo, tvTitreVideo, tvDescVideo, tvNbViewVideo, tvTempsVideo, tvDateVideo;
		} // ViewHolder

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			/** Creating array adapter to set data in listview */
			//ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), R.layout.youtube_liste);

			/** Setting the array adapter to the listview */
			//definirListener();
			at=new GetFromUrl(this).execute("http://gdata.youtube.com/feeds/api/users/KIOSKVIDEOHUG/uploads/?v=2&alt=json");

			adapter = new YoutubeAdapter(getActivity(), data);
			setListAdapter(adapter);
			//definirVariables();
			return super.onCreateView(inflater, container, savedInstanceState);
		}

		@Override
		public void onStart() {
			super.onStart();

			getListView().setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					Video item =(Video) arg0.getItemAtPosition(arg2);
					Intent intent=new Intent(getActivity(),YoutubeVideo.class);
					intent.putExtra("vSel", item.getId());
					startActivity(intent);
				}
			});
		}

		@Override
		public void onDestroy(){
			super.onDestroy();
			at.cancel(true);
		}


		private class YoutubeAdapter extends BaseAdapter{
			private Context context;
			ArrayList<Video> data = new ArrayList<Video>();
			YoutubeAdapter(Context context, ArrayList<Video> data) {
				this.context=context;this.data = data; inflater = LayoutInflater.from(context);
			}

			@Override
			public int getCount(){return data.size();}


			@Override
			public Video getItem(int position) {
				return(data.get(position));
			}

			@Override
			public long getItemId(int position) {
				return(position);
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder vH;

				if (convertView==null) {  
					inflater = LayoutInflater.from(getActivity());
					convertView = inflater.inflate(R.layout.une_video, null);
					vH = new ViewHolder();
					vH.tvIdVideo = (TextView)convertView.findViewById(R.id.tvIdVideo);
					vH.tvTitreVideo = (TextView)convertView.findViewById(R.id.tvTitreVideo);
					vH.tvDescVideo = (TextView)convertView.findViewById(R.id.tvDescVideo);
					vH.tvNbViewVideo = (TextView)convertView.findViewById(R.id.tvNbViewVideo);
					vH.tvTempsVideo = (TextView)convertView.findViewById(R.id.tvTempsVideo);
					vH.tvDateVideo = (TextView)convertView.findViewById(R.id.tvDateVideo);
					convertView.setTag(vH);
				} else {
					vH = (ViewHolder)convertView.getTag();
				}

				Video v = (Video) data.get(position);
				vH.tvTitreVideo.setText(v.getTitre());
				vH.tvTempsVideo.setText(v.getTemps());
				vH.tvNbViewVideo.setText(v.getnbViews());
				vH.tvDateVideo.setText(v.getDate());
				vH.tvDescVideo.setText(v.getDescription());
				vH.tvIdVideo.setText(v.getId());
				return convertView;
			}

		}



		/* Listener de la tâche GetFromUrl: l'objet JSON a été récupéré */
		public ArrayList<Video> loadJSON (JSONObject json) {
			YoutubeWallInfo infos = null;
			try {
				infos = new YoutubeWallInfo(json);
				return infos.getLstVideo();
			} catch (JSONException e) {return new ArrayList<Video>();}
		} // displayJSON

		public class GetJSON implements GetFromUrl.Listener {
			private String url;
			public GetJSON(String url) {
				this.url = url;
				new GetFromUrl(this).execute(url);
			}

			@Override
			public void onGetFromUrlResult(String result) {
				// TODO Auto-generated method stub
				try {
					data= loadJSON(new JSONObject(result));
				} catch(Exception e){

				}
			}

			@Override
			public void onGetFromUrlError(Exception e) {
				// TODO Auto-generated method stub
			}
		} //GetFromUrlResult

		@Override
		public void onGetFromUrlResult(String result) {
			// TODO Auto-generated method stub
			try {
				Log.e("YT",result);
				data= loadJSON(new JSONObject(result));
			} catch(Exception e){

			}
			adapter = new YoutubeAdapter(getActivity(), data);
			setListAdapter(adapter);

		}

		@Override
		public void onGetFromUrlError(Exception e) {
			// TODO Auto-generated method stub

		}
	}

	/* 
	 * Classe statique qui modélise le fragment RSS
	 * */
	public static class RSSFragment extends ListFragment {
		private FeedAdapter adapter=null;
		private LayoutInflater inflater; /* Outil de création d'une vue "layout" à partir de sa description XML */
		private FeedTask ft = null;
		private static class ViewHolder {
			TextView tvRssTitle, tvRssContent;
			RSSItem rssItem;
		} // ViewHolder

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			/*TextView textView = new TextView(getActivity());
            textView.setGravity(Gravity.CENTER);
            textView.setText(R.string.stRSS);
            return textView;*/
			Log.e("RSS","onCreateView");
			//Récupération des feeds depuis la classe Feed
			ArrayList<Feed> alFeeds = Feed.getFeeds();
			//Affichage du 3ème Feed uniquement (HUG general)
			Feed f = alFeeds.get(2);  
			Log.e("RSS",f.getUrl());
			loadUrl(f.getUrl());
			//        	for(Feed f : alFeeds){
			//        		loadUrl(f.getUrl());
			//        	}        	
			/** Creating array adapter to set data in listview */
			//ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, rss_ars);

			/** Setting the array adapter to the listview */

			//setListAdapter(adapter);


			return super.onCreateView(inflater, container, savedInstanceState);
		}

		@Override
		public void onStart() {
			super.onStart();

			getListView().setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					RSSItem item =(RSSItem) arg0.getItemAtPosition(arg2);
					onItemSelected(item);
				}
			});
		}

		@Override
		public void onSaveInstanceState(Bundle state) {
			state.putInt("1",
					getListView().getCheckedItemPosition());
		}

		protected void restoreState(Bundle state) {
			if (state!=null) {
				int position=state.getInt("1", -1);

				if (position>-1) {
					getListView().setItemChecked(position, true);
				}
			}
		}

		public void loadUrl(String url) {
			Log.e("RSS","loadURL");
			ft = (FeedTask) new FeedTask().execute(url);
		}

		@Override
		public void onDestroy(){
			super.onDestroy();
			ft.cancel(true); 
		}

		public void onItemSelected(RSSItem item) {
			startActivity(new Intent(Intent.ACTION_VIEW, item.getLink()));
		}

		private void setFeed(RSSFeed feed) {
			adapter=new FeedAdapter(feed);
			setListAdapter(adapter);
		}
		/*
		 * Réalise la tâche asychrone du fragment RSS
		 * */
		private class FeedTask extends AsyncTask<String, Void, RSSFeed> {
			private RSSReader reader=new RSSReader();
			private Exception e=null;

			@Override
			public RSSFeed doInBackground(String... urls) {
				Log.e("RSS","doInBackground");
				RSSFeed result=null;

				try {
					result=reader.load(urls[0]);
				}
				catch (Exception e) {
					this.e=e;
				}

				return(result);
			}

			@Override
			public void onPostExecute(RSSFeed feed) {
				Log.e("RSS","postExec");
				if (e==null) {
					setFeed(feed);
				}
				else {
					Log.e("ItemsFragment", "Exception parsing feed", e);

				}
			}
		}
		/* 
		 * Classe privée qui modélise l'adapteur personnalisé du RSS
		 * */
		private class FeedAdapter extends BaseAdapter {
			RSSFeed feed=null;

			FeedAdapter(RSSFeed feed) {
				super();

				this.feed=feed;
			}


			@Override
			public int getCount() {
				return(feed.getItems().size());
			}

			@Override
			public RSSItem getItem(int position) {
				return(feed.getItems().get(position));
			}

			@Override
			public long getItemId(int position) {
				return(position);
			}

			@Override
			public View getView(int position, View convertView,
					ViewGroup parent) {
				Log.e("RSS","getView");
				ViewHolder vH;

				if (convertView==null) {  
					inflater = LayoutInflater.from(getActivity());
					convertView = inflater.inflate(R.layout.un_rss, null);
					vH = new ViewHolder();
					vH.tvRssTitle = (TextView)convertView.findViewById(R.id.tvRssTitle);
					vH.tvRssContent = (TextView)convertView.findViewById(R.id.tvRssContent);
					convertView.setTag(vH);
				}else{
					vH = (ViewHolder)convertView.getTag();
				}

				RSSItem item=(RSSItem)getItem(position);
				vH.tvRssTitle.setText(item.getTitle());
				Log.e("RSS","CONTENT : \""+item.getDescription()+"\"");
				return convertView;
			}
		}
	}


}