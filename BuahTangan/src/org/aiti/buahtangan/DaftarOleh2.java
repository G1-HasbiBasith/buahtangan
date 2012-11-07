package org.aiti.buahtangan;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DaftarOleh2 extends Activity {
	String[] daftarOleh2;
	String[] daftarDesc;
	String[] daftarGambar;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] mentah = createDatabaseLayout();
		pecah(mentah);
		setContentView(R.layout.oleh2);
		
		
		((TextView) findViewById(R.id.oleh2Kota)).setText(ScreenUtama.kota);

		ListView lv = (ListView) findViewById(R.id.listViewOleh2);
		lv.setAdapter(new ArrayAdapterOleh2(this,
				android.R.layout.simple_list_item_1));

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(view.getContext(), DetilOleh2.class);
				intent.putExtra("kota", ScreenUtama.kota);
				intent.putExtra("oleh2", daftarOleh2[position]);
				intent.putExtra("desc_oleh2", daftarDesc[position]);
				intent.putExtra("url", daftarGambar[position]);

				// Create the view using FirstGroup's LocalActivityManager
				View v = DaftarOleh2Group.group
						.getLocalActivityManager()
						.startActivity("show_city",
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView();

				// Again, replace the view
				DaftarOleh2Group.group.replaceView(v);

			}
		});
	}
	
	private class ArrayAdapterOleh2 extends ArrayAdapter<String>{
		
		public ArrayAdapterOleh2(Context context, int textViewResourceId){
			super(context, textViewResourceId, daftarOleh2);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.listitemoleh2, null);
			}
			
			AssetManager assetManager = getAssets();

	        InputStream istr = null;
			try {
				String gambar = daftarGambar[position];
				String thumbnail = ""+gambar.substring(0, gambar.length()-4)+"_t"+gambar.substring(gambar.length()-4, gambar.length());
				Log.i("thumbnail1",thumbnail);
				istr = assetManager.open(thumbnail);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        Bitmap bitmap = BitmapFactory.decodeStream(istr);
			((ImageView) v.findViewById(R.id.oleh2_t)).setImageBitmap(bitmap);
			
			((TextView) v.findViewById(R.id.namaOleh2)).setText(daftarOleh2[position]);
			((TextView) v.findViewById(R.id.penjelasanOleh2)).setText(daftarDesc[position].substring(0, 30)+"...");
			
			return v;
		}
	}
	
	private void pecah(String[] mentah) {
		String[] temp;
		daftarOleh2 = new String[mentah.length];
		daftarDesc= new String[mentah.length];
		daftarGambar = new String[mentah.length];
		for (int i = 0; i < mentah.length; i++) {
			temp = mentah[i].split("##");
			daftarOleh2[i] = temp[0];
			daftarDesc[i] = temp[1];
			daftarGambar[i] = temp[2];
		}
		
	}
	private String[] createDatabaseLayout() {
		SQLiteDatabase db = openOrCreateDatabase("coba", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		String ambil_data="SELECT oleholeh.nama,oleholeh.deskripsi,oleholeh.url FROM kota,oleholeh WHERE kota.nama=\""+ScreenUtama.kota+
				"\" AND kota._id = oleholeh.idKota";
		String hitung_data="SELECT COUNT(*) FROM kota,oleholeh WHERE kota.nama=\""+ScreenUtama.kota+
				"\" AND kota._id= oleholeh.idKota";
		Cursor jumlah_query = db.rawQuery(hitung_data, null);
		
		Cursor hasil_query = db.rawQuery(ambil_data, null);
		jumlah_query.moveToFirst();
		int besarArray = jumlah_query.getInt(0) - 1;
		
		hasil_query.moveToFirst();
		String isi[] = new String[besarArray];
		
        int nilai = 0;
        
        if (hasil_query!=null) {
        	while(hasil_query.moveToNext() && hasil_query!=null) {
        		String nama_oleh2 = hasil_query.getString(0);
        		String desc_oleh2 = hasil_query.getString(1);
        		
        		isi[nilai++] = nama_oleh2+"##"+desc_oleh2+"##"+
        				hasil_query.getString(2);
        	}
        }
        jumlah_query.close();
        hasil_query.close();
		db.close();
		return isi;
	}
}
