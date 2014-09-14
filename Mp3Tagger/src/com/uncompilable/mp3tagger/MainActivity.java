package com.uncompilable.mp3tagger;

import java.util.Observable;
import java.util.Observer;

import com.uncompilable.mp3tagger.R;
import com.uncompilable.mp3tagger.controll.SelectionController;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;


/**
 * Main Activity and entry point to the Application
 * @author dennis
 *
 */
public class MainActivity extends ActionBarActivity implements TabListener {
	private ViewPager mPager;
	private CustomFragmentPagerAdapter mPagerAdapter;

	private boolean mUpdateHeader;

	public static SelectionController sSelectionController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sSelectionController = new SelectionController(PreferenceManager.getDefaultSharedPreferences(this));

		final ActionBar actionBar = this.getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		actionBar.addTab(actionBar.newTab().setText(getResources().getString(R.string.tab_selection_header)).setTabListener(this), true);
		actionBar.addTab(actionBar.newTab().setText(getResources().getString(R.string.tab_editing_header)).setTabListener(this));

		mPagerAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mPagerAdapter);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int index) {
				actionBar.setSelectedNavigationItem(index);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}

		});

		//Adjust Selection header when selection changes
		this.setUpdateSelectionHeader(true);

		sSelectionController.getSelection().addObserver(new Observer() {

			@Override
			public void update(Observable source, Object data) {
				if (mUpdateHeader) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							actionBar.getTabAt(CustomFragmentPagerAdapter.SELECTION_FRAGMENT).setText(
									getResources().getString(R.string.tab_selection_header) + " (" +
											sSelectionController.getSelection().getFileSet().size() + ")");
						}
					});
				}
			}
		});
	}

	protected void setUpdateSelectionHeader(boolean updateSelectionHeader) {
		this.mUpdateHeader = updateSelectionHeader;
		
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				getActionBar().getTabAt(CustomFragmentPagerAdapter.SELECTION_FRAGMENT).setText(
						getResources().getString(R.string.tab_selection_header) + " (" +
								sSelectionController.getSelection().getFileSet().size() + ")");
			}
		});
	}

	protected void switchTab(int tab) {
		if (tab == CustomFragmentPagerAdapter.SELECTION_FRAGMENT) {
			mPager.setCurrentItem(tab);
			this.getActionBar().setSelectedNavigationItem(tab);
		} else if (tab == CustomFragmentPagerAdapter.TAG_FRAGMENT) {
			mPager.setCurrentItem(tab);
			this.getActionBar().setSelectedNavigationItem(tab);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent settingsIntent = new Intent(this, SettingsActivity.class);
			startActivity(settingsIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction transaction) {
		if (mPager != null) mPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
	}
}
