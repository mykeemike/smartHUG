package hug_communiquent.traitement;

import java.util.ArrayList;

import noyau.presentation.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class YoutubeAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater inflater;
	
	private static class ViewHolder {
		TextView tvIdVideo, tvTitreVideo, tvDescVideo, tvNbViewVideo, tvTempsVideo, tvDateVideo;
		ImageView ivMetaPic;
	} // ViewHolder
	
	ArrayList<Video> data = new ArrayList<Video>();
	public YoutubeAdapter(Context context, ArrayList<Video> data) {
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
			convertView = inflater.inflate(R.layout.une_video, null);
			vH = new ViewHolder();
			vH.tvIdVideo = (TextView)convertView.findViewById(R.id.tvIdVideo);
			vH.tvTitreVideo = (TextView)convertView.findViewById(R.id.tvTitreVideo);
			vH.tvDescVideo = (TextView)convertView.findViewById(R.id.tvDescVideo);
			vH.tvNbViewVideo = (TextView)convertView.findViewById(R.id.tvNbViewVideo);
			vH.tvTempsVideo = (TextView)convertView.findViewById(R.id.tvTempsVideo);
			vH.tvDateVideo = (TextView)convertView.findViewById(R.id.tvDateVideo);
			vH.ivMetaPic = (ImageView)convertView.findViewById(R.id.ivMetaPic);
			convertView.setTag(vH);
		} else {
			vH = (ViewHolder)convertView.getTag();
		}

		Video v = (Video) data.get(position);
		vH.tvTitreVideo.setText(v.getTitre());
		vH.tvTempsVideo.setText(v.getTemps());
		vH.tvNbViewVideo.setText(v.getnbViews() + " " + convertView.getResources().getString(R.string.stVue));
		vH.tvDateVideo.setText(v.getDate());
		vH.tvDescVideo.setText(v.getDescription());
		vH.tvIdVideo.setText(v.getId());
		vH.ivMetaPic.setImageBitmap(v.getImageBitmap());
		return convertView;
	}

}