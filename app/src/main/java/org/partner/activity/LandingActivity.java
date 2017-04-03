package org.partner.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.partner.R;
import org.partner.callback.LaunchFragmentCallback;
import org.partner.fragment.ChildDetailFragment;
import org.partner.fragment.ListChildFragment;

public class LandingActivity extends AppCompatActivity
        implements LaunchFragmentCallback {


    public static final int FRAGMENT_LIST_CHILD = 1;
    public static final int FRAGMENT_CHILD_DETAILS = 2;



    private FloatingActionButton mAddChildFab;


    /**
     * Hold the ID of the current Fragment
     */
    private int mCurrentFragmentId;

    /**
     * Hold the Uid of the selected child
     */
    private String mUid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAddChildFab = (FloatingActionButton) findViewById(R.id.add_child_fab);

        if (savedInstanceState == null) {
            mCurrentFragmentId = FRAGMENT_LIST_CHILD;
            displayFragment();
        }
    }

    @Override
    public void switchFragment(int fragmentId, @Nullable String uid) {
        mCurrentFragmentId = fragmentId;
        mUid = uid;

        displayFragment();
    }

    private void displayFragment() {
        Fragment fragment = null;

        switch (mCurrentFragmentId) {
            case FRAGMENT_LIST_CHILD:
                fragment = new ListChildFragment();
                mAddChildFab.show();
                break;


            case FRAGMENT_CHILD_DETAILS:
                mAddChildFab.hide();
                fragment = ChildDetailFragment.newInstance(mUid);
                break;
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
    }
}
