package anh.android.seniorapp.model;

public class SongNum {

	private int songId;
	private String songTitle;
	private String songLyric;
	private String songAuthor;
	private boolean hasLyric = false;

	public boolean isHasLyric() {
		return hasLyric;
	}

	public void setHasLyric(boolean hasLyric) {
		this.hasLyric = hasLyric;
	}

	public int getSongId() {
		return songId;
	}

	public void setSongId(int songId) {
		this.songId = songId;
	}

	public String getSongTitle() {
		return songTitle;
	}

	public void setSongTitle(String songTitle) {
		this.songTitle = songTitle;
	}

	public String getSongLyric() {
		return songLyric;
	}

	public void setSongLyric(String songLyric) {
		this.songLyric = songLyric;
	}

	public String getSongAuthor() {
		return songAuthor;
	}

	public void setSongAuthor(String songAuthor) {
		this.songAuthor = songAuthor;
	}

}
