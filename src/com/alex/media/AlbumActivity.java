package com.alex.media;

import java.io.File;

import com.alex.media.ArtistActivity.ListItemClickListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

public class AlbumActivity extends Activity {
	private int[] _ids;
	private String[]_titles;
	private String[] _path;									//�����ļ���·��
	private ListView listview;
	private int pos;
	private String albumName;
	private  MusicListAdapter adapter;
	/*�����Ĳ˵���*/
	private static final int PLAY_ITEM = Menu.FIRST;
	private static final int DELETE_ITEM = Menu.FIRST+1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = this.getIntent();
		albumName = intent.getExtras().getString("albums");
		listview = new ListView(this);
		setListData();
		listview.setOnItemClickListener(new ListItemClickListener());
		listview.setOnCreateContextMenuListener(new ContextMenuListener());
		LinearLayout list = new LinearLayout(this);
		list.setBackgroundResource(R.drawable.listbg);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
	    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	    list.addView(listview,params);
	    setContentView(list);
	}
	/*����ѡ�е�����*/
	private void playMusic(int position){
		Intent intent = new Intent(AlbumActivity.this,MusicActivity.class);
		intent.putExtra("_ids", _ids);
		intent.putExtra("_titles", _titles);
		intent.putExtra("position", position);
		startActivity(intent);
		finish();
	}
	
	/*���б���ɾ��ѡ�е�����*/
	private void deleteMusic(int position){
		this.getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, 
				MediaStore.Audio.Media._ID + "=" + _ids[position], 
				null);
	}
	
	/*��sdcard��ɾ��ѡ�е�����*/
	private void deleteMusicFile(int position){
		File file = new File(_path[pos]);
		file.delete();
	}
	 class ListItemClickListener implements OnItemClickListener{

	    	@Override
	    	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
	    		// TODO Auto-generated method stub
	    		playMusic(position);
	    	}
	    	
	 }
	 
	 /*���������Ĳ˵�������*/
		class ContextMenuListener implements OnCreateContextMenuListener{
			@Override
			public void onCreateContextMenu(ContextMenu menu, View view,
					ContextMenuInfo info) {
				menu.setHeaderTitle("����");
				menu.add(0, PLAY_ITEM, 0, "����");
				menu.add(0, DELETE_ITEM, 0, "ɾ��");
				final AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) info;
				pos = menuInfo.position;
			}
		}
		
		/*�����Ĳ˵���ĳһ����ʱ�ص��÷���*/
		@Override
		public boolean onContextItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case PLAY_ITEM:									//��ʼ����
				playMusic(pos);
				break;

			case DELETE_ITEM:								//ɾ��һ�׸���
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("���Ҫɾ�����׸�����")
				.setPositiveButton("��", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						deleteMusic(pos);					//���б���ɾ������
						deleteMusicFile(pos);				//��sdcard��ɾ������
						setListData();						//���»���б���ҩ��ʾ�����
						adapter.notifyDataSetChanged();		//�����б�UI
					}
				})
				.setNegativeButton("��", null);
				AlertDialog ad = builder.create();
				ad.show();
				break;
			}
			return true;
		}
	 
	 private void setListData(){
		 Cursor c = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
					new String[]{MediaStore.Audio.Media.TITLE,
					MediaStore.Audio.Media.DURATION,
					MediaStore.Audio.Media.ARTIST,
					MediaStore.Audio.Media._ID,
					MediaStore.Audio.Media.DISPLAY_NAME,
					MediaStore.Audio.Media.DATA}, 
					MediaStore.Audio.Media.ALBUM + "='" + albumName+"'", 
					null,
					null);
		   c.moveToFirst();
		   _ids = new int[c.getCount()];
		   _titles = new String[c.getCount()];
		   _path = new String[c.getCount()];
		   for(int i=0;i<c.getCount();i++){
		       _ids[i] = c.getInt(3);
		       _titles[i] = c.getString(0);
		       _path[i] = c.getString(5);
		       c.moveToNext();
		  }
		   adapter = new MusicListAdapter(this, c);
		   listview.setAdapter(adapter);
	 }
}
