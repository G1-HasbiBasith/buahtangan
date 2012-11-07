package org.aiti.buahtangan;

import java.io.IOException;
import java.io.InputStream;

import org.aiti.buahtangan.DetilOleh2.clickFalse;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetilWisata extends Activity{
	String kota="";
	String wisata="";
	String desc="";
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.detilwisata);
		
		kota = getIntent().getStringExtra("kota");
		wisata = getIntent().getStringExtra("wisata");
		desc = getIntent().getStringExtra("desc_wisata");
		
//		Drawable d = (Drawable) getResources().getDrawable(R.id.fotoWisata);
//		try {
//			d = Drawable.createFromStream(getAssets().open(getIntent().getStringExtra("url")), null);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		AssetManager assetManager = getAssets();

        InputStream istr = null;
		try {
			istr = assetManager.open(getIntent().getStringExtra("url"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Bitmap bitmap = BitmapFactory.decodeStream(istr);

		
		((ImageView) findViewById(R.id.fotoWisata)).setImageBitmap(bitmap);
		
		((TextView)findViewById(R.id.detilNamaKotaWisata)).setText(kota+" > "+wisata);
		Log.i("test", kota+" > "+wisata);
		((TextView)findViewById(R.id.descWisata)).setText(desc);
		
		
		((ImageButton)findViewById(R.id.shareFBWisata)).setOnClickListener(new clickFalse());
		((ImageButton) findViewById(R.id.shareTwitterWisata)).setOnClickListener(new clickFalse());
	}
	
	class clickFalse implements OnClickListener {

		public void onClick(View v) {
			Toast.makeText(getBaseContext(), "Belum Diimplementasikan", 
					Toast.LENGTH_SHORT).show();
			
		}
		
	}
}
