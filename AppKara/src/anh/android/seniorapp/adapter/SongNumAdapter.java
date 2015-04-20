package anh.android.seniorapp.adapter;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import anh.android.seniorapp.control.SearchNoneSymbol;
import anh.android.seniorapp.model.SongNum;

import com.example.appkara.R;

public class SongNumAdapter extends BaseAdapter {

	private ArrayList<SongNum> mListSong;
	private ArrayList<SongNum> mListSearch;
	private Context mContext;
	private LayoutInflater mInflater;

	public SongNumAdapter(Context context, ArrayList<SongNum> mListSong) {
		this.mContext = context;
		this.mListSong = mListSong;
		this.mInflater = LayoutInflater.from(mContext);
		this.mListSearch = new ArrayList<SongNum>();
		this.mListSearch.addAll(mListSong);
	}

	private class ViewHolder {
//		ImageView songImage;
		TextView tvSongCode;
		TextView tvSongName;
		TextView tvSongLyric;
		TextView tvSongAuthor;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			
			convertView = mInflater.inflate(R.layout.song_num_item, parent, false);
			holder = new ViewHolder();
			
//			holder.songImage = (ImageView) convertView.findViewById(R.id.tvSongImg);
			holder.tvSongCode = (TextView) convertView.findViewById(R.id.tvSongId);
			holder.tvSongName = (TextView) convertView.findViewById(R.id.tvSongName);
			holder.tvSongLyric = (TextView) convertView.findViewById(R.id.tvSongLyric);
			holder.tvSongAuthor = (TextView) convertView.findViewById(R.id.tvSongAuthor);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		SongNum song = getItem(position);
		if (song != null) {
			
//			holder.songImage.setImageResource(R.drawable.ic_song);
			holder.tvSongCode.setText(String.valueOf(song.getSongId()));
			holder.tvSongName.setText(song.getSongTitle().toString());
			holder.tvSongAuthor.setText(song.getSongAuthor().toString());
			
			if(song.isHasLyric()){
				holder.tvSongLyric.setText(song.getSongLyric().toString());
			}else{
				holder.tvSongLyric.setVisibility(View.GONE);
			}
			
		}

		return convertView;
	}

	@Override
	public int getCount() {
		return mListSong.size();
	}

	@Override
	public SongNum getItem(int position) {
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
			for (SongNum songNum : mListSearch) {

				if (searchNoneSymbol.convertString(
						searchNoneSymbol.stringStandard(songNum.getSongTitle().toLowerCase(
								Locale.getDefault()))).startsWith(charText)) {
					mListSong.add(songNum);
				} else if (searchNoneSymbol.convertString(
						songNum.getSongTitle().toString()).startsWith(charText)) {
					mListSong.add(songNum);
				}

			}
		}
		notifyDataSetChanged();
	}
	
}
