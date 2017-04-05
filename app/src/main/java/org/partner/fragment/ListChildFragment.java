package org.partner.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
import org.partner.model.Child;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Fragment to display the Child from Genie
 */
public class ListChildFragment extends Fragment
        implements IUserProfile {


    private static final String TAG = "ListChildFragment";

    private static final boolean DEBUG = BuildConfig.DEBUG;


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
     * Callback for updating fragment
     */
    private LaunchFragmentCallback mCallback;



    public ListChildFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (getActivity() instanceof LaunchFragmentCallback) {
            mCallback = (LaunchFragmentCallback) getActivity();
        }
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

        UserProfileResponseHandler responseHandler = new UserProfileResponseHandler(this);
        mUserProfile.getAllProfiles(responseHandler);

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


    /**
     * Initialize the {@link RecyclerView} with the Child data
     */
    private void initChildListRecycler() {
        if (mChildList == null) {
            return;
        }

        ChildListAdapter adapter = new ChildListAdapter(mChildList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        mChildListRecyclerView.setLayoutManager(layoutManager);
        mChildListRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
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


}
