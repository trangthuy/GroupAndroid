package anh.android.seniorapp.adapter;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import anh.android.seniorapp.control.SearchNoneSymbol;
import anh.android.seniorapp.model.Song;

import com.example.appkara.R;

public class SongAdapter extends BaseAdapter {

	private ArrayList<Song> mListSong;
	private ArrayList<Song> mListSearch;
	private Context mContext;
	private LayoutInflater mInflater;

	public SongAdapter(Context context, ArrayList<Song> mListSong) {
		this.mContext = context;
		this.mListSong = mListSong;
		this.mInflater = LayoutInflater.from(mContext);
		mListSearch = new ArrayList<Song>();
		mListSearch.addAll(mListSong);
	}

	private class ViewHolder {
		ImageView imgSong;
		//ImageView imgChose;
		TextView tvSongName;
		TextView tvSongLyric;
		TextView tvSongSinger;
		TextView tvUsername;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.songs_item, parent, false);
			holder = new ViewHolder();
			holder.imgSong = (ImageView) convertView.findViewById(R.id.image);
			holder.tvSongName = (TextView) convertView.findViewById(R.id.name);
			holder.tvSongLyric = (TextView) convertView
					.findViewById(R.id.lyric);
			holder.tvSongSinger = (TextView) convertView
					.findViewById(R.id.name_singer);
			holder.tvUsername = (TextView) convertView
					.findViewById(R.id.username);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Song song = getItem(position);
		if (song != null) {
			holder.tvSongName.setText(song.getName());
			holder.tvSongLyric.setText(song.getLyric());
			holder.tvSongSinger.setText(song.getSinger());
			holder.tvUsername.setText(song.getUsername());

			if (song.isChose()==1) {
				holder.imgSong.setImageResource(R.drawable.ic_like_yes);
			} else if(song.isChose()==2){
				holder.imgSong.setImageResource(R.drawable.ic_like_no);
			} else if(song.isChose()==3){
				holder.imgSong.setImageResource(R.drawable.ic_star_book);
			}else if(song.isChose()==4){
				holder.imgSong.setImageResource(R.drawable.ic_star);
			}

		}

		return convertView;
	}

	@Override
	public int getCount() {
		return mListSong.size();
	}

	@Override
	public Song getItem(int position) {
		return mListSong.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		SearchNoneSymbol searchNoneSymbol = new SearchNoneSymbol();
		mListSong.clear();
		if (charText.length() == 0) {
			mListSong.addAll(mListSearch);
		} else {
			for (Song songNum : mListSearch) {

				if (searchNoneSymbol.convertString(
						searchNoneSymbol.stringStandard(songNum.getName().toLowerCase(
								Locale.getDefault()))).startsWith(charText)) {
					mListSong.add(songNum);
				} else if (searchNoneSymbol.convertString(
						songNum.getName().toString()).startsWith(charText)) {
					mListSong.add(songNum);
				}

			}
		}
		notifyDataSetChanged();
	}

}
