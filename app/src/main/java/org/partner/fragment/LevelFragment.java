package org.partner.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import org.partner.R;
import org.partner.Util.Util;
import org.partner.activity.MainActivity;

public class LevelFragment extends Fragment {
    private boolean D= Util.DEBUG;
    private String TAG=LevelFragment.class.getSimpleName();
    private Context mContext;
    private Spinner spinnerLevel;
   private  Fragment mFragment;
    private  Util util;
    private String[]levelArray={"Child level","Generic level"};



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        mContext=getActivity();
        mFragment=this;
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       final View rootView=inflater.inflate(R.layout.level_fragment, container, false);
        Button btn_Next=(Button)rootView.findViewById(R.id.btn_Next);
       final RadioGroup radioGrpLevel=(RadioGroup)rootView.findViewById(R.id.radioGrpLevel);
       // Util.hideKeyboard(getActivity(),getActivity());


        btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util util = (Util) mContext.getApplicationContext();
                if (radioGrpLevel.getCheckedRadioButtonId() == -1) {
                    Util.showToastmessage(mContext, "Please select any one.");

                } else {
                    // get selected radio button from radioGroup
                    int selectedId = radioGrpLevel.getCheckedRadioButtonId();
                    // find the radiobutton by returned id
                    RadioButton selectedRadioButton = (RadioButton) rootView.findViewById(selectedId);
                    String selectedRB = selectedRadioButton.getText().toString();
                    if (selectedRB.equalsIgnoreCase("for each child")) {
                        if ((util.getUidProfile().isEmpty()))
                            Toast.makeText(mContext, getString(R.string.level_not_valid), Toast.LENGTH_LONG).show();
                        else
                            moveToNextScreen(selectedRB);
                    } else
                        moveToNextScreen(selectedRB);

                }



            }
        });

        return rootView;
    }

    private void moveToNextScreen(String levelValue){
        DisplayChildProfileFragment displayChildProfileFragment = new DisplayChildProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("levelValue", levelValue);
        displayChildProfileFragment.setArguments(bundle);
        ((MainActivity) mContext).switchContent(displayChildProfileFragment, 1, true,"DisplayChildProfileFragment");

    }

}
