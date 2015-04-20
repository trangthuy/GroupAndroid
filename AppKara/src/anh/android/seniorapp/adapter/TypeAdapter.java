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
import anh.android.seniorapp.model.Type;

import com.example.appkara.R;

public class TypeAdapter extends BaseAdapter{

	private ArrayList<Type> mListSong;
	private ArrayList<Type> mListSearch;
	private Context mContext;
	private LayoutInflater mInflater;
	
	public TypeAdapter(Context context, ArrayList<Type> mListSong){
		this.mContext = context;
		this.mListSong = mListSong;
		this.mInflater = LayoutInflater.from(mContext);
		mListSearch = new ArrayList<Type>();
		mListSearch.addAll(mListSong);
	}
	
	private class ViewHolder{
		ImageView imgSong;
		TextView tvSinger;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.types_item, parent, false);
			holder = new ViewHolder();
			holder.imgSong = (ImageView) convertView.findViewById(R.id.imgView);
			holder.tvSinger = (TextView) convertView.findViewById(R.id.nameType);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		Type song = getItem(position);
		if(song!=null){
			holder.imgSong.setImageResource(R.drawable.ic_type);
			holder.tvSinger.setText(song.getNameType());
		}
		
		return convertView;
	}
	
	@Override
	public int getCount() {
		return mListSong.size();
	}

	@Override
	public Type getItem(int position) {
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
			for (Type songNum : mListSearch) {

				if (searchNoneSymbol.convertString(
						searchNoneSymbol.stringStandard(songNum.getNameType().toLowerCase(
								Locale.getDefault()))).startsWith(charText)) {
					mListSong.add(songNum);
				} else if (searchNoneSymbol.convertString(
						songNum.getNameType().toString()).startsWith(charText)) {
					mListSong.add(songNum);
				}

			}
		}
		notifyDataSetChanged();
	}
	
}
