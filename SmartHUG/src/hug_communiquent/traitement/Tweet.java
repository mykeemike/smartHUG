/**
 * GREP - smartHUG
 * 
 * Modélise un Tweet
 *
 * @author GREP
 * @version Version 1.0
 * 
 */
package hug_communiquent.traitement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.graphics.Bitmap;

public class Tweet {
	
	private String name, username,text,link;
	private String date;
	private int retweets,favorites;
	private Bitmap avatar;

	public Tweet(String name, String username,String text, String link,String date,int retweets, int favorites){this.retweets=retweets;this.favorites=favorites;this.avatar=avatar;this.name=name;this.username=username;this.text=text; this.link=link; this.date=date;}	
	public String getName(){return name;}
	public String getUsername(){return username;}
	public String getText(){return text;}
	public String getUrl(){return link;}
	public int getRetweets(){return retweets;}
	public int getFavorites(){return favorites;}
	
	private Date createDate(String date){
		String format = "EEE MMM dd HH:mm:ss z yyyy";
		Date d=null;
		try {
			d=new SimpleDateFormat(format).parse(date);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}				
		return d;
	}
	
	public String getAge(){
		long now = Calendar.getInstance().getTime().getTime();
		long diff=(now-createDate(date).getTime())/1000;
		String finalDate="";
		
		if(diff<60) finalDate=diff+"s";
		else if(diff<3600) finalDate=diff/60+"m";
		else if(diff<216000) finalDate=diff/3600+"h";
		else finalDate=diff/3600/24+"d";
		
		return  finalDate;
	}
}
