package org.aiti.buahtangan;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;

public class ScreenUtama extends TabActivity{
	public static String kota;
	public static TabWidget tabWisata;
	private String[] localityName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screenutama);
		Log.i("masukan", getIntent().getStringExtra("kota"));
		localityName = getIntent().getStringExtra("kota").split(",");
		try {
		kota = localityName[1];
		}catch(ArrayIndexOutOfBoundsException ar) {
			kota = localityName[0]; 
		}
//		kota = getIntent().getStringExtra("kota");

        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, DaftarWisataGroup.class);

        
        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("wisata").setIndicator("Wisata",
                          res.getDrawable(R.drawable.icon_tab_wisata))
                      .setContent(intent);
        tabHost.addTab(spec);

     // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, DaftarOleh2Group.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("oleh-oleh").setIndicator("Oleh-Oleh",
                          res.getDrawable(R.drawable.icon_tab_belanja))
                      .setContent(intent);
        tabHost.addTab(spec);
        
     // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, Map.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("map").setIndicator("Map",
                          res.getDrawable(R.drawable.icon_tab_map))
                      .setContent(intent);
        tabHost.addTab(spec);
	}
	
	public void replaceContentView(String id, Intent newIntent) {
			View view = getLocalActivityManager().startActivity(id,newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)) .getDecorView();
			this.setContentView(view);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		default : 
			Toast.makeText(getBaseContext(), "Belum Diimplementasikan",
					Toast.LENGTH_SHORT).show();
			
		}
		return true;
	}
}
	
