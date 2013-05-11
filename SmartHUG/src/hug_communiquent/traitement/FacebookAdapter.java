package hug_communiquent.traitement;

import java.util.ArrayList;

import noyau.presentation.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class FacebookAdapter extends BaseAdapter {
	/* 3 types de publication */
	private static final int VIEW_USUAL = 0, VIEW_CONNECTED = 1; 
	
	/* liste des publications */
	private ArrayList<Publication> data;
	private Context context;
	private int status;
	
	public FacebookAdapter(Context context, ArrayList<Publication> data, int status){
		this.context = context;
		this.data = data;	
		this.status = status; //0 = offline 1 = online
	}//Constructeur	
	
	public int getCount(){return data.size();}
	
	public Object getItem(int position){return data.get(position);}
	
	public long getItemId(int position){return position;}
	
	public int getViewTypeCount(){return 2;}	
	
	public int getItemViewType (int position) {
		return data.get(position).getType();
	} // getItemViewType
	
	/* Modélisation d'une ligne normale */
	private static class PublicationUsual extends LinearLayout {
	    TextView tvStatut, tvLikes, tvComments;

	    PublicationUsual (Context context, int layoutId) {
	      super(context);
	      LayoutInflater.from(context).inflate(layoutId, this);
	      tvStatut = (TextView)findViewById(R.id.tvStatut);
	      tvLikes = (TextView)findViewById(R.id.tvLikes);
	      tvComments = (TextView)findViewById(R.id.tvComments);	      
	    } //Constructeur
	} //publication
	
	private static class PublicationOnline extends PublicationUsual{
	    Button btnLike;
	    EditText etComment;

	    PublicationOnline (Context context, int layoutId) {
	      super(context, layoutId);
	      btnLike = (Button)findViewById(R.id.btnLike);
	      etComment = (EditText)findViewById(R.id.etComment);
	    } // Constructeur	    
	} // publicationImage
	
	  public View getView (int position, View convertView, ViewGroup parent) {
		  	if (convertView == null) {
		      switch (status) {
		        case VIEW_USUAL:   convertView = new PublicationUsual(context, R.layout.un_facebook_offline); break;
		        case VIEW_CONNECTED: convertView = new PublicationOnline(context, R.layout.un_facebook_online); break;
		      }
		    }
		    PublicationUsual lig = (PublicationUsual)convertView;
		    Publication p = data.get(position);
		    lig.tvStatut.setText(p.getShortStatut());
		    lig.tvStatut.setTag(position);
		    lig.tvStatut.setOnClickListener(new OnClickListener(){
		    	@Override
		    	public void onClick(View v) {
		    		//Lorsque l'on clique sur le nom, on récupère la position de la "Personne"
		    		Integer position = (Integer)v.getTag();
		    		Publication p = data.get(position);
		    		TextView tv = (TextView)v;
		    		if (tv.getText().equals(p.getShortStatut())){
		    			tv.setText(p.getStatut());
		    		} else {
		    			tv.setText(p.getShortStatut());
		    		}	    		
		    				
		    		//On prévient les listeners qu'il y a eu un clic sur le TextView "TV_Nom".
		    		//sendListener(data.get(position), position);
		    	}	    			
	  		});
		    
		    
		    lig.tvLikes.setText(p.getLikes()+" j'aime");
		    lig.tvComments.setText(p.getComments()+" commentaires");
		    
		    if (status == VIEW_CONNECTED) {
		    	((PublicationOnline)lig).btnLike.setOnClickListener(new OnClickListener(){
		    			@Override
				    	public void onClick(View v) {
		    				System.out.println("LIKE");		    			
		    			}
		    	});		    	
		    }
		    return convertView;
	  } // getView
}