package com.uncompilable.mp3tagger;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CustomFragmentPagerAdapter extends FragmentPagerAdapter {
	protected static final int SELECTION_FRAGMENT = 0;
	protected static final int TAG_FRAGMENT = 1;

	private final FileSelectionFragment mSelectionFragment;
	private final TagEditFragment mTagFragment;


	public CustomFragmentPagerAdapter(final FragmentManager manager) {
		super(manager);

		this.mSelectionFragment = new FileSelectionFragment();
		this.mTagFragment       = new TagEditFragment      ();
	}

	@Override
	public Fragment getItem(final int index) {
		switch (index) {
		case SELECTION_FRAGMENT:
			return this.mSelectionFragment;
		case TAG_FRAGMENT:
			return this.mTagFragment;
		default:
			//do nothing on default
		}
		return null;
	}

	@Override
	public int getCount() {
		return 2;
	}

}
