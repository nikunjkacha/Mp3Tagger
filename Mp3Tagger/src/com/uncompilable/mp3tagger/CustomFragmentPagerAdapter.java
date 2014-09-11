package com.uncompilable.mp3tagger;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CustomFragmentPagerAdapter extends FragmentPagerAdapter {
	protected static final int SELECTION_FRAGMENT = 0;
	protected static final int TAG_FRAGMENT = 1;

	private final FileSelectionFragment mSelectionFragment;
	private final TagEditFragment mTagFragment;


	public CustomFragmentPagerAdapter(FragmentManager fm) {
		super(fm);

		mSelectionFragment = new FileSelectionFragment();
		mTagFragment       = new TagEditFragment      ();
	}

	@Override
	public Fragment getItem(int index) {
		switch (index) {
		case SELECTION_FRAGMENT:
			return mSelectionFragment;
		case TAG_FRAGMENT:
			return mTagFragment;
		}
		return null;
	}

	@Override
	public int getCount() {
		return 2;
	}

}
