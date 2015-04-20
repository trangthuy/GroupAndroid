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
import anh.android.seniorapp.model.Singer;

import com.example.appkara.R;

public class SingerAdapter extends BaseAdapter {

	private ArrayList<Singer> mListSong;
	private ArrayList<Singer> mListSearch;
	private Context mContext;
	private LayoutInflater mInflater;

	public SingerAdapter(Context context, ArrayList<Singer> mListSong) {
		this.mContext = context;
		this.mListSong = mListSong;
		this.mInflater = LayoutInflater.from(mContext);
		mListSearch = new ArrayList<Singer>();
		mListSearch.addAll(mListSong);
	}

	private class ViewHolder {
		ImageView imgSong;
		TextView tvSinger;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.singers_item, parent,
					false);
			holder = new ViewHolder();
			holder.imgSong = (ImageView) convertView.findViewById(R.id.imgView);
			holder.tvSinger = (TextView) convertView
					.findViewById(R.id.nameSinger);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Singer song = getItem(position);
		if (song != null) {
			holder.imgSong.setImageResource(R.drawable.ic_singer);
			holder.tvSinger.setText(song.getNameSinger());
		}

		return convertView;
	}

	@Override
	public int getCount() {
		return mListSong.size();
	}

	@Override
	public Singer getItem(int position) {
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
			for (Singer songNum : mListSearch) {

				if (searchNoneSymbol.convertString(
						searchNoneSymbol.stringStandard(songNum.getNameSinger().toLowerCase(
								Locale.getDefault()))).startsWith(charText)) {
					mListSong.add(songNum);
				} else if (searchNoneSymbol.convertString(
						songNum.getNameSinger().toString()).startsWith(charText)) {
					mListSong.add(songNum);
				}

			}
		}
		notifyDataSetChanged();
	}

}
