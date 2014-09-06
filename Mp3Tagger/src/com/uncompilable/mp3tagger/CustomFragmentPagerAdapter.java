package com.uncompilable.mp3tagger;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CustomFragmentPagerAdapter extends FragmentPagerAdapter {
	private static final int SELECTION_FRAGMENT = 0;
	private static final int TAG_FRAGMENT = 1;
	private static final int COVER_FRAGMENT = 2;
	

	public CustomFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {
		switch (index) {
		case SELECTION_FRAGMENT:
			return new FileSelectionFragment();
		case TAG_FRAGMENT:
			return new TagEditFragment();
		case COVER_FRAGMENT:
			return new AlbumCoverFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		return 3;
	}

}
