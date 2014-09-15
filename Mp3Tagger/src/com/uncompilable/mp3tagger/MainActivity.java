package com.uncompilable.mp3tagger;

import java.util.Observable;
import java.util.Observer;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.uncompilable.mp3tagger.controll.SelectionController;


/**
 * Main Activity and entry point to the Application
 * @author dennis
 *
 */
public class MainActivity extends ActionBarActivity implements TabListener {
	private ViewPager mPager;

	private boolean mUpdateHeader;

	public static SelectionController sSelectionController;
	
	public MainActivity() {
		super();
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);

		sSelectionController = new SelectionController(PreferenceManager.getDefaultSharedPreferences(this));

		final ActionBar actionBar = this.getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		actionBar.addTab(actionBar.newTab().setText(this.getResources().getString(R.string.tab_selection_header)).setTabListener(this), true);
		actionBar.addTab(actionBar.newTab().setText(this.getResources().getString(R.string.tab_editing_header)).setTabListener(this));

		final CustomFragmentPagerAdapter pagerAdapter = new CustomFragmentPagerAdapter(this.getSupportFragmentManager());
		this.mPager = (ViewPager) this.findViewById(R.id.pager);
		this.mPager.setAdapter(pagerAdapter);
		this.mPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(final int index) {
				actionBar.setSelectedNavigationItem(index);
			}

			@Override
			public void onPageScrollStateChanged(final int arg0) {
				//do nothing
			}
			
			@Override
			public void onPageScrolled(final int arg0, final float arg1, final int arg2) {
				//do nothing
			}

		});

		//Adjust Selection header when selection changes
		this.setUpdateSelectionHeader(true);

		sSelectionController.getSelection().addObserver(new Observer() {

			@Override
			public void update(final Observable source, final Object data) {
				if (MainActivity.this.mUpdateHeader) {
					MainActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							actionBar.getTabAt(CustomFragmentPagerAdapter.SELECTION_FRAGMENT).setText(
									MainActivity.this.getResources().getString(R.string.tab_selection_header) + " (" +
											sSelectionController.getSelection().getFileSet().size() + ")");
						}
					});
				}
			}
		});
	}

	protected void setUpdateSelectionHeader(final boolean updateSelectionHeader) {
		this.mUpdateHeader = updateSelectionHeader;

		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				MainActivity.this.getActionBar().getTabAt(CustomFragmentPagerAdapter.SELECTION_FRAGMENT).setText(
						MainActivity.this.getResources().getString(R.string.tab_selection_header) + " (" +
								sSelectionController.getSelection().getFileSet().size() + ")");
			}
		});
	}

	protected void switchTab(final int tab) {
		if (tab == CustomFragmentPagerAdapter.SELECTION_FRAGMENT) {
			this.mPager.setCurrentItem(tab);
			this.getActionBar().setSelectedNavigationItem(tab);
		} else if (tab == CustomFragmentPagerAdapter.TAG_FRAGMENT) {
			this.mPager.setCurrentItem(tab);
			this.getActionBar().setSelectedNavigationItem(tab);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		this.getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		final int itemId = item.getItemId();
		if (itemId == R.id.action_settings) {
			final Intent settingsIntent = new Intent(this, SettingsActivity.class);
			this.startActivity(settingsIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabReselected(final Tab arg0, final FragmentTransaction arg1) {
		//do nothing
	}

	@Override
	public void onTabSelected(final Tab tab, final FragmentTransaction transaction) {
		if (this.mPager != null) {
			this.mPager.setCurrentItem(tab.getPosition());
		}
	}

	@Override
	public void onTabUnselected(final Tab arg0, final FragmentTransaction arg1) {
		//do nothing
	}
}
