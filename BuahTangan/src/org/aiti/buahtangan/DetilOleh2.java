package org.aiti.buahtangan;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetilOleh2 extends Activity {
	String kota = "";
	String oleh2 = "";
	private String desc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub 
		super.onCreate(savedInstanceState);

		setContentView(R.layout.detiloleh2);

		kota = getIntent().getStringExtra("kota");
		oleh2 = getIntent().getStringExtra("oleh2");
		desc = getIntent().getStringExtra("desc_oleh2");
		
		AssetManager assetManager = getAssets();

        InputStream istr = null;
		try {
			istr = assetManager.open(getIntent().getStringExtra("url"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Bitmap bitmap = BitmapFactory.decodeStream(istr);

		
		((ImageView) findViewById(R.id.fotoOleh2)).setImageBitmap(bitmap);

		((TextView) findViewById(R.id.detilNamaKotaOleh2)).setText(kota + " > " + oleh2);
		((TextView) findViewById(R.id.descOleh2)).setText(desc);
		Log.i("test", kota+" > "+oleh2);
		
		((ImageButton) findViewById(R.id.shareFBOleh2)).setOnClickListener(new clickFalse());
		((ImageButton) findViewById(R.id.shareTwitterOleh2)).setOnClickListener(new clickFalse());
	}
	
	class clickFalse implements OnClickListener {

		public void onClick(View v) {
			Toast.makeText(getBaseContext(), "Belum Diimplementasikan", 
					Toast.LENGTH_SHORT).show();
			
		}
		
	}
}

