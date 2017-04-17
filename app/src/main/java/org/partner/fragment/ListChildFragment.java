package org.partner.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ekstep.genieservices.sdks.Partner;
import org.ekstep.genieservices.sdks.UserProfile;
import org.ekstep.genieservices.sdks.response.GenieListResponse;
import org.json.JSONArray;
import org.partner.BuildConfig;
import org.partner.R;
import org.partner.activity.LandingActivity;
import org.partner.adapter.ChildListAdapter;
import org.partner.callback.IUserProfile;
import org.partner.callback.LaunchFragmentCallback;
import org.partner.callback.OnItemClickListener;
import org.partner.callback.UserProfileResponseHandler;
import org.partner.database.PartnerDBHelper;
import org.partner.model.Child;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Fragment to display the Child from Genie
 */
public class ListChildFragment extends Fragment
        implements IUserProfile, SearchView.OnQueryTextListener {


    private static final String TAG = "ListChildFragment";

    private static final boolean DEBUG = BuildConfig.DEBUG;


    private static final String BUNDLE_SCHOOL_ID = "school_id";


    /**
     * To display Child from Genie as a List
     */
    private RecyclerView mChildListRecyclerView;


    private Partner mPartner;

    private UserProfile mUserProfile;


    /**
     * List to hold the Children
     */
    private List<Child> mChildList;


    /**
     * ID of the school
     */
    private String mSchoolId = null;


    /**
     * Callback for updating fragment
     */
    private LaunchFragmentCallback mCallback;

    /**
     * Adapter for Child list RecyclerView
     */
    private ChildListAdapter mChildAdapter;


    public ListChildFragment() {
        // Required empty public constructor
    }


    /**
     * Create a new instance of the fragment
     * @param schoolId id of the school
     * @return fragment instance
     */
    public static ListChildFragment newInstance(String schoolId) {
        ListChildFragment listChildFragment = new ListChildFragment();

        Bundle args = new Bundle();
        args.putString(BUNDLE_SCHOOL_ID, schoolId);

        listChildFragment.setArguments(args);

        return listChildFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (getActivity() instanceof LaunchFragmentCallback) {
            mCallback = (LaunchFragmentCallback) getActivity();
        }

        if (getArguments() != null) {
            mSchoolId = getArguments().getString(BUNDLE_SCHOOL_ID, null);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_child, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mChildListRecyclerView = (RecyclerView) view.findViewById(R.id.child_recycler_view);

        mPartner = new Partner(getActivity());
        mUserProfile = new UserProfile(getActivity());

        if (mSchoolId == null) {
            UserProfileResponseHandler responseHandler = new UserProfileResponseHandler(this);
            mUserProfile.getAllProfiles(responseHandler);
        } else {
            loadChildBySchool();
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_child_list, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mPartner != null) {
            mPartner.finish();
        }

        if (mUserProfile != null) {
            mUserProfile.finish();
        }
    }

    @Override
    public void onSuccessUserProfile(GenieListResponse genieListResponse) {
        if (DEBUG) {
            Log.i(TAG, "onSuccessUserProfile: " + genieListResponse.getResults());
        }

        JSONArray jsonArray = new JSONArray(genieListResponse.getResults());

        Type childType = new TypeToken<List<Child>>(){}.getType();
        mChildList = new Gson().fromJson(jsonArray.toString(), childType);

        initChildListRecycler();
    }

    @Override
    public void onFailureUserprofile(GenieListResponse genieListResponse) {
        if (DEBUG) {
            Log.i(TAG, "onFailureUserProfile: " + genieListResponse.getError());
        }

        Toast.makeText(getActivity(), genieListResponse.getError(),
                Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (mChildAdapter != null) {
            mChildAdapter.search(newText);
        }
        return false;
    }

    /**
     * Initialize the {@link RecyclerView} with the Child data
     */
    private void initChildListRecycler() {
        if (mChildList == null) {
            return;
        }

        mChildAdapter = new ChildListAdapter(mChildList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        mChildListRecyclerView.setLayoutManager(layoutManager);
        mChildListRecyclerView.setAdapter(mChildAdapter);

        mChildAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Child child = mChildList.get(position);

                if (mCallback != null) {
                    mCallback.switchFragment(LandingActivity.FRAGMENT_CHILD_DETAILS,
                            child.getUid());
                }
            }
        });
    }


    /**
     * Get all child related to a school id
     */
    private void loadChildBySchool() {
        mChildList = PartnerDBHelper.getAllChild(mSchoolId);
        initChildListRecycler();
    }


}
