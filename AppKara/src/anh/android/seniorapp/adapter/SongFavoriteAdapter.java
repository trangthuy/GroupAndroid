package anh.android.seniorapp.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.appkara.R;
import anh.android.seniorapp.model.SongFavorite;

public class SongFavoriteAdapter extends BaseAdapter{

	private ArrayList<SongFavorite> mListSong;
	private Context mContext;
	private LayoutInflater mInflater;
	
	public SongFavoriteAdapter(Context context, ArrayList<SongFavorite> mListSong){
		this.mContext = context;
		this.mListSong = mListSong;
		this.mInflater = LayoutInflater.from(mContext);
	}
	
	private class ViewHolder{
		TextView songNumIC;
		TextView songRating;
		TextView songName;
		TextView songSinger;
		TextView songLyric;
	}
	
	@Override
	public int getCount() {
		return mListSong.size();
	}

	@Override
	public SongFavorite getItem(int position) {
		return mListSong.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.rating_item, parent, false);
			holder = new ViewHolder();
			
			holder.songNumIC = (TextView) convertView.findViewById(R.id.tvSTT);
			holder.songRating = (TextView) convertView.findViewById(R.id.tvRating);
			holder.songName = (TextView) convertView.findViewById(R.id.tvNameSong);
			holder.songSinger = (TextView) convertView.findViewById(R.id.tvNameSinger);
			holder.songLyric = (TextView) convertView.findViewById(R.id.tvLyric);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		SongFavorite song = getItem(position);
		if(song!=null){
			holder.songNumIC.setText(String.valueOf(song.getSongNumIC())); 
			holder.songRating.setText(String.valueOf(song.getSongRating()));  
			holder.songName.setText(song.getSongName().toString());  
			holder.songSinger.setText(song.getSongSinger().toString());  
			holder.songLyric.setText(song.getSongLyric().toString());  
		}
		
		return convertView;
	}

}
