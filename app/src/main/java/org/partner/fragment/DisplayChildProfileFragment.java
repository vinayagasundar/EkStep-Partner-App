package org.partner.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.partner.R;
import org.partner.Util.TelemetryEventGenertor;
import org.partner.Util.Util;
import org.partner.activity.MainActivity;
import org.partner.callback.CurrentGetuserResponseHandler;
import org.partner.callback.CurrentuserResponseHandler;
import org.partner.callback.EndSessionResponseHandler;
import org.partner.callback.ICurrentGetUser;
import org.partner.callback.ICurrentUser;
import org.partner.callback.IEndSession;
import org.partner.callback.IPartnerData;
import org.partner.callback.IStartSession;
import org.partner.callback.ITelemetryData;
import org.partner.callback.IUserProfile;
import org.partner.callback.PartnerDataResponseHandler;
import org.partner.callback.StartSessionResponseHandler;
import org.partner.callback.TelemetryResponseHandler;
import org.partner.callback.UserProfileResponseHandler;
import org.partner.customviews.CustomEditText;
import org.partner.customviews.CustomTextView;
import org.partner.customviews.MyProgressBar;
import org.partner.model.Age;
import org.partner.model.ConfigModel;
import org.partner.model.FieldModel;
import org.partner.model.InstructionsField;
import org.partner.model.SectionValue;
import org.ekstep.genieservices.aidls.domain.Profile;
import org.ekstep.genieservices.sdks.Partner;
import org.ekstep.genieservices.sdks.Telemetry;
import org.ekstep.genieservices.sdks.UserProfile;
import org.ekstep.genieservices.sdks.response.GenieResponse;

/**
 * Created by Dhruv on 10/7/2015.
 */
