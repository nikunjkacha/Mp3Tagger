package com.uncompilable.mp3tagger;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

public class AlbumCoverActivity extends FragmentActivity {
	private AlbumGridAdapter mAdapter;
	private Fragment mCoverFragment;
	private Fragment mErrorFragment;
	private CoverFetchTask mFetchTask;
	private int mVisibleFragment;

	protected static final int COVER_FRAGMENT = 1;
	protected static final int ERROR_FRAGMENT = 2;

	public AlbumCoverActivity() {
		super();
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_album_cover);

		this.mAdapter = new AlbumGridAdapter(this);

		this.mFetchTask = new CoverFetchTask(this);
		this.mCoverFragment = new AlbumCoverFragment(this.mAdapter);

		this.mErrorFragment = new AlbumCoverErrorFragment();

		final FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
		transaction.add(R.id.container, this.mCoverFragment);
		transaction.commit();
		this.mVisibleFragment = COVER_FRAGMENT;

		this.mFetchTask.execute("");

		this.getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	protected void setVisibleFragment(final int fragment) {
		if (fragment == COVER_FRAGMENT &&
				this.mVisibleFragment != COVER_FRAGMENT) {


			final FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
			transaction.remove(this.mErrorFragment);
			transaction.replace(R.id.container, this.mCoverFragment);
			transaction.commit();

			this.mVisibleFragment = fragment;
		} else if (fragment == ERROR_FRAGMENT &&
				this.mVisibleFragment != ERROR_FRAGMENT) {

			final FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
			transaction.remove(this.mCoverFragment);
			transaction.replace(R.id.container, this.mErrorFragment);
			transaction.commit();

			this.mVisibleFragment = fragment;
		}

	}

	protected void runFetchTask() {
		this.mFetchTask.cancel(true);
		this.mFetchTask = new CoverFetchTask(this);
		this.mFetchTask.execute("");

		this.setVisibleFragment(COVER_FRAGMENT);
	}

	protected AlbumGridAdapter getAdapter() {
		return this.mAdapter;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
