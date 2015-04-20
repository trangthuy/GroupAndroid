package anh.android.seniorapp.model;

public class NavDrawerItem {
	
	private String title;
	private int icon;

	private boolean isCounterVisible = false;
	
	public NavDrawerItem(){}

	public NavDrawerItem(String title, int icon){
		this.title = title;
		this.icon = icon;
	}
	
	public NavDrawerItem(String title, int icon, boolean isCounterVisible, String count){
		this.title = title;
		this.icon = icon;
		this.isCounterVisible = isCounterVisible;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public int getIcon(){
		return this.icon;
	}
	
	public boolean getCounterVisibility(){
		return this.isCounterVisible;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setIcon(int icon){
		this.icon = icon;
	}

	public CharSequence getCount() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
