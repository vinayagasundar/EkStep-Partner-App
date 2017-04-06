package org.partner.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ekstep.genieservices.aidls.domain.Profile;
import org.ekstep.genieservices.sdks.Partner;
import org.ekstep.genieservices.sdks.Telemetry;
import org.ekstep.genieservices.sdks.UserProfile;
import org.ekstep.genieservices.sdks.response.GenieResponse;
import org.partner.BuildConfig;
import org.partner.R;
import org.partner.Util.AppConstants;
import org.partner.Util.TelemetryEventGenertor;
import org.partner.Util.Util;
import org.partner.callback.CurrentGetuserResponseHandler;
import org.partner.callback.CurrentuserResponseHandler;
import org.partner.callback.ICurrentGetUser;
import org.partner.callback.ICurrentUser;
import org.partner.callback.IPartnerData;
import org.partner.callback.IStartSession;
import org.partner.callback.ITelemetryData;
import org.partner.callback.IUserCreateProfile;
import org.partner.callback.PartnerDataResponseHandler;
import org.partner.callback.StartSessionResponseHandler;
import org.partner.callback.TelemetryResponseHandler;
import org.partner.callback.UserProfileCreateResponseHandler;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * A Fragment to create a new child in Genie
 * @author vinayagasundar
 */
public class CreateChildFragment extends Fragment
        implements IStartSession, IUserCreateProfile, ICurrentUser, ICurrentGetUser,
        IPartnerData, ITelemetryData {


    private static final String TAG = "CreateChildFragment";
    private static final boolean DEBUG = BuildConfig.DEBUG;

    private EditText mHandleEditText;

    private Spinner mGenderSpinner;

    private EditText mAgeEditText;

    private Spinner mClassSpinner;


    private EditText mChildNameEditText;

    private EditText mAddressEditText;

    private EditText mContactNumberText;

    private ProgressDialog mProgressDialog;


    /**
     * UID of the child selected
     */
    private String mUid;


    /**
     * User Profile to get the list of the Child & Set Current Child in genie
     */
    private UserProfile mUserProfile;


    /**
     * To start the Partner Session for moving into genie
     */
    private Partner mPartner;



    public CreateChildFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_child, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mHandleEditText = (EditText) view.findViewById(R.id.child_handle_text);
        mAgeEditText = (EditText) view.findViewById(R.id.age_text);

        mGenderSpinner = (Spinner) view.findViewById(R.id.gender_text);
        mClassSpinner = (Spinner) view.findViewById(R.id.class_text);

        mChildNameEditText = (EditText) view.findViewById(R.id.child_name_text);
        mAddressEditText = (EditText) view.findViewById(R.id.child_address_text);
        mContactNumberText = (EditText) view.findViewById(R.id.child_mobile_text);

        Button createChildButton = (Button) view.findViewById(R.id.create_child_button);
        createChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processData();
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();

        hideDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mUserProfile != null) {
            mUserProfile.finish();
        }

        if (mPartner != null) {
            mPartner.finish();
        }

        hideDialog();
    }


    @Override
    public void onSuccessSession(GenieResponse genieResponse) {
        String result = new Gson().toJson(genieResponse.getResult());

        if (DEBUG) {
            Log.d(TAG, "onSuccessSession: " + result);
        }

        Profile profile=new Profile(mHandleEditText.getText().toString(), "Avatar","en");
        profile.setStandard(Integer.valueOf(mClassSpinner.getSelectedItem().toString()));
        profile.setGender(mGenderSpinner.getSelectedItem().toString());
        profile.setAge(Integer.valueOf(mAgeEditText.getText().toString()));

        mUserProfile = new UserProfile(getActivity());
        UserProfileCreateResponseHandler responseHandler =
                new UserProfileCreateResponseHandler(this);
        mUserProfile.createUserProfile(profile, responseHandler);
    }

    @Override
    public void onFailureSession(GenieResponse genieResponse) {
        if (DEBUG) {
            Log.d(TAG, "onFailureSession: " + genieResponse.getError());
        }
        hideDialog();
    }


    @Override
    public void onSuccessUserProfile(GenieResponse genieResponse) {
        String json = new Gson().toJson(genieResponse.getResult());

        if (DEBUG) {
            Log.d(TAG, "onSuccessUserProfile: " + json);
        }

        Type type = new TypeToken<HashMap<String, String>>(){}.getType();
        HashMap<String, String> data = new Gson().fromJson(json, type);

        CurrentuserResponseHandler responseHandler = new CurrentuserResponseHandler(this);

        mUid = data.get("uid");

        if (DEBUG) {
            Log.d(TAG, "onSuccessUserProfile: UID : " + mUid);
        }

        if (mUserProfile != null) {
            mUserProfile.setCurrentUser(mUid, responseHandler);
        }
    }

    @Override
    public void onFailureUserProfile(GenieResponse genieResponse) {
        if (DEBUG) {
            Log.d(TAG, "onFailureUserprofile: " + genieResponse.getError());
        }
        hideDialog();
    }


    @Override
    public void onSuccessCurrentUser(GenieResponse genieResponse) {
        String result = new Gson().toJson(genieResponse);

        if (DEBUG) {
            Log.d(TAG, "onSuccessCurrentUser: " + result);
        }

        CurrentGetuserResponseHandler responseHandler = new CurrentGetuserResponseHandler(this);
        mUserProfile.getCurrentUser(responseHandler);
    }

    @Override
    public void onFailureCurrentUser(GenieResponse genieResponse) {
        String result = new Gson().toJson(genieResponse.getResult());
        if (DEBUG) {
            Log.d(TAG, "onFailureCurrentUser :" + result);
        }

        hideDialog();
    }


    @Override
    public void onSuccessCurrentGetUser(GenieResponse genieResponse) {
        String json = new Gson().toJson(genieResponse.getResult());

        if (DEBUG) {
            Log.d(TAG, "onSuccessCurrentGetUser: " + json);
        }

        HashMap<String, Object> partnerData = new HashMap<>();

        partnerData.put("uid", mUid);
        partnerData.put("child_name", mChildNameEditText.getText().toString());
        partnerData.put("address", mAddressEditText.getText().toString());
        partnerData.put("contact", mContactNumberText.getText().toString());

        PartnerDataResponseHandler responseHandler = new PartnerDataResponseHandler(this);

        if (mPartner != null) {

            if (DEBUG) {
                Log.i(TAG, "onSuccessCurrentGetUser: Partner Data : " + partnerData);
            }

            mPartner.sendData(AppConstants.PARTNER_ID, partnerData, responseHandler);
        }
    }

    @Override
    public void onFailureCurrentGetUser(GenieResponse genieResponse) {
        String result = new Gson().toJson(genieResponse.getResult());
        if (DEBUG) {
            Log.d(TAG, "onFailureCurrentGetUser :" + result);
        }

        hideDialog();
    }


    @Override
    public void onSuccessPartner(GenieResponse genieResponse) {
        String result = new Gson().toJson(genieResponse.getResult());
        if (DEBUG) {
            Log.d(TAG, "onSuccessPartner :" + result);
        }

        Util util = (Util) getActivity().getApplicationContext();

        Telemetry telemetry = new Telemetry(getActivity());

        TelemetryResponseHandler responseHandler = new TelemetryResponseHandler(this);

        telemetry.send(TelemetryEventGenertor.generateOEEndEvent(getActivity(),
                mUid,
                util.getStartTime(),
                System.currentTimeMillis()).toString(), responseHandler);
    }

    @Override
    public void onFailurePartner(GenieResponse genieResponse) {
        String result = new Gson().toJson(genieResponse.getResult());
        if (DEBUG) {
            Log.d(TAG, "onFailurePartner :" + result);
        }

        hideDialog();
    }


    @Override
    public void onSuccessTelemetry(GenieResponse genieResponse) {
        if (DEBUG)
            Log.d(TAG, "onSuccessTelemetry: ");

        Util.processSuccess(getActivity(), genieResponse);


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
    public void onFailureTelemetry(GenieResponse genieResponse) {
        if (DEBUG)
            Log.d(TAG, "onFailureTelemetry: ");

        hideDialog();
        Util.processSendFailure(getActivity(), genieResponse);
    }

    private void processData() {
        if (validate()) {
            showDialog();

            mPartner = new Partner(getActivity());
            StartSessionResponseHandler responseHandler = new StartSessionResponseHandler(this);
            mPartner.startPartnerSession(AppConstants.PARTNER_ID, responseHandler);
        }
    }


    private boolean validate() {
        if (TextUtils.isEmpty(mHandleEditText.getText())) {
            Toast.makeText(getActivity(), "Please enter Handle", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(mAgeEditText.getText())) {
            Toast.makeText(getActivity(), "Please enter age", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(mChildNameEditText.getText())) {
            Toast.makeText(getActivity(), "Please Enter child name", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (TextUtils.isEmpty(mAddressEditText.getText())) {
            Toast.makeText(getActivity(), "Please enter Address", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (TextUtils.isEmpty(mContactNumberText.getText())) {
            Toast.makeText(getActivity(), "Please enter Contact number", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        
        return true;
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
