package anh.android.seniorapp.model;

public class SongFavorite {

	private int songNumIC;
	private int songRating;
	private int songId;
	private String songName;
	private String songSinger;
	private String songLyric;
	
	public int getSongId() {
		return songId;
	}

	public void setSongId(int songId) {
		this.songId = songId;
	}

	public int getSongNumIC() {
		return songNumIC;
	}

	public void setSongNumIC(int songNumIC) {
		this.songNumIC = songNumIC;
	}

	public int getSongRating() {
		return songRating;
	}

	public void setSongRating(int songRating) {
		this.songRating = songRating;
	}

	public String getSongName() {
		return songName;
	}

	public void setSongName(String songName) {
		this.songName = songName;
	}

	public String getSongSinger() {
		return songSinger;
	}

	public void setSongSinger(String songSinger) {
		this.songSinger = songSinger;
	}

	public String getSongLyric() {
		return songLyric;
	}

	public void setSongLyric(String songLyric) {
		this.songLyric = songLyric;
	}

}