public class DisplayChildProfileFragment extends Fragment implements IEndSession,IStartSession,IPartnerData
        {
    private boolean D= Util.DEBUG;
    private String TAG=DisplayChildProfileFragment.class.getSimpleName();
    private Context mContext;
    private  Fragment mFragment;
    private Button Register_btn;
    private Spinner spinnerLanguage,spinnerGender,spinnerClass;
    private LinearLayout displayLayout;
    private TextView child_nameLabel,father_nameLabel,child_classLabel,genderLabel,dobLabel,languageLabel;
    private EditText child_name,father_name;
    private  HashMap<Integer,String>hashMap_order=new HashMap<>();
    private Map<String,Object> hashMapDataMain=new HashMap<>();
    private  CustomTextView[] textViewHeading=new CustomTextView[60];
    private  CustomTextView[] textViewInstructions=new CustomTextView[60];
    private  CustomTextView[] textViews=new CustomTextView[60];

    private  CustomTextView[] textViewData=new CustomTextView[60];
    private CustomEditText[] editTexts=new CustomEditText[60];
    private Spinner[] spinners=new Spinner[60];
    private int sizeOfMap=0;
    private  ArrayList<String> language_List=new ArrayList<>();
    private  String[]gender_array={"MALE","FEMALE"};
    private  String[]child_classarray={"4","5"};
    private  HashMap<String,String> code_language_List=new HashMap<>();
    private List<HashMap<String,String>>hashMapLanguage;
    private DatePickerDialog mDatePickerDialog=null;
    private int year;
    private int month;
    private int day;
    private View rootView;
    private  GradientDrawable gradientDrawable;
    private Profile profile;
    private UserProfile userProfile;
    private UserProfileResponseHandler userProfileResponseHandler;
    private CurrentuserResponseHandler currentuserSetResponseHandler;
    private CurrentGetuserResponseHandler currentGetuserResponseHandler;
    private Partner partner;
    private Telemetry telemetry;
    private StartSessionResponseHandler startSessionResponseHandler;
    private EndSessionResponseHandler endSessionResponseHandler;
    private TelemetryResponseHandler telemetryResponseHandler;
    private PartnerDataResponseHandler partnerDataResponseHandler;
    private String UID;
    private String code;
    private String handle="";
    private MyProgressBar progressBar;
    private ScrollView scrollViewChildContainer;
    private CheckBox[] checkBoxes=new CheckBox[60];
    private RadioGroup[] radioGroups=new RadioGroup[120];
    private RadioButton[] radioButtons=new RadioButton[60];
    private ConfigModel configModel;
    private boolean ischkRB=false;
    private Spinner spinnerProfile;
    private LinearLayout linearLayoutprofile;
    private boolean oe_start=true;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // ((MainActivity)mContext).showBackIcon("Child Registration",mFragment);
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        mContext=getActivity();
        mFragment=this;
        super.onCreate(savedInstanceState);

    }

    LayoutInflater layoutInflater;
    boolean isChildLevel=false;
    ArrayList<String>uidProfile=new ArrayList<>();
    ArrayList<String>handleProfile=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater=getLayoutInflater(savedInstanceState);
        rootView=inflater.inflate(R.layout.display_child_profile, container, false);
        Register_btn=(Button)rootView.findViewById(R.id.Register_btn);
        displayLayout=(LinearLayout)rootView.findViewById(R.id.displayLayout);
        progressBar=(MyProgressBar)rootView.findViewById(R.id.sendingDetails);
        scrollViewChildContainer=(ScrollView)rootView.findViewById(R.id.scrollViewChildContainer);
        linearLayoutprofile=(LinearLayout)rootView.findViewById(R.id.linearLayoutprofile);
        String levelValue=getArguments().getString("levelValue");
        if(levelValue.equalsIgnoreCase("for each child")){
            linearLayoutprofile.setVisibility(View.VISIBLE);
            isChildLevel=true;
            Util util=(Util)mContext.getApplicationContext();
            uidProfile=util.getUidProfile();
            handleProfile=util.getHandleProfile();
            spinnerProfile=(Spinner)rootView.findViewById(R.id.spinnerProfile);
            ArrayAdapter<String> adapterLevel=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,handleProfile);
            adapterLevel.setDropDownViewResource(R.layout.spinner_popup_item);
            spinnerProfile.setAdapter(adapterLevel);
        }else
            linearLayoutprofile.setVisibility(View.GONE);


        displayForm();


        Register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValidateData()) {
                     sendDataToGenieServices();

                }

            }
        });
        return rootView;
    }
    /*
    * To validate form data*/
    private boolean isValidateData(){
        int sectionSize = configModel.getSectionValues().length;
        if (D)
            Log.d(TAG, "Register_btn sectionSize==>" + sectionSize);

        SectionValue[] sectionValues = configModel.getSectionValues();
        boolean flag = true, noEmptyField = true;
        try {
            int i = 0;
            for (int pos = 0; pos < sectionSize; pos++) {
                final FieldModel[] fieldModels = sectionValues[pos].getFieldModels();
                int length = fieldModels.length;
                Map<String,Object> hashMapData=new HashMap<>();
                String sectionHeading=sectionValues[pos].getSectionHeading();
                for (int j = 0; j < length; j++) {
                    i++;
                    final String fieldLabelName = fieldModels[j].getFieldName();
                    if (D)
                        Log.d(TAG, "fieldLabelName==>" + fieldLabelName);
                    if (!fieldLabelName.equalsIgnoreCase("instructions")) {
                        String fieldType = fieldModels[j].getFieldValue().getFieldType();
                        String fieldInputType = fieldModels[j].getFieldValue().getFieldInputType();

                        if (D)
                            Log.d(TAG, "fieldType" + fieldType + ", fieldInputType" + fieldInputType);
                        boolean validation = fieldModels[j].getFieldValue().getValidation().isFlag();
                        if (D)
                            Log.d(TAG, "4.validation" + validation);

                        final int fieldPos = j;
                        final int editRows = i;

                        int maxvalue = fieldModels[fieldPos].getFieldValue().getValidation().getMaximum();
                        int minvalue = fieldModels[fieldPos].getFieldValue().getValidation().getMinimum();
                        if (fieldInputType.equals("date") ) {
                            if (validation) {
                                if (textViewData[i].getText().toString().trim().isEmpty()) {
                                    Util.showToastmessage(mContext, "Please select " + fieldLabelName);
                                    noEmptyField = false;
                                    break;
                                }

                            }
                            //Setting date
                            hashMapData.put(fieldLabelName, textViewData[i].getText().toString());


                        }
                        else if (fieldType.equals("Text")) {  //1. Validation of TEXT
                            String charstr = editTexts[editRows].getText().toString();
                            int charCount = charstr.length();
                            //(i). Validation of Email
                            if (fieldInputType.equals("textEmailAddress")) {
                                String emailid = editTexts[i].getText().toString();
                                //for emailid
                                Pattern pattern2 = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
                                Matcher matcher2 = pattern2.matcher(emailid);
                                Boolean emailpattern = matcher2.matches();
                                if (validation) { //validation :true
                                    if (emailid.trim().isEmpty()) {
                                        Util.showToastmessage(mContext, "Please enter " + fieldLabelName);
                                        noEmptyField = false;
                                        break;
                                    } else if (!emailpattern) {
                                        Util.showToastmessage(mContext, "Please enter valid " + fieldLabelName);
                                        noEmptyField = false;
                                        break;
                                    } else if (charCount < minvalue) { // Validation
                                        Util.showToastmessage(mContext, fieldLabelName + " can't be less than " + minvalue + " character.");
                                        noEmptyField = false;
                                        break;

                                    } else if (charCount > maxvalue) { // Validation
                                        Util.showToastmessage(mContext, fieldLabelName + " can't be more than " + maxvalue + " character.");
                                        noEmptyField = false;
                                        break;
                                    }


                                } else {  //validation :false
                                    if (!emailid.trim().isEmpty()) {
                                        if (!emailpattern) {
                                            Util.showToastmessage(mContext, "Please enter valid " + fieldLabelName);
                                            noEmptyField = false;
                                            break;
                                        }
                                    }
                                }
                                //Setting email-id
                                hashMapData.put(fieldLabelName, editTexts[i].getText().toString());


                            }//------------------End of Validation of email
                            //(ii)Validation of integer
                            else if(fieldInputType.equalsIgnoreCase("number")){
                                if (validation) {
                                    int inputInteger=0;
                                    if(!charstr.isEmpty()){
                                        try{
                                         inputInteger =Integer.parseInt(charstr);}catch (NumberFormatException e){
                                            Util.showToastmessage(mContext, fieldLabelName + " can't be more than " + maxvalue);
                                            noEmptyField = false;
                                            break;
                                          }
                                    }

                                    if (editTexts[i].getText().toString().trim().isEmpty()) {
                                        Util.showToastmessage(mContext, "Please enter " + fieldLabelName);
                                        noEmptyField = false;
                                        break;
                                    } else if (inputInteger < minvalue) { // Validation
                                        Util.showToastmessage(mContext, fieldLabelName + " can't be less than " + minvalue);
                                        noEmptyField = false;
                                        break;

                                    } else if (inputInteger > maxvalue) { // Validation
                                        Util.showToastmessage(mContext, fieldLabelName + " can't be more than " + maxvalue);
                                        noEmptyField = false;
                                        break;
                                    }
                                }
                                //Setting text
                                hashMapData.put(fieldLabelName, editTexts[i].getText().toString());


                            } //end of (ii)Validation of integer
                            //(iii)Validation of numberDecimal
                            else if(fieldInputType.equalsIgnoreCase("numberDecimal")){
                                if (validation) {
                                    double inputDouble=0.0;
                                    if(!charstr.isEmpty())
                                        inputDouble=Double.parseDouble(charstr);

                                    if (editTexts[i].getText().toString().trim().isEmpty()) {
                                        Util.showToastmessage(mContext, "Please enter " + fieldLabelName);
                                        noEmptyField = false;
                                        break;
                                    } else if (inputDouble < minvalue) { // Validation
                                        Util.showToastmessage(mContext, fieldLabelName + " can't be less than " + minvalue);
                                        noEmptyField = false;
                                        break;

                                    } else if (inputDouble > maxvalue) { // Validation
                                        Util.showToastmessage(mContext, fieldLabelName + " can't be more than " + maxvalue);
                                        noEmptyField = false;
                                        break;
                                    }
                                }
                                //Setting text
                                hashMapData.put(fieldLabelName, editTexts[i].getText().toString());


                            } //end of (ii)Validation of integer


                            //(ii). Validation of text
                            else {
                                if (validation) {
                                    if (editTexts[i].getText().toString().trim().isEmpty()) {
                                        Util.showToastmessage(mContext, "Please enter " + fieldLabelName);
                                        noEmptyField = false;
                                        break;
                                    } else if (charCount < minvalue) { // Validation
                                        Util.showToastmessage(mContext, fieldLabelName + " can't be less than " + minvalue + " character.");
                                        noEmptyField = false;
                                        break;

                                    } else if (charCount > maxvalue) { // Validation
                                        Util.showToastmessage(mContext, fieldLabelName + " can't be more than " + maxvalue + " character.");
                                        noEmptyField = false;
                                        break;
                                    }
                                }
                                //Setting text
                                hashMapData.put(fieldLabelName, editTexts[i].getText().toString());


                            } //end of  Validation of text.............


                        } else if (fieldType.equals("MultipleChoiceMore")) {  //2. Validation of CheckBox

                            //fieldValues
                            ArrayList field_Value = fieldModels[j].getFieldValues();
                            boolean ischkBox = false;
                            ArrayList<String> OptionSelected = new ArrayList<String>();

                            for (int k = 0; k < field_Value.size(); k++) {
                                if (checkBoxes[k].isChecked()) {
                                    ischkBox = true;
                                    OptionSelected.add(checkBoxes[k].getText().toString());

                                }
                            }
                            if (validation) { //validation :true
                                int size = OptionSelected.size();
                                if (!ischkBox) {
                                    Util.showToastmessage(mContext, "Please select " + fieldLabelName);
                                    noEmptyField = false;
                                    break;
                                } else if (size < minvalue) { // Validation
                                    Util.showToastmessage(mContext, "You should select " + fieldLabelName + " minimum " + minvalue + " option.");
                                    noEmptyField = false;
                                    break;

                                } else if (size > maxvalue) { // Validation
                                    Util.showToastmessage(mContext, "You can't select " + fieldLabelName + " more than " + maxvalue + " option.");
                                    noEmptyField = false;
                                    break;
                                }

                            }

                            hashMapData.put(fieldLabelName, OptionSelected);


                        }// End of Validation of CheckBox
                        //--------------------3. Validation of Radio button
                        else if (fieldType.equals("MultipleChoiceSingle")) {
                            //fieldValues
                            ArrayList field_Value = fieldModels[j].getFieldValues();
                            if (radioGroups[i].getCheckedRadioButtonId() == -1) {
                                if (validation) { //validation :true
                                    Util.showToastmessage(mContext, "Please select any one " + fieldLabelName);
                                    noEmptyField = false;
                                    break;
                                }
                            } else {
                                for (int k = 0; k < field_Value.size(); k++) {
                                    // get selected radio button from radioGroup
                                    int selectedId = radioGroups[i].getCheckedRadioButtonId();
                                    // find the radiobutton by returned id
                                    RadioButton selectedRadioButton = (RadioButton) rootView.findViewById(selectedId);
                                    String selectedRB = selectedRadioButton.getText().toString();
                                    hashMapData.put(fieldLabelName, selectedRB);
                                }
                            }
                        } //-------------end of Radio Button
                        //--------------------------4.DropDown
                        else if (fieldType.equals("DropDown")) { //dropdown value
                            hashMapData.put(fieldLabelName, spinners[i].getSelectedItem().toString());
                        }//5. Validation of TextComment-----------------
                        else if (fieldType.equals("TextComment")) {
                            String charstr = editTexts[editRows].getText().toString();
                            int charCount = charstr.length();

                            if (validation) {
                                if (editTexts[i].getText().toString().trim().isEmpty()) {
                                    Util.showToastmessage(mContext, "Please enter " + fieldLabelName);
                                    noEmptyField = false;
                                    break;
                                } else if (charCount < minvalue) { // Validation
                                    Util.showToastmessage(mContext, fieldLabelName + " can't be less than " + minvalue + " line.");
                                    noEmptyField = false;
                                    break;

                                }
                            }
                            //Setting text
                            hashMapData.put(fieldLabelName, editTexts[i].getText().toString());


                        }


                    }

                } //end of inner for-loop

                if (!noEmptyField) {
                    if (D)
                        Log.d(TAG, "  :breaking inner loop  noEmptyField==>" + noEmptyField);

                    break;
                }
                if(sectionHeading.isEmpty())
                    hashMapDataMain.put("NO_HEADING"+pos,hashMapData);
                else
                    hashMapDataMain.put(sectionHeading,hashMapData);

            }//end of outer for loop
        } catch (Exception e) {
            if (D)
                Log.d(TAG, "Exception in register info:" + e);
        }

        if (D)
            Log.d(TAG, noEmptyField + " noEmptyField :hashMapDataMain ==>" + hashMapDataMain);

        return  noEmptyField;
    }

    private void sendDataToGenieServices(){
        progressBar.setVisibility(View.VISIBLE);
        scrollViewChildContainer.setVisibility(View.GONE);
        Register_btn.setVisibility(View.GONE);
        Util util=(Util)mContext.getApplicationContext();
        partner=util.getPartner();
        endSessionResponseHandler=new EndSessionResponseHandler(DisplayChildProfileFragment.this);
        //1. terminate session
        partner.terminatePartnerSession(configModel.getPartnerId(), endSessionResponseHandler);
        if(isChildLevel){
            int pos=handleProfile.indexOf(spinnerProfile.getSelectedItem().toString());
            UID=uidProfile.get(pos);
            hashMapDataMain.put(Util.UID, UID);
        }
       }


    private void displayToastMsg(String msg){
       Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
    }

    int editRow=0;
    int lastSpecialRequestsCursorPosition;
    String specialRequests;
    int dateRow = 0,SectionField=0,SectionfieldRow;
    DatePickerDialog.OnDateSetListener mDatePickerListeners[]=new DatePickerDialog.OnDateSetListener[100];
    /*
    * displaying fields based on configuration file*/
    private void displayForm(){
        try {
             configModel=new Util(mContext).getConfigModel();
            int sectionSize=configModel.getSectionValues().length;
             if (D)
                Log.d(TAG, "sectionSize :" + sectionSize);
            /*String partnerName=configModel.getPartnerName();
            ((MainActivity) mContext).showTitle(partnerName, mFragment);
          */ final SectionValue[] sectionValues=configModel.getSectionValues();
            //Layout inflater
            View view;
            InstructionsField[] instructionsFields=new InstructionsField[sectionSize];

            int i=0;
            for (int pos=0;pos<sectionSize;pos++){
                //sectionValues[i]=new SectionValue();
                if(D)
                    Log.d(TAG," sectionValues[i]==>"+sectionValues[pos]);
                 final FieldModel[] fieldModels=sectionValues[pos].getFieldModels();
                int length=fieldModels.length;
                if(D)
                    Log.d(TAG,"sectionValues length==>"+length);

                //1st position fixed as sectionHeadings
                String heading=sectionValues[pos].getSectionHeading();
                if (D)
                    Log.d(TAG, " getSectionHeading==>" +heading );

                if(!heading.isEmpty()){
                    view=layoutInflater.inflate(R.layout.textview_heading_template,displayLayout,false);
                    textViewHeading[pos] = (CustomTextView)view.findViewById(R.id.textHeadingTemplate);
                    textViewHeading[pos].setText(heading);
                    displayLayout.addView(textViewHeading[pos]);}

                for (int j=0;j<length;j++){
                    i++;
                    try {
                        //-------------------field displaying
                        final String fieldLabelName=fieldModels[j].getFieldName();
                        String fieldType ="";
                        String fieldInputType = "";
                        String fieldHint=fieldModels[j].getFieldHint();


                        // int displayOrder = fieldModels[j].getDisplayOrder();
                        if(fieldLabelName.equalsIgnoreCase("instructions")){
                            String instruction=fieldModels[j].getFieldHint();
                            if(!instruction.isEmpty()){
                                view=layoutInflater.inflate(R.layout.textview_instructions_template,displayLayout,false);
                                textViewInstructions[pos] = (CustomTextView)view.findViewById(R.id.textInstructionsTemplate);
                                textViewInstructions[pos].setText(instruction);
                                displayLayout.addView(textViewInstructions[pos]);}

                        }else{
                            if (D)
                                Log.d(TAG, "fieldType at pos CreateForm :" + i);

                            fieldType =fieldModels[j].getFieldValue().getFieldType();
                            fieldInputType = fieldModels[j].getFieldValue().getFieldInputType();
                            //Heading as Label
                            view=layoutInflater.inflate(R.layout.textview_template,displayLayout,false);
                            textViews[i] = (CustomTextView)view.findViewById(R.id.textTemplate);
                            if(fieldModels[j].getFieldValue().getValidation().isFlag())
                                textViews[i].setText(fieldLabelName + "(*)");
                            else
                                textViews[i].setText(fieldLabelName);

                            displayLayout.addView(textViews[i]);

                            //Label value
                            if(fieldInputType.equals("date")){
                                if(D)
                                    Log.d(TAG,fieldLabelName+"==>fieldLabelName textViewData date row==>"+i);

                                final int row=i;
                                final int sectionPos=pos;
                                final int fieldRow=j;


                                view=layoutInflater.inflate(R.layout.textview_dob_template,displayLayout,false);
                                textViewData[i] =(CustomTextView)view.findViewById(R.id.textdobTemplate);
                                if(!fieldHint.isEmpty())
                                textViewData[i].setHint(fieldHint);
                                else
                                    textViewData[i].setHint("Select " + fieldLabelName);

                                displayLayout.addView(textViewData[i]);

                                Calendar c = Calendar.getInstance();
                                year  = c.get(Calendar.YEAR);
                                month = c.get(Calendar.MONTH);
                                day   = c.get(Calendar.DAY_OF_MONTH);
                                mDatePickerListeners[i]=new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker dp, int selectedYear, int monthOfYear,
                                                          int dayOfMonth) {
                                        year = selectedYear;
                                        month = monthOfYear;
                                        day = dayOfMonth;
                                        String strDay;
                                        String strMonth;
                                        if(day<10){
                                            strDay="0"+day;
                                        }
                                        else{
                                            strDay=""+day;
                                        }

                                        if((month+1)<10){
                                            strMonth="0"+(month+1);
                                        }
                                        else{
                                            strMonth=""+(month+1);
                                        }
                                        String date=year+"-"+ strMonth + "-"+strDay;
                                        if(D)
                                            Log.d(TAG,"date===>"+date+" dateRow=>"+dateRow+" SectionfieldRow==>"+SectionfieldRow+" sectionPos:"+SectionField);

                                        int requiredAge= Age.getChildAge(date);
                                        String fieldName=sectionValues[SectionField].getFieldModels()[SectionfieldRow].getFieldName();
                                        boolean validation=sectionValues[SectionField].getFieldModels()[SectionfieldRow].getFieldValue().getValidation().isFlag();
                                        if(validation){
                                            int minmunYear=sectionValues[SectionField].getFieldModels()[SectionfieldRow].getFieldValue().getValidation().getMinimum();
                                            int maximunYear=sectionValues[SectionField].getFieldModels()[SectionfieldRow].getFieldValue().getValidation().getMaximum();
                                            if(D)
                                                Log.d(TAG,fieldName+" --fieldName>minmunYear:"+minmunYear+", maximunYear"+maximunYear);


                                            if(requiredAge<minmunYear)
                                                displayToastMsg(fieldName+" should be minumum "+minmunYear+" year");
                                            else if(requiredAge>maximunYear)
                                                displayToastMsg(fieldName+" can't be greater than "+maximunYear+" year");
                                            else{ //set date
                                                textViewData[dateRow].setText(date);
                                                // mTxt_dob.setTextColor(getResources().getColor(android.R.color.black));

                                                // scrolling a particular view in ScrollView
                                                new Handler().post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        scrollViewChildContainer.smoothScrollTo(0, textViewData[dateRow].getTop());
                                                        textViews[dateRow].requestFocus();
                                                        if (D)
                                                            Log.d(TAG, "scrollViewChildContainer===>");


                                                    }
                                                });


                                            }//end of set date


                                        }
                                        else{ //set date
                                            textViewData[dateRow].setText(date);
                                            // mTxt_dob.setTextColor(getResources().getColor(android.R.color.black));

                                            // scrolling a particular view in ScrollView
                                            new Handler().post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    scrollViewChildContainer.smoothScrollTo(0, textViewData[dateRow].getTop());
                                                    textViews[dateRow].requestFocus();
                                                    if (D)
                                                        Log.d(TAG, "scrollViewChildContainer===>");


                                                }
                                            });


                                        }//end of set date




                                    }
                                };


                                mDatePickerDialog=new DatePickerDialog(getActivity(), mDatePickerListeners[i], year, month, day);

                                textViewData[i].setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(D)
                                            Log.d(TAG,sectionPos+"-->sectionPos,  textViewData row==>"+row+" ,fieldRow:"+fieldRow);
                                        dateRow=row;
                                        SectionField=sectionPos;
                                        SectionfieldRow=fieldRow;
                                        mDatePickerDialog.show();
                                    }
                                });


                                textViewData[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                    @Override
                                    public void onFocusChange(View view, boolean b) {
                                        if(D)
                                            Log.d(TAG,"textViewData[i].setOnFocusChangeListener= row=>"+row);

                                        Util.hideKeyboard(getActivity(),mContext);
                                    }
                                });

                            }else if (fieldType.equals("Text")) { // label Value
                                if(D)
                                    Log.d(TAG,i+"==>editTexts at pos & fieldInputType :"+fieldInputType);

                                if(D)
                                    Log.d(TAG,"fieldInputType:"+fieldInputType);
                                view=layoutInflater.inflate(R.layout.edit_text_template,displayLayout,false);

                                editTexts[i] =(CustomEditText)view.findViewById(R.id.editTextTemplate);
                                if(!fieldHint.isEmpty())
                                    editTexts[i].setHint(fieldHint);
                                else
                                editTexts[i].setHint("Please enter your " + fieldLabelName);

                                if(fieldInputType.equals("phone"))
                                    editTexts[i].setInputType(InputType.TYPE_CLASS_PHONE);
                                else  if(fieldInputType.equals("number"))
                                    editTexts[i].setInputType(InputType.TYPE_CLASS_NUMBER);
                                else  if(fieldInputType.equals("numberDecimal"))
                                    editTexts[i].setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                 else if(fieldInputType.equals("textEmailAddress"))
                                    editTexts[i].setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                                else if(fieldInputType.equals("textPassword")){
                                    editTexts[i].setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                    editTexts[i].setTransformationMethod(PasswordTransformationMethod.getInstance());
                                }
                                displayLayout.addView(editTexts[i]);


                            }
                            else if (fieldType.equals("TextComment")) { // label Value type="comment";
                                if(D)
                                    Log.d(TAG,i+"==>editTexts at pos & fieldInputType :"+fieldInputType);

                                view=layoutInflater.inflate(R.layout.comment_template,displayLayout,false);
                                editTexts[i] =(CustomEditText)view.findViewById(R.id.commentTemplate);
                                if(!fieldHint.isEmpty())
                                    editTexts[i].setHint(fieldHint);
                                else
                                 editTexts[i].setHint("Please enter your " + fieldLabelName);

                                displayLayout.addView(editTexts[i]);
                                editRow=i;
                                final int fieldPos=j;
                                editTexts[i].addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                        lastSpecialRequestsCursorPosition = editTexts[editRow].getSelectionStart();

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        int lineCount = editTexts[editRow].getLineCount();
                                        int maxvalue =fieldModels[fieldPos].getFieldValue().getValidation().getMaximum();
                                        editTexts[editRow].removeTextChangedListener(this);
                                        if(D)
                                            Log.d(TAG,editRow+" ==>editRow....lineCount---->"+lineCount);

                                        if (lineCount > maxvalue) { // Validation
                                            Util.showToastmessage(mContext, fieldLabelName + " can't be more than " + maxvalue + " line.");
                                            editTexts[editRow].setText(specialRequests);
                                                editTexts[editRow].setSelection(lastSpecialRequestsCursorPosition);
                                                Util.hideKeyboard(getActivity(), mContext);
                                            }
                                            else
                                                specialRequests = editTexts[editRow].getText().toString();

                                        editTexts[editRow].addTextChangedListener(this);
                                    }
                                });
                            }else if (fieldType.equals("MultipleChoiceMore")) {
                                //fieldValues
                                ArrayList field_Value = fieldModels[j].getFieldValues();


                                if (D)
                                    Log.d(TAG, " fieldModels[j]  field_Values=>" + field_Value);

                                for(int k=0;k<field_Value.size();k++){
                                    view=layoutInflater.inflate(R.layout.checkbox_text_template,displayLayout,false);
                                    checkBoxes[k] =(CheckBox)view.findViewById(R.id.checkBoxTemplate);
                                    checkBoxes[k].setText(""+field_Value.get(k));
                                    displayLayout.addView(checkBoxes[k]);

                                }


                            } else if (fieldType.equals("MultipleChoiceSingle")) {
                                //fieldValues
                                ArrayList<String> field_Value = fieldModels[j].getFieldValues();


                                if (D)
                                    Log.d(TAG, " fieldModels[j]  field_Values=>" + field_Value);
                                view=layoutInflater.inflate(R.layout.radiogroup_template,displayLayout,false);
                                radioGroups[i] =(RadioGroup)view.findViewById(R.id.radioGroupTemplate);

                                for(int k=0;k<field_Value.size();k++){
                                    view=layoutInflater.inflate(R.layout.radio_text_template,displayLayout,false);

                                    radioButtons[k] =(RadioButton)view.findViewById(R.id.radioTemplate);
                                    radioButtons[k].setText(field_Value.get(k));
                                    radioButtons[k].setId(k);
                                    radioGroups[i].addView(radioButtons[k]);

                                }

                                displayLayout.addView(radioGroups[i]);



                            }

                            else if (fieldType.equals("DropDown")) { //dropdown value
                                view=layoutInflater.inflate(R.layout.spinner_template,displayLayout,false);
                                spinners[i] =(Spinner)view.findViewById(R.id.spinnerTemplate);
                                //fieldValues
                                ArrayList field_Value = fieldModels[j].getFieldValues();


                                if (D)
                                    Log.d(TAG, " fieldModels[j]  field_Values=>" + field_Value);
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item, field_Value);
                               // spinners[i].setAdapter(adapter);
                                adapter.setDropDownViewResource(R.layout.spinner_popup_item);
                                spinners[i].setAdapter(adapter);
                                displayLayout.addView(spinners[i]);

                            }
                            //--------end of field display


                        }//end of field display



                    }catch (Exception e){
                        if(D)
                            Log.e(TAG,"ex in fieldModels[j]:"+e);
                    }


                    //end of else




                }//end of inner for-loop

                if(D)
                    Log.d(TAG," =========end fields of section======>");

        }//end of outer for-loop


            //To Show soft keyboard
            if(sectionValues[0].getFieldModels()[0].getFieldName().equalsIgnoreCase("instructions")){
                //then at 1st pos if fieldType="Text" show key
                String fieldTypeChk=sectionValues[0].getFieldModels()[1].getFieldValue().getFieldType();
                String fieldInputTypeChk=sectionValues[0].getFieldModels()[1].getFieldValue().getFieldInputType();
                if(!fieldInputTypeChk.equals("date")){
                    if (fieldTypeChk.equals("Text")) {
                        //show key
                        if(D)
                            Log.d(TAG,"showing key-----------------");
                        Util.showKeyboard(getActivity(), mContext);
                    }
                }


            }else{
                //then at 0th pos if fieldType="Text" show key
                String fieldTypeChk=sectionValues[0].getFieldModels()[0].getFieldValue().getFieldType();
                String fieldInputTypeChk=sectionValues[0].getFieldModels()[0].getFieldValue().getFieldInputType();
                if(!fieldInputTypeChk.equals("date")){
                    if (fieldTypeChk.equals("Text")) {
                        //show key
                        if(D)
                            Log.d(TAG,"showing key-----------------");
                        Util.showKeyboard(getActivity(), mContext);
                    }
                }
            }
            //end of  Show soft keyboard




        }catch (Exception e){
            if(D)
                Log.e(TAG,"createChildForm Ex :"+e);

        }

    }



    @Override
    public void onSuccessEndSession(GenieResponse genieResponse) {
        String result=new Gson().toJson(genieResponse.getResult());
        if(D)
            Log.d(TAG,"onSuccessEndSession :"+result);
        //2. start partner session
        startSessionResponseHandler=new StartSessionResponseHandler(this);
        partner.startPartnerSession(configModel.getPartnerId(), startSessionResponseHandler);


    }

    @Override
    public void onFailureEndSession(GenieResponse genieResponse) {
        String result=new Gson().toJson(genieResponse.getResult());
        if(D)
            Log.d(TAG,"onFailureEndSession :"+result);
        progressBar.setVisibility(View.GONE);
        scrollViewChildContainer.setVisibility(View.VISIBLE);
        Register_btn.setVisibility(View.VISIBLE);

    }

    @Override
    public void onSuccessSession(GenieResponse genieResponse) {
        String result=new Gson().toJson(genieResponse.getResult());
        if(D)
            Log.d(TAG,"onSuccessSession :"+result);

        //3. send partner data to Genie services

        if(D)
            Log.d(TAG,isChildLevel+" isChildLevel, partnerData==>"+hashMapDataMain);

        partnerDataResponseHandler=new PartnerDataResponseHandler(this);
        partner.sendData(configModel.getPartnerId(), hashMapDataMain, partnerDataResponseHandler);

    }

    @Override
    public void onFailureSession(GenieResponse genieResponse) {
        String result=new Gson().toJson(genieResponse.getResult());
        if(D)
            Log.d(TAG,"onFailureSession :"+result);
        progressBar.setVisibility(View.GONE);
        scrollViewChildContainer.setVisibility(View.VISIBLE);
        Register_btn.setVisibility(View.VISIBLE);

    }

    @Override
    public void onSuccessPartner(GenieResponse genieResponse) {
        String result=new Gson().toJson(genieResponse.getResult());

        if(D)
            Log.d(TAG, "onSuccessPartner :" + result);
        progressBar.setVisibility(View.GONE);
         Util.showToastmessage(mContext, ""+getString(R.string.data_success));
       //4  exit the app
        if(isChildLevel){
            progressBar.setVisibility(View.GONE);
            scrollViewChildContainer.setVisibility(View.VISIBLE);
            Register_btn.setVisibility(View.VISIBLE);
            displayLayout.removeAllViews();
            displayForm();
            hashMapDataMain.clear();}
        else
            ((MainActivity)mContext).switchContent(new LevelFragment(),2,true,"LevelFragment");





    }

    @Override
    public void onFailurePartner(GenieResponse genieResponse) {
        String result=new Gson().toJson(genieResponse.getResult());
        if(D)
            Log.d(TAG,"onFailurePartner :"+result);
        progressBar.setVisibility(View.GONE);
        scrollViewChildContainer.setVisibility(View.VISIBLE);
        Register_btn.setVisibility(View.VISIBLE);

    }




    @Override
    public void onDetach() {
        super.onDetach();
        if(D)
            Log.d(TAG,"onDetach");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(D)
            Log.d(TAG,"onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(D)
            Log.d(TAG, "onDestroy partner--->" + partner);


    }

    @Override
    public void onStop() {
        super.onStop();
        if(D)
            Log.d(TAG, "onStop :userProfile->" + userProfile + " partner--> " + partner);
    }


}
