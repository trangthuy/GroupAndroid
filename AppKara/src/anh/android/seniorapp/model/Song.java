package anh.android.seniorapp.model;

public class Song {

	private int id;
	private String name;
	private String lyric;
	private String singer;
	private String username;
	private int idSinger;
	private int idUserBooking;
	private int idOrder;
	private int idType;
	
	
	public int getIdSinger() {
		return idSinger;
	}

	public void setIdSinger(int idSinger) {
		this.idSinger= idSinger;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	private int isChose;

	public int isChose() {
		return isChose;
	}

	public void setChose(int isChose) {
		this.isChose = isChose;
	}

	public String getSinger() {
		return singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLyric() {
		return lyric;
	}

	public void setLyric(String lyric) {
		this.lyric = lyric;
	}

	public void setIdOrder(int idOrder) {
		this.idOrder = idOrder;
	}

	public int getIdUserBooking() {
		return idUserBooking;
	}

	public void setIdUserBooking(int idUserBooking) {
		this.idUserBooking = idUserBooking;
	}

	public int getIdOrder() {
		return idOrder;
	}

	public int getIdType() {
		return idType;
	}

	public void setIdType(int idType) {
		this.idType = idType;
	}

}
