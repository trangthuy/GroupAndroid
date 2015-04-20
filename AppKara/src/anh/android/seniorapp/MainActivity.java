package anh.android.seniorapp;

import java.util.ArrayList;

import com.example.appkara.R;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import anh.android.seniorapp.adapter.NavDrawerListAdapter;
import anh.android.seniorapp.control.SQLiteHandler;
import anh.android.seniorapp.control.SessionManager;
import anh.android.seniorapp.fragment.BookListFragment;

import anh.android.seniorapp.fragment.FavoriteFragment;
import anh.android.seniorapp.fragment.ProfileFragment;
import anh.android.seniorapp.fragment.SearchFragment;
import anh.android.seniorapp.fragment.SongNumFragment;
import anh.android.seniorapp.fragment.TopFavoriteFragment;
import anh.android.seniorapp.model.NavDrawerItem;

public class MainActivity extends ActionBarActivity implements
		OnItemClickListener {

	public static final String TAG = MainActivity.class.getSimpleName();

	private Toolbar toolbar;

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ListView mDrawerList;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	private SQLiteHandler db;
	private SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		toolbar = (Toolbar) findViewById(R.id.app_bar);
		setSupportActionBar(toolbar);

		db = new SQLiteHandler(getApplicationContext());
		session = new SessionManager(getApplicationContext());

		if (!session.isLoggedIn()) {
			logoutUser();
		}

		mTitle = mDrawerTitle = getTitle();

		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		navDrawerItems = new ArrayList<NavDrawerItem>();

		for (int i = 0; i < navMenuTitles.length; i++) {
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons
					.getResourceId(i, -1)));
		}

		navMenuIcons.recycle();

		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);
		mDrawerList.setOnItemClickListener(this);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
				R.string.drawer_open, R.string.drawer_close) {

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getSupportActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				//getSupportActionBar().hide();
				getSupportActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		if (savedInstanceState == null) {
			displayView(0);
		}
		mDrawerLayout.post(new Runnable() {

			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		displayView(position);
	}

	private void displayView(int position) {

		Fragment fragment = null;

		switch (position) {
		case 0:
			fragment = new SearchFragment();
			break;
		case 1:
			fragment = new BookListFragment();
			break;
		case 2:
			fragment = new FavoriteFragment();
			break;
		case 3:
			fragment = new ProfileFragment();
			break;
		case 4:
			fragment = new TopFavoriteFragment();
			
			break;
		case 5:
			fragment = new SongNumFragment();
			break;
		case 6:
			drawerStatus(position);
			alertSelection();
			break;
		}

		if (fragment != null) {

			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.frameLayout, fragment).commit();
			drawerStatus(position);
		} else {
			Log.d(TAG, "Logout user click!");
		}
	}

	private void drawerStatus(int position){
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
		setTitle(navMenuTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}
	
	private void logoutUser() {
		session.setLogin(false);
		db.deleteUsers();
		Intent in = new Intent(MainActivity.this, LoginActivity.class);
		startActivity(in);
		finish();
	}

	private void alertSelection() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Bạn muốn...?");
		builder.setPositiveButton("Hủy",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		builder.setNeutralButton("Đăng xuất",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						logoutUser();
						System.exit(0);
					}
				});
		builder.setNegativeButton("Thoát",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						finish();
						System.exit(0);
					}
				});
	
		builder.show();
	}
}
