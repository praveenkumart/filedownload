package com.example.filedownload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	ProgressDialog pb;
	Bitmap bmp;
	ImageView im;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//pb=(ProgressDialog) findViewById(R.id.progressBar1);


	}

	public void startAsync(View v) {

		MyAsyncTask obj=new MyAsyncTask();
		obj.execute("http://bbhheartcentre.com/images/cardiodr.jpg");
	}

	class MyAsyncTask extends AsyncTask<String, Integer, String>
	{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pb=ProgressDialog.show(MainActivity.this,"Asyansample","Loading...");

			Toast.makeText(getApplicationContext(), "before execute", Toast.LENGTH_SHORT).show();
		}

		@Override
		protected String doInBackground(String... params) {
			InputStream is=null;
			try
			{
				URL ulrn=new URL(params[0]);
				HttpURLConnection con=(HttpURLConnection)ulrn.openConnection();
				is=con.getInputStream();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			bmp=BitmapFactory.decodeStream(is);
			fileDownload();
			return "hello";
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			int v=values[0];
			pb.setProgress(v);

		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			im=(ImageView)findViewById(R.id.img);
			im.setImageBitmap(bmp);
			super.onPostExecute(result);
			pb.dismiss();
			Toast.makeText(getApplicationContext(), "after execute", Toast.LENGTH_SHORT).show();

		}

	}

	public void fileDownload() {
		try {


			final File fs=new File(Environment.getExternalStorageDirectory(), "mm.jpg");
			if(!fs.exists())
			{
				fs.createNewFile();
			}
			//			FileReader in=new FileReader();
			FileWriter out=new FileWriter(fs);
			Bitmap bitmap = bmp;
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
			byte[] bitmapdata = bos.toByteArray();

			//write the bytes in file
			FileOutputStream fos = new FileOutputStream(fs);
			fos.write(bitmapdata);
			out.close();
			fos.close();
			//			in.close();
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), "file downloaded\n"+fs.getAbsolutePath(), Toast.LENGTH_LONG).show();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
