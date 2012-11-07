package org.aiti.buahtangan;
/**
 * Class BasketActivity
 * @version 1.0 Jan 07, 2011
 * @author Agus Haryanto (agus.superwriter@gmail.com)
 * @website http://agusharyanto.net
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

public class Map extends MapActivity{
	
    private MapView mapView;
	private LocationManager locManager;
	private LocationListener locListener;
	private SQLiteDatabase db;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.peta);
        
        initMap();
        initLoc();
    }

	private void initLoc() {
		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		 
		locListener = new LocationListener() {
			public void onLocationChanged(Location newLocation) {
			tampilkanPosisikeMap(newLocation);
			}
			public void onProviderDisabled(String arg0) {
			}
			
			public void onProviderEnabled(String arg0) {
			}
			
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			}
		};
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				locListener);
		locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, 
				locListener);
		
	}

	private void initMap() {
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.displayZoomControls(true);
		mapView.getController().setZoom(15);
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	
	private void tampilkanPosisikeMap(Location newLocation) {
		 List overlays = mapView.getOverlays();
		 
		 // first remove old overlay
		 if (overlays.size() > 0) {
			 for (Iterator iterator = overlays.iterator(); iterator.hasNext();) {
				 iterator.next();
				 iterator.remove();
			 }
		 }
		
		GeoPoint geopoint = new GeoPoint(
				(int) (newLocation.getLatitude() * 1E6), (int) (newLocation.
						getLongitude() * 1E6));
		
		// initialize icon
		Drawable icon = getResources().getDrawable(R.drawable.orang);
		icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon
				.getIntrinsicHeight());
		
		// create my overlay and show it
		MyItemizedOverlay overlay = new  MyItemizedOverlay(icon);
		OverlayItem item = new OverlayItem(geopoint, "Posisi saya", null);
		overlay.addItem(item);
		mapView.getOverlays().add(overlay);
		
		cariOverlay(mapView);
		
		// move to location
		mapView.getController().animateTo(geopoint);
		
		// redraw map
		mapView.postInvalidate();

	}
	
	private double[][] cariOverlay(MapView mapView2) {
		db = openOrCreateDatabase("coba", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		String ambil_data="SELECT toko.latitude, toko.longitude , toko.nama, toko.alamat FROM toko,oleholeh,padaToko,kota "
				+"WHERE padaToko._idToko = toko._id"+
		" AND oleholeh._id = padaToko._idBarang AND kota.nama=\""+ScreenUtama.kota+
				"\" AND kota._id= oleholeh.idKota";
		String hitung_data="SELECT COUNT(*) FROM toko,oleholeh,padaToko,kota "
				+"WHERE padaToko._idToko = toko._id"+
		" AND oleholeh._id = padaToko._idBarang AND kota.nama=\""+ScreenUtama.kota+
				"\" AND kota._id= oleholeh.idKota";
		
		Cursor jumlah_query = db.rawQuery(hitung_data, null);
		
		Cursor hasil_query = db.rawQuery(ambil_data, null);
		jumlah_query.moveToFirst();
		int besarArray = jumlah_query.getInt(0) - 1;
		
		hasil_query.moveToFirst();
		double isi[][] = new double[2][besarArray];
		int nilai = 0;
        
        if (hasil_query!=null) {
        	while(hasil_query.moveToNext() && hasil_query!=null) {
        		isi[0][nilai] = Double.parseDouble(hasil_query.getString(0));
        		isi[1][nilai] = Double.parseDouble(hasil_query.getString(1));
        		
        		GeoPoint geopoint = new GeoPoint(
        				(int) (isi[0][nilai] * 1E6), 
        				(int) (isi[1][nilai] * 1E6));
        		
        		Drawable icon = getResources().getDrawable(R.drawable.marker);
        		icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon
        				.getIntrinsicHeight());
        		
        		MyItemizedOverlay overlay = new  MyItemizedOverlay(icon);
        		OverlayItem item = new OverlayItem(geopoint, hasil_query.getString(2), hasil_query.getString(3));
        		overlay.addItem(item);
        		mapView2.getOverlays().add(overlay);
        		
        	}
        }
        jumlah_query.close();
        hasil_query.close();
		db.close();
		return isi;
	}
	
	
	class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {
		private ArrayList<OverlayItem> items;	
		private Drawable thing;
		
		public MyItemizedOverlay(Drawable icon) {
			super(icon);
			items = new ArrayList<OverlayItem>();
			thing = icon;
		}

		public void addItem(OverlayItem item) {
			items.add(item);
			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return (OverlayItem) items.get(i);
		}

		@Override
		public int size() {
			return items.size();
		}
		
		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, shadow);
			boundCenterBottom(thing);
		}
		
		@Override
		protected boolean onTap(int index) {
			GeoPoint gpoint = items.get(index).getPoint();
			double lat = gpoint.getLatitudeE6()/1e6;
	        double lon = gpoint.getLongitudeE6()/1e6;
	        String toast = "id: "+items.get(index).getTitle();
	        toast += 	"\nLat = "+lat+" Lon = "+lon+"";
	        if (!items.get(index).getTitle().equalsIgnoreCase("Posisi saya"))
	        	toast += 	"\nLokasi : "+items.get(index).getSnippet();
	        Toast.makeText(getBaseContext(), toast, Toast.LENGTH_LONG).show();
	        return(true);
		}
		
	}
}


