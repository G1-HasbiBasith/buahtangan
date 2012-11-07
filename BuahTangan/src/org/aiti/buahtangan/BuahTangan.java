package org.aiti.buahtangan;
/**
 * Class TabSportActivity
 * @version 1.0 Jan 07, 2011
 * @author Agus Haryanto (agus.superwriter@gmail.com)
 * @website http://agusharyanto.net
 */


import java.io.IOException;

import org.aiti.buahtangan.gps.Geocoder;
import org.aiti.buahtangan.helper.DataBaseHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class BuahTangan extends Activity {

     ProgressDialog loading;
     int loadingStatus = 0;
     Handler loadingHandler = new Handler();
     
     //added by RD
     private LocationManager lm;
     private LocationListener locationListener;
     private Location currLoc;
     private boolean foundGPS = false;
     private ProgressDialog dialog;
     private boolean foundKota = false;
	public String hasil;
     


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        makeDB();
        dialog = new ProgressDialog(BuahTangan.this);
        	
        // Process button to start spinner progress dialog with anonymous inner class
        Button cariKota = (Button) findViewById(R.id.cariKota);
        cariKota.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
            	dialog.setMessage("mencari GPS...");
            	dialog.show();
//            	Toast.makeText(getBaseContext(), "halo", Toast.LENGTH_SHORT).show();
            	lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                 
                 
                locationListener = new MyLocationListener();
                 
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
//            	gps();
                
            	// prepare for a progress bar dialog
//    			loading = new ProgressDialog(v.getContext());
//    			loading.setMessage("Mendeteksi Kota Anda");
//    			loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//    			loading.setProgress(0);
//    			loading.setMax(100);
//    			loading.show();
//    			
//    			loadingStatus=0;
//
//    			
//    			new Thread(new Runnable() {
//    				  public void run() {
//    					while (loadingStatus < 100) {
//    	 
//    					  // process some tasks
//    					  loadingStatus = gps();
//    					  
//    					  try {
//    						Thread.sleep(1);
//    					  } catch (InterruptedException e) {
//    						e.printStackTrace();
//    					  }
//    	 
//    					  // Update the progress bar
//    					  loadingHandler.post(new Runnable() {
//    						public void run() {
//    						  loading.setProgress(loadingStatus);
//    						}
//    					  });
//    					}
//    	 
//    					// ok, file is downloaded,
//    					if (loadingStatus >= 100) {
//    	 
//    						// sleep 2 seconds, so that you can see the 100%
//    						try {
//    							Thread.sleep(2000);
//    						} catch (InterruptedException e) {
//    							e.printStackTrace();
//    						}
//    	 
//    						// close the progress bar dialog
//    						loading.dismiss();
//    						
//    		                Intent i = new Intent(BuahTangan.this, ScreenUtama.class);
//    		                startActivity(i);
//    					}
//    				  }
//    			}).start();
            	
            	
            }
        });
       
    }

    private void makeDB() {
		DataBaseHelper myDbHelper = new DataBaseHelper(getBaseContext());
		try { 
        	myDbHelper.createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}
		try {
			myDbHelper.openDataBase();
		}catch(SQLException sqle){
			throw sqle;
		}
	}

    public double getLat() {
    	return currLoc.getLatitude();
    }
    public double getLong() {
    	return currLoc.getLongitude();
    }
    
    private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location loc) {
			currLoc = loc;
    		if (loc != null) {
    			Log.i("latitude", Double.toString(loc.getLatitude()));
    			Log.i("longitude", Double.toString(loc.getLongitude()));
    			foundGPS = true;
    			if(dialog.isShowing()) {
            		dialog.dismiss();
            	}
    			Toast.makeText(getBaseContext(),"mendapatkan GPS!",Toast.LENGTH_LONG).show();
    			lm.removeUpdates(locationListener);
    			
    			ReverseGeocodeLookupTask task = new ReverseGeocodeLookupTask();
                task.applicationContext = BuahTangan.this;
                task.execute();
    		}
			
		}

		public void onProviderDisabled(String provider) {
			
			
		}

		public void onProviderEnabled(String provider) {
			
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {			
		}
    	
    }
    
    public class ReverseGeocodeLookupTask extends AsyncTask <Context, Void, String> {
        protected Context applicationContext;
       
        @Override
        protected void onPreExecute() {
        	dialog = new ProgressDialog(BuahTangan.this);
            dialog.setMessage("mencari kota..");
            dialog.show();
        }
       
        @Override
        protected String doInBackground(Context... params) {
            String localityName = "nyanyanyanya";
           
            if (currLoc != null) {
                localityName = Geocoder.reverseGeocode(currLoc);
            }
           
            Log.i("kota",localityName);
            foundKota = true;
            return localityName;
        }
       
        @Override
        protected void onPostExecute(String result) {
        	if(dialog.isShowing()) {
        		dialog.dismiss();
        	}
        	hasil = result;
//            Intent x = new Intent(BuahTangan.this, Peta.class);
//            x.putExtra("local", result);
//            x.putExtra("lat", currLoc.getLatitude());
//            x.putExtra("longi", currLoc.getLongitude());
//            startActivity(x);
        	Intent i = new Intent(BuahTangan.this, ScreenUtama.class);
        	i.putExtra("kota", hasil);
            startActivity(i);
            Toast.makeText(getBaseContext(), "local : "+result, Toast.LENGTH_SHORT).show();
        }
    }

//    public class LocateControl extends AsyncTask<Context, Void, Void> {
//    	private final ProgressDialog dialog = new ProgressDialog(BuahTangan.this);
//
//    	@Override
//    	protected void onPreExecute() {
//    		this.dialog.setMessage("mencari GPS...");
//    		this.dialog.show();
//    	}
//    	
//		protected Void doInBackground(Context... params) {
//			
//			return null;
//		}
//		
//		@Override
//		protected void onPostExecute(Void result) {
//			if(this.dialog.isShowing()) {
//                this.dialog.dismiss();
//            }
//			
//
//		}
//    	
//    }
}