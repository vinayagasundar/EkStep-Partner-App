package org.partner.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import org.partner.model.Config;
import org.partner.model.ConfigModel;
import org.partner.model.FieldModel;
import org.partner.model.FieldValue;
import org.partner.model.InstructionsField;
import org.partner.model.SectionValue;
import org.partner.model.ValidationField;
import org.ekstep.genieservices.sdks.Partner;
import org.ekstep.genieservices.sdks.response.GenieResponse;

import org.partner.R;
import org.partner.Util.Util;
import org.partner.callback.IRegister;
import org.partner.callback.RegisterResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

/*
* Splashscreeen will show logo of Partner App(Akshara)
* Automatic will register to partner
* */
public class Splashscreeen extends Activity implements IRegister{
    private boolean D= Util.DEBUG;
    private  String TAG=Splashscreeen.class.getSimpleName();
    private  Context mContext;
    private  Partner partner;
    private  RegisterResponseHandler responseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);
        mContext=Splashscreeen.this;
        isPartnerRegistered();

    }




    private void isPartnerRegistered(){
        if(isAppInstalled(Util.GENIE_SERVICES_PACKAGENAME,mContext)) {
            ConfigModel configModel=new Util(mContext).getConfigModel();
            if(D)
                Log.d(TAG,"configModel:"+configModel);
                if(configModel==null)
                readConfigFile();
               else
               registerPartner(configModel);


           }else {
            Toast.makeText(mContext, getString(R.string.Genie_service_Check), Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            },2*1000);

        }





    }

    private void registerPartner(ConfigModel configModel ){
        partner=new Partner(Splashscreeen.this);
        responseHandler = new RegisterResponseHandler(this);
        partner.register(configModel.getPartnerId(), configModel.getPartnerPublicKey(), responseHandler);

    }




    @Override
    public void onSuccess(GenieResponse genieResponse) {
        String result = new Gson().toJson(genieResponse.getResult());
         if(D)
             Log.d(TAG, "onSuccess GenieResponse :" + result);
        moveTonextScreen();

    }

       private void moveTonextScreen(){
           if(D)
               Log.d(TAG, "moveTonextScreen :");

           new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splashscreeen.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        }, 2 * 1000);

    }
    public String loadConfigFileFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("config.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    private void readConfigFile(){
        if(D)
            Log.d(TAG,"readConfigFile==>");

        new AsyncTask<Void,Void,Boolean>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                }
            @Override
            protected Boolean doInBackground(Void... voids) {
                boolean result=false;
                try {
                    String jsonData=loadConfigFileFromAsset();
                    if(D)
                        Log.d(TAG,"jsonData==>"+jsonData);

                    JSONObject jsonObject=new JSONObject(jsonData);
                    if(D)
                        Log.d(TAG,"jsonObject==>"+jsonObject);

                    String partnerName=jsonObject.getString(Config.partnerName);
                    String partnerId=jsonObject.getString(Config.partnerId);
                    String partnerPublicKey=jsonObject.getString(Config.partnerPublicKey);
                    if(partnerName.isEmpty()){
                        displayToast(getString(R.string.Partner_empty));
                    }else if(partnerId.isEmpty()){
                        displayToast(getString(R.string.partnerId_empty));
                    }else if(partnerPublicKey.isEmpty()){
                        displayToast(getString(R.string.partnerPublicKey_empty));
                     }else{
                        ConfigModel configModel=new ConfigModel();
                        configModel.setPartnerName(partnerName);
                        configModel.setPartnerId(jsonObject.getString(Config.partnerId));
                        configModel.setPartnerPublicKey(jsonObject.getString(Config.partnerPublicKey));

                        JSONArray sectionsArray=jsonObject.getJSONArray(Config.sections);
                        int size=sectionsArray.length();

                        if(D)
                            Log.d(TAG,"size==>"+size);

                        SectionValue[] sectionValues=new SectionValue[size];
                        InstructionsField[] instructionsFields=new InstructionsField[size];
                        for (int i=0;i<size;i++){
                            sectionValues[i]=new SectionValue();
                            JSONArray partnerFieldsArray=sectionsArray.getJSONArray(i);
                            if(D)
                                Log.d(TAG,"partnerFieldsArray==>"+partnerFieldsArray);

                            int length=partnerFieldsArray.length();
                            if(D)
                                Log.d(TAG,"length==>"+length);


                            //FieldModel[] fieldModels=new FieldModel[length-1];
                            //Adding Instructions inside fieldModel
                            FieldModel[]  fieldModels=new FieldModel[length];
                            FieldValue[] fieldValue=new FieldValue[length-1];
                            ValidationField[] validationField=new ValidationField[length-1];
                            JSONObject fieldObject=partnerFieldsArray.getJSONObject(0);
                            if(D)
                                Log.d(TAG,"fieldObject  at pos zero==>"+fieldObject);

                            //if(j==0){ //1st position fixed as sectionHeadings that's why adding displayOrder

                            //1st position fixed as sectionHeadings
                            sectionValues[i].setSectionHeading(fieldObject.getString(Config.SectionHeading));
                            //Adding Instructions inside fieldModel
                            JSONObject instructionsObject=fieldObject.getJSONObject(Config.instructions);
                            int displayOrder=instructionsObject.getInt(Config.displayOrder);
                            if(D)
                                Log.d(TAG,"Instructions displayOrder==>"+displayOrder);




                            int instructionRow=0;
                            for (int j=0;j<length-1;j++){
                                if(D)
                                    Log.d(TAG,"fieldModels at pos :..........>"+j);
                                fieldModels[j]=new FieldModel();
                                fieldObject=partnerFieldsArray.getJSONObject(j+1);
                                if(D)
                                    Log.d(TAG,"fieldObject  partnerFields==>"+fieldObject);

                                fieldModels[j].setFieldName(fieldObject.getString(Config.fieldName));
                                fieldModels[j].setFieldHint(fieldObject.getString(Config.fieldHint));
                                fieldModels[j].setDisplayOrder(fieldObject.getInt(Config.displayOrder));

                                //fieldValue
                                fieldValue[j]=new FieldValue();
                                JSONObject fieldValueObject=fieldObject.getJSONObject(Config.fieldvalue);
                                fieldValue[j].setFieldType(fieldValueObject.getString(Config.fieldType));
                                fieldValue[j].setFieldInputType(fieldValueObject.getString(Config.fieldInputType));
                                //Validation
                                validationField[j]=new ValidationField();
                                JSONObject validationObject=fieldValueObject.getJSONObject(Config.validation);
                                validationField[j].setFlag(validationObject.getBoolean(Config.flag));
                                validationField[j].setMinimum(validationObject.getInt(Config.minimum));
                                validationField[j].setMaximum(validationObject.getInt(Config.maximum));
                                fieldValue[j].setValidation(validationField[j]);

                                fieldModels[j].setFieldValue(fieldValue[j]);

                                //fieldValues
                                JSONArray fieldValues=fieldObject.getJSONArray(Config.fieldValues);
                                int fieldValuesSize=fieldValues.length();
                                if(fieldValuesSize>0){
                                    ArrayList field_Value=new ArrayList();
                                    for (int pos=0;pos<fieldValuesSize;pos++){
                                        field_Value.add(fieldValues.getString(pos));
                                    }
                                    fieldModels[j].setFieldValues(field_Value);
                                } //end of if






                            }//end of inner for-loop
                            FieldModel[]fieldModel=null;
                            String title=instructionsObject.getString(Config.title);
                            if(!title.isEmpty()){
                                if(D)
                                    Log.d(TAG,"fieldModels at pos :..........>"+(length-1));

                                fieldModels[length-1]=new FieldModel();
                                fieldModels[length-1].setFieldName("instructions");
                                fieldModels[length-1].setFieldHint(instructionsObject.getString(Config.title));
                                fieldModels[length-1].setDisplayOrder(displayOrder);
                                fieldModel=new FieldModel[length];
                            }else
                                fieldModel=new FieldModel[length-1];

                            int fieldSize=fieldModel.length;
                            if(D)
                                Log.d(TAG,"-----------------fieldSize----------------->"+fieldSize);



                            //Sorting Field based on displayorder

                            for ( int k=0;k<fieldSize;k++) {
                                if(D)
                                    Log.d(TAG,"Displaying fieldModels at pos :..........>"+k);

                                if(D)
                                    Log.d(TAG,k+" the value of K ...fieldModel1.getFieldName()=============>"+fieldModels[k].getFieldName()+" getDisplayOrder :"+fieldModels[k].getDisplayOrder());

                                if(fieldModels[k].getFieldName().equalsIgnoreCase("instructions")){
                                    fieldModel[k]=new FieldModel(fieldModels[k].getFieldName(),
                                            null,
                                            fieldModels[k].getFieldHint(),
                                            fieldModels[k].getDisplayOrder(),
                                            null                                    );

                                }else{
                                    fieldModel[k]=new FieldModel(fieldModels[k].getFieldName(),
                                            fieldModels[k].getFieldValue(),
                                            fieldModels[k].getFieldHint(),
                                            fieldModels[k].getDisplayOrder(),
                                            fieldModels[k].getFieldValues()
                                    );

                                }




                            }


                            if(D)
                                Log.d(TAG,"after adding fields .fieldModel.length**********=============>"+fieldModel.length);


                            Arrays.sort(fieldModel);
                            sectionValues[i].setFieldModels(fieldModel);




                        }//end of outer for-loop

                        configModel.setSectionValues(sectionValues);
                        new Util(mContext).storeConfigModel(configModel);
                        result=true;
                    }




                }catch (JSONException jsonException){
                    if(D)
                        Log.e(TAG,"jsonException"+jsonException);
                }

                return result;
            }
            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if(result)
                    registerPartner(new Util(mContext).getConfigModel());
                  else
                    finish();
            }
        }.execute();
    }

    @Override
    public void onFailure(GenieResponse genieResponse) {
        String result = new Gson().toJson(genieResponse.getResult());
        Util.processSendFailure(Splashscreeen.this, genieResponse);
        if(D)
            Log.d(TAG," onFailureGenieResponse :"+result+" ,getErrorMessages"+genieResponse.getErrorMessages());
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Splashscreeen.this.finish();

            }
        });



    }


    public static boolean isAppInstalled(String packageName,Context context) {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    @Override
    protected void onStop() {
        if(D)
            Log.d(TAG,"onStop");
        super.onStop();
        if(partner!=null)
        partner.finish();
    }

    private  void displayToast(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Util.showToastmessage(Splashscreeen.this,msg);
            }
        });
    }




}
