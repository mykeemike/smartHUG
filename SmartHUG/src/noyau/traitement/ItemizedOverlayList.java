/**
 * GREP - smartHUG
 * 
 * Modélise les couches superposés de la map
 *
 * @author GREP
 * @version Version 1.0
 * 
 */
package noyau.traitement;


import java.util.ArrayList;


import noyau.presentation.*;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class ItemizedOverlayList extends ItemizedOverlay {
	Context mContext;
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	public ItemizedOverlayList(Drawable defaultMarker) {
		  super(boundCenterBottom(defaultMarker));
	}
	
	public ItemizedOverlayList(Drawable defaultMarker, Context context) {
		  super(boundCenterBottom(defaultMarker));
		  mContext = context;
	}
	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}
	
	
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = mOverlays.get(index);
	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	  
	  Dialog dial = new Dialog(mContext);
	  dial.setContentView(R.layout.une_consultation);
	  dial.setTitle(item.getTitle());
	  dial.show();
	  return true;
	}
	
	@Override
	public int size() {
		return mOverlays.size();
	}

}
