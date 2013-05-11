package hug_communiquent.traitement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.*;

public class YoutubeWallInfo {

	private static int nbVideos = 10;

	private ArrayList<Video> lstVideo;

	/* Constructeur: définit les attributs à partir de json */
	public YoutubeWallInfo (JSONObject json) throws JSONException {
		lstVideo = new ArrayList<Video>();
		JSONArray wall = json.getJSONObject("feed").getJSONArray("entry");
		for (int i = 0; i < nbVideos; i++){
			String idVideo = wall.getJSONObject(i).getJSONObject("media$group").getJSONObject("yt$videoid").getString("$t");
			String title = wall.getJSONObject(i).getJSONObject("title").getString("$t");
			String description = wall.getJSONObject(i).getJSONObject("media$group").getJSONObject("media$description").getString("$t");
			String nbViews = wall.getJSONObject(i).getJSONObject("yt$statistics").getString("viewCount");
			String temps = wall.getJSONObject(i).getJSONObject("media$group").getJSONObject("yt$duration").getString("seconds");
			String date = wall.getJSONObject(i).getJSONObject("published").getString("$t");
			lstVideo.add(new Video(idVideo, title, description, nbViews, TimeFormat(temps), DateFormat(date)));
		}
	} // Constructeur

	private String TimeFormat(String temps)
	{
		int secsIn = Integer.parseInt(temps);
		int hours = secsIn / 3600,
				remainder = secsIn % 3600,
				minutes = remainder / 60,
				seconds = remainder % 60;

		return ( (hours < 10 ? "0" : "") + hours
				+ ":" + (minutes < 10 ? "0" : "") + minutes
				+ ":" + (seconds< 10 ? "0" : "") + seconds );
	}


	private String DateFormat(String date){
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		SimpleDateFormat fmt2 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		try {
			Date newDate = fmt.parse(date);
			return fmt2.format(newDate);
		}
		catch(ParseException pe) {
			return "Date";    
		}
	}

	/* Accesseurs */
	public ArrayList<Video> getLstVideo () {return lstVideo;}

} // GeoCodingInfo
