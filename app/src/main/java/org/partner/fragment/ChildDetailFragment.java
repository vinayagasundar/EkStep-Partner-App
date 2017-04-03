package org.partner.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ekstep.genieservices.sdks.Partner;
import org.ekstep.genieservices.sdks.UserProfile;
import org.ekstep.genieservices.sdks.response.GenieListResponse;
import org.ekstep.genieservices.sdks.response.GenieResponse;
import org.json.JSONArray;
import org.partner.BuildConfig;
import org.partner.R;
import org.partner.Util.AppConstants;
import org.partner.Util.TelemetryEventGenertor;
import org.partner.Util.Util;
import org.partner.callback.CurrentGetuserResponseHandler;
import org.partner.callback.CurrentuserResponseHandler;
import org.partner.callback.ICurrentGetUser;
import org.partner.callback.ICurrentUser;
import org.partner.callback.IStartSession;
import org.partner.callback.IUserProfile;
import org.partner.callback.StartSessionResponseHandler;
import org.partner.callback.UserProfileResponseHandler;
import org.partner.model.Child;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChildDetailFragment extends Fragment
        implements IUserProfile, IStartSession, ICurrentUser, ICurrentGetUser {

    private static final String TAG = "ChildDetailFragment";

    private static final boolean DEBUG = BuildConfig.DEBUG;

    private static final String BUNDLE_UID = "uid";

    /**
     * UID of the child selected
     */
    private String mUid;



    private TextView mHandleText;
    private TextView mGenderText;
    private TextView mAgeText;
    private TextView mClassText;


    private UserProfile mUserProfile;

    private Partner mPartner;

    private ProgressDialog mProgressDialog;


    /**
     * Create a new instance of the Fragment
     * @param uid UID used inside the fragment
     * @return instance of the {@link ChildDetailFragment}
     */
    public static ChildDetailFragment newInstance(String uid) {
        ChildDetailFragment fragment = new ChildDetailFragment();

        Bundle args = new Bundle();
        args.putString(BUNDLE_UID, uid);

        fragment.setArguments(args);

        return fragment;
    }


    public ChildDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mUid = getArguments().getString(BUNDLE_UID, null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_child_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mHandleText = (TextView) view.findViewById(R.id.child_handle_text);
        mGenderText = (TextView) view.findViewById(R.id.gender_text);
        mAgeText = (TextView) view.findViewById(R.id.age_text);
        mClassText = (TextView) view.findViewById(R.id.class_text);


        mUserProfile = new UserProfile(getActivity());

        UserProfileResponseHandler responseHandler = new UserProfileResponseHandler(this);
        mUserProfile.getAllProfiles(responseHandler);

        Button launchGenie = (Button) view.findViewById(R.id.launch_genie_button);
        launchGenie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processData();
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

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
        List<Child> childList = new Gson().fromJson(jsonArray.toString(), childType);


        for (Child child : childList) {
            if (child.getUid().equals(mUid)) {
                displayChildDetails(child);
                break;
            }
        }
    }

    @Override
    public void onFailureUserprofile(GenieListResponse genieListResponse) {
        if (DEBUG) {
            Log.i(TAG, "onFailureUserprofile: " + genieListResponse.getError());
        }

        Toast.makeText(getActivity(), genieListResponse.getError(),
                Toast.LENGTH_SHORT).show();

        hideDialog();
    }


    @Override
    public void onSuccessSession(GenieResponse genieResponse) {
        if (DEBUG) {
            Log.i(TAG, "onSuccessSession: " + genieResponse);
        }

        CurrentuserResponseHandler responseHandler = new CurrentuserResponseHandler(this);
        mUserProfile.setCurrentUser(mUid, responseHandler);
    }

    @Override
    public void onFailureSession(GenieResponse genieResponse) {
        if (DEBUG) {
            Log.i(TAG, "onFailureSession: " + genieResponse.getError());
        }

        Toast.makeText(getActivity(), genieResponse.getError(), Toast.LENGTH_SHORT).show();

        hideDialog();
    }


    @Override
    public void onSuccessCurrentUser(GenieResponse genieResponse) {
        if (DEBUG) {
            Log.i(TAG, "onSuccessCurrentUser: " + genieResponse);
        }

        CurrentGetuserResponseHandler responseHandler = new CurrentGetuserResponseHandler(this);
        mUserProfile.setCurrentUser(mUid, responseHandler);
    }

    @Override
    public void onFailureCurrentUser(GenieResponse genieResponse) {
        if (DEBUG) {
            Log.i(TAG, "onFailureCurrentUser: " + genieResponse.getError());
        }

        Toast.makeText(getActivity(), genieResponse.getError(), Toast.LENGTH_SHORT).show();

        hideDialog();
    }


    @Override
    public void onSuccessCurrentGetUser(GenieResponse genieResponse) {
        if (DEBUG) {
            Log.i(TAG, "onSuccessCurrentGetUser: " + genieResponse);
        }

        Util util = (Util) getActivity().getApplication();

        TelemetryEventGenertor.generateOEEndEvent(getActivity(), mUid, util.getStartTime(),
                System.currentTimeMillis());


        PackageManager manager = getActivity().getPackageManager();
        try {
            //Toast.makeText(MainActivity.this, packageName, Toast.LENGTH_LONG).show();
            Intent intent = manager.getLaunchIntentForPackage(
                    AppConstants.GENIE_SERVICES_PACKAGE_NAME);
            if (intent == null) {
                return;
                //throw new PackageManager.NameNotFoundException();
            }
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            getActivity().startActivity(intent);
            getActivity().finish();
        } catch (Exception e) {

        }
    }

    @Override
    public void onFailureCurrentGetUser(GenieResponse genieResponse) {
        if (DEBUG) {
            Log.i(TAG, "onFailureCurrentGetUser: " + genieResponse.getError());
        }

        Toast.makeText(getActivity(), genieResponse.getError(), Toast.LENGTH_SHORT).show();

        hideDialog();
    }

    private void displayChildDetails(Child child) {
        mHandleText.setText(child.getHandle());

        mAgeText.setText(String.valueOf(child.getAge()));
        mClassText.setText(String.valueOf(child.getStandard()));
        mGenderText.setText(child.getGender());
    }


    private void processData() {
        showDialog();

        mPartner = new Partner(getActivity());

        StartSessionResponseHandler responseHandler = new StartSessionResponseHandler(this);
        mPartner.startPartnerSession(AppConstants.PARTNER_ID, responseHandler);
    }

    private void showDialog() {
        hideDialog();

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait");
        mProgressDialog.show();
    }

    private void hideDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
