package org.partner.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.partner.BuildConfig;
import org.partner.R;
import org.partner.callback.LaunchFragmentCallback;
import org.partner.customviews.CustomButton;
import org.partner.database.PartnerDBHelper;
import org.partner.util.PrefUtil;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SchoolInfoFragment extends Fragment {


    private static final String TAG = "SchoolInfoFragment";

    private static final boolean DEBUG = BuildConfig.DEBUG;


    private Spinner mDistrictSpinner;

    private Spinner mBlockSpinner;

    private Spinner mSchoolSpinner;


    private String mSelectedDistrict;

    private String mSelectedBlock;

    private String mSelectedSchool;



    /**
     * Callback for updating fragment
     */
    private LaunchFragmentCallback mCallback;



    public SchoolInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null && getActivity() instanceof LaunchFragmentCallback) {
            mCallback = (LaunchFragmentCallback) getActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_school_info, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDistrictSpinner = (Spinner) view.findViewById(R.id.spinnerDistrict);
        mBlockSpinner = (Spinner) view.findViewById(R.id.spinnerBlock);
        mSchoolSpinner = (Spinner) view.findViewById(R.id.spinnerSchool);




        mSelectedDistrict = PrefUtil.getString(PartnerDBHelper.FIELD_DISTRICT);
        mSelectedBlock = PrefUtil.getString(PartnerDBHelper.FIELD_BLOCK);
        mSelectedSchool = PrefUtil.getString(PartnerDBHelper.FIELD_SCHOOL_NAME);




        fillUpSpinnerWithData(mDistrictSpinner, PartnerDBHelper.FIELD_DISTRICT,
                getString(R.string.district_default));

        fillUpSpinnerWithData(mBlockSpinner, PartnerDBHelper.FIELD_BLOCK,
                getString(R.string.block_default));

        fillUpSpinnerWithData(mSchoolSpinner, PartnerDBHelper.FIELD_SCHOOL_NAME,
                getString(R.string.school_default));

        mDistrictSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    mSelectedDistrict = (String) mDistrictSpinner.getSelectedItem();
                    updateBlock(mSelectedDistrict);

                    PrefUtil.storeString(PartnerDBHelper.FIELD_DISTRICT, mSelectedDistrict);
                } else {
                    fillUpSpinnerWithData(mBlockSpinner, PartnerDBHelper.FIELD_BLOCK,
                            getString(R.string.block_default));

                    fillUpSpinnerWithData(mSchoolSpinner, PartnerDBHelper.FIELD_SCHOOL_NAME,
                            getString(R.string.school_default));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mBlockSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    mSelectedBlock = (String) mBlockSpinner.getSelectedItem();
                    updateSchool(mSelectedBlock);
                    PrefUtil.storeString(PartnerDBHelper.FIELD_BLOCK, mSelectedBlock);
                } else {
                    fillUpSpinnerWithData(mSchoolSpinner, PartnerDBHelper.FIELD_SCHOOL_NAME,
                            getString(R.string.school_default));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSchoolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    mSelectedSchool = (String) mSchoolSpinner.getSelectedItem();

                    PrefUtil.storeString(PartnerDBHelper.FIELD_SCHOOL_NAME, mSelectedSchool);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        CustomButton nextButton = (CustomButton) view.findViewById(R.id.next_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    String schoolID = PartnerDBHelper.getSchoolID(mSelectedDistrict, mSelectedBlock,
                            mSelectedSchool);

                    if (DEBUG) {
                        Log.i(TAG, "onClick: " + schoolID);
                    }

                    if (mCallback != null) {
                        Fragment fragment = ListChildFragment.newInstance(schoolID);
                        mCallback.launchFragment(fragment);
                    }
                }
            }
        });
    }




    private void fillUpSpinnerWithData(Spinner spinner, String field, String placeHolderStr) {
        ArrayList<String> localData = PartnerDBHelper.
                getDistinctValues(PartnerDBHelper.TABLE_SCHOOL_INFO, field);

        ArrayList<String> data = new ArrayList<>();

        if (!TextUtils.isEmpty(placeHolderStr)) {
            data.add(placeHolderStr);
        }

        data.addAll(localData);


        String previousSelectedValue = PrefUtil.getString(field);

        int selectedIndex = 0;

        if (!TextUtils.isEmpty(previousSelectedValue)) {
            selectedIndex = data.indexOf(previousSelectedValue);
        }

        if (selectedIndex < 0) {
            selectedIndex = 0;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item,
                data);
//        spinner.setAdapter(adapter);
        adapter.setDropDownViewResource(R.layout.spinner_popup_item);
        spinner.setAdapter(adapter);

        spinner.setSelection(selectedIndex, false);
    }


    private void updateBlock(String district) {
        ArrayList<String> localData;
        localData = PartnerDBHelper.getAllValueForFieldWithCondition(PartnerDBHelper.TABLE_SCHOOL_INFO,
                        PartnerDBHelper.FIELD_BLOCK, PartnerDBHelper.FIELD_DISTRICT, district);

        ArrayList<String> data = new ArrayList<>();
        data.add(getString(R.string.block_default));
        data.addAll(localData);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item,
                data);
        mBlockSpinner.setAdapter(adapter);
        adapter.setDropDownViewResource(R.layout.spinner_popup_item);
        mBlockSpinner.setAdapter(adapter);
    }

    private void updateSchool(String block) {
        ArrayList<String> localData;
        localData = (ArrayList<String>) PartnerDBHelper
                .getAllValueForFieldWithCondition(PartnerDBHelper.TABLE_SCHOOL_INFO,
                        PartnerDBHelper.FIELD_SCHOOL_NAME, PartnerDBHelper.FIELD_BLOCK, block);

        ArrayList<String> data = new ArrayList<>();
        data.add(getString(R.string.school_default));
        data.addAll(localData);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item,
                data);
        mSchoolSpinner.setAdapter(adapter);
        adapter.setDropDownViewResource(R.layout.spinner_popup_item);
        mSchoolSpinner.setAdapter(adapter);
    }


    private boolean validate() {

        if (mDistrictSpinner.getSelectedItemPosition() == 0
                || TextUtils.isEmpty(mSelectedDistrict)) {
            Toast.makeText(getActivity(), "Please Select District", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mBlockSpinner.getSelectedItemPosition() == 0
                || TextUtils.isEmpty(mSelectedBlock)) {
            Toast.makeText(getActivity(), "Please Select Block", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mSchoolSpinner.getSelectedItemPosition() == 0
                || TextUtils.isEmpty(mSelectedSchool)) {
            Toast.makeText(getActivity(), "Please Select School", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }


}
