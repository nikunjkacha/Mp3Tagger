package com.uncompilable.mp3tagger;

import com.uncompilable.mp3tagger.R;
import com.uncompilable.mp3tagger.controll.SelectionController;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;


/**
 * Main Activity and entry point to the Application
 * @author dennis
 *
 */
public class MainActivity extends ActionBarActivity implements TabListener {
	private ViewPager pager;
	private SelectionController mSelectionController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSelectionController = new SelectionController(PreferenceManager.getDefaultSharedPreferences(this));
		
		final ActionBar actionBar = this.getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		actionBar.addTab(actionBar.newTab().setText("File Selection").setTabListener(this), true);
		actionBar.addTab(actionBar.newTab().setText("Edit Tags").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("Album Covers").setTabListener(this));
		
		final CustomFragmentPagerAdapter pagerAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(pagerAdapter);
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int index) {
				actionBar.setSelectedNavigationItem(index);
				if (index == 0) {
					((FileSelectionFragment)pagerAdapter.getItem(0)).refresh();
				}
				else if (index == 1) {
					((TagEditFragment)pagerAdapter.getItem(1)).refresh();
				}
			}
		});
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
			getFragmentManager().beginTransaction()
            .replace(android.R.id.content, new PreferenceFragment() {
            	@Override
            	public void onCreate(Bundle savedInstanceState) {
            		super.onCreate(savedInstanceState);
            		addPreferencesFromResource(R.xml.preferences);
            		
            	}
            })
            .commit();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public SelectionController getSelectionController() {
		return this.mSelectionController;
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction transaction) {
		if (pager != null) pager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
	}
}
