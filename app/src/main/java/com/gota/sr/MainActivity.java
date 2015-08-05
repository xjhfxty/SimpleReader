package com.gota.sr;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.util.Calendar;

public class MainActivity extends Activity {

	ReadView readView;
	TextView pageview;
	BufferedReader reader;
	int num=320;
	CharBuffer buffer = CharBuffer.allocate(num);
	int position=0;
	String path;
	private int page=1;
	String rcode;
	Calendar c = Calendar.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		path="/storage/emulated/0/ebook/xue.txt";
		readView = (ReadView) findViewById(R.id.read_view);
		pageview = (TextView) findViewById(R.id.txt_page);
		pageview.setText("第"+page+"页"+"  "+c.get(Calendar.HOUR_OF_DAY)
				+":"+c.get(Calendar.MINUTE));
		try {
			onOptionsItemSelected();
		} catch (IOException e) {
			e.printStackTrace();
		}
		loadPage(0);
		menuthings();
	}

	private void menuthings() {
		Button menubutton;
		menubutton= (Button) findViewById(R.id.menu);
		menubutton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				openOptionsMenu();
			}
		});
	}


	private void onOptionsItemSelected() throws IOException {

		File file = new File(path);

		InputStream in=new java.io.FileInputStream(file);
		byte[] b = new byte[3];
		in.read(b);
		in.close();
		if (b[0] == -17 && b[1] == -69 && b[2] == -65) {
			loadBook("utf8");
			rcode="utf8";
		}
		else {
			loadBook("GBK");
			rcode="GBK";
		}
	}



	/**
	 * 将电子书都一部分到缓冲区
	 */
	private void loadBook(String code) {
		AssetManager assets = getAssets();
		try{
			FileInputStream fileInputStream=new FileInputStream(path);
			InputStreamReader in = new InputStreamReader(fileInputStream,code);
			reader = new BufferedReader(in);
			reader.read(buffer);

		}catch(IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * 从指定位置开始载入一页
	 */
	private void loadPage(int position) {
		buffer.position(position);
		readView.setText(buffer);
	}
	
	/**
	 * 上一页按钮
	 */
	public void previewPageBtn(View view) {

		if(position>0)
		{
			position-=readView.getCharNum();
			page-=1;
			if(position<0){
				position=0;
				page=1;

			}
			loadPage(position);
			readView.resize();
			if(page>1){
				num-=320;
				buffer = CharBuffer.allocate(num);
				loadBook(rcode);
			}
		}
		else {
			position = 0;
			page=1;
		}
		pageview.setText("第"+page+"页"+"  "+c.get(Calendar.HOUR_OF_DAY)
				+":"+c.get(Calendar.MINUTE));
		if(page==1)
			Toast.makeText(MainActivity.this, "已经第一页了", Toast.LENGTH_SHORT).show();


	}
	
	/**
	 * 下一页按钮
	 */
	public void nextPageBtn(View view) {
		num+=320;
		buffer = CharBuffer.allocate(num);
		loadBook(rcode);
		position += readView.getCharNum();
		page+=1;
		loadPage(position);
		readView.resize();
		pageview.setText("第"+page+"页"+"  "+c.get(Calendar.HOUR_OF_DAY)
				+":"+c.get(Calendar.MINUTE));
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){
			case R.id.menu_size:
				break;
			case R.id.menu_bg:
				break;
			case R.id.menu_1:
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

}
