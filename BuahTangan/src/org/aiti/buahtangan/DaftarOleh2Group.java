package org.aiti.buahtangan;

import java.util.ArrayList;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DaftarOleh2Group extends ActivityGroup {
	// Keep this in a static variable to make it accessible for all the nesten activities,
	 //lets them manipulate the view
		public static DaftarOleh2Group group;
	 
	        // Need to keep track of the history if you want the back-button to
	// work properly, don't use this if your activities requires a lot of memory.
		private ArrayList<View> history;
	 
		@Override
		protected void onCreate(Bundle savedInstanceState) {
		      super.onCreate(savedInstanceState);
		      this.history = new ArrayList<View>();
		      group = this;
	 
	              // Start the root activity withing the group and get its view
		      View view = getLocalActivityManager().startActivity("DaftarOleh2", new
		    	      							Intent(this,DaftarOleh2.class)
		    	      							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
		    	                                .getDecorView();
	 
	              // Replace the view of this ActivityGroup
		      replaceView(view);
	 
		   }
	 
		public void replaceView(View v) {
	                // Adds the old one to history
			history.add(v);
	                // Changes this Groups View to the new View.
			setContentView(v);
		}
	 
		public void back() {
			if(history.size() > 0) {
				history.remove(history.size()-1);
				setContentView(history.get(history.size()-1));
			}else {
				finish();
			}
		}
	 
	   @Override
	    public void onBackPressed() {
	    	DaftarOleh2Group.group.back();
	        return;
	    }

}
