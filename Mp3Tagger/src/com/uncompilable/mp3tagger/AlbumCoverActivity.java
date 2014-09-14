package com.uncompilable.mp3tagger;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class AlbumCoverActivity extends FragmentActivity {
	private AlbumGridAdapter mAdapter;
	private Fragment mCoverFragment;
	private Fragment mErrorFragment;
	private CoverFetchTask mFetchTask;
	private int mVisibleFragment;

	protected static final int COVER_FRAGMENT = 1;
	protected static final int ERROR_FRAGMENT = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album_cover);

		mAdapter = new AlbumGridAdapter(this);

		mFetchTask = new CoverFetchTask(this);
		mCoverFragment = new AlbumCoverFragment(mAdapter);

		mErrorFragment = new AlbumCoverErrorFragment();

		FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
		transaction.add(R.id.container, mCoverFragment);
		transaction.commit();
		mVisibleFragment = COVER_FRAGMENT;
		
		mFetchTask.execute("");
	}

	protected void setVisibleFragment(int fragment) {
		if (fragment == COVER_FRAGMENT &&
				mVisibleFragment != COVER_FRAGMENT) {


			FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
			transaction.remove(mErrorFragment);
			transaction.replace(R.id.container, mCoverFragment);
			transaction.commit();

			mVisibleFragment = fragment;
		} else if (fragment == ERROR_FRAGMENT &&
				mVisibleFragment != ERROR_FRAGMENT) {

			FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
			transaction.remove(mCoverFragment);
			transaction.replace(R.id.container, mErrorFragment);
			transaction.commit();

			mVisibleFragment = fragment;
		}

	}

	protected void runFetchTask() {
		this.mFetchTask.cancel(true);
		mFetchTask = new CoverFetchTask(this);
		mFetchTask.execute("");
		
		setVisibleFragment(COVER_FRAGMENT);
	}

	protected AlbumGridAdapter getAdapter() {
		return mAdapter;
	}
}
