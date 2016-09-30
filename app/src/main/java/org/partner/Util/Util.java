package org.partner.Util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.partner.R;
import org.partner.model.ConfigModel;
import org.ekstep.genieservices.sdks.Partner;
import org.ekstep.genieservices.sdks.Telemetry;
import org.ekstep.genieservices.sdks.UserProfile;
import org.ekstep.genieservices.sdks.response.GenieResponse;

import java.io.File;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;



/**
 * Created by Jaya on 9/24/2015.
 */
public class Util extends Application {
    public static final boolean DEBUG=false;
    private static String TAG =Util.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context mContext;
    public static final String CONFIGMODEL_DATA="configModel";
    private String PARTNER_REG="partnerreg";
    public static final String GENIE_SERVICES_PACKAGENAME="org.ekstep.genieservices";
    public static  final String GENIE_PACKAGENAME="org.ekstep.android.genie";
    private long startTime;
    private String language;
    private Partner partner;
    private Telemetry telemetry;
    private UserProfile userProfile;
    private ArrayList<String>uidProfile=new ArrayList<>();
    private ArrayList<String>handleProfile=new ArrayList<>();
    public static final String sid="5c833841-6d67-48cf-93cd-9765cb4df4fa",uid="589e8bae-6ddf-41f0-bbd2-7d7bf24e3070";

    public ArrayList<String> getUidProfile() {
        return uidProfile;
    }

    public void setUidProfile(ArrayList<String> uidProfile) {
        this.uidProfile = uidProfile;
    }

    public ArrayList<String> getHandleProfile() {
        return handleProfile;
    }

    public void setHandleProfile(ArrayList<String> handleProfile) {
        this.handleProfile = handleProfile;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public Telemetry getTelemetry() {
        return telemetry;
    }

    public void setTelemetry(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    public  static final  String UID="uid";

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }


    public Util(){}
    public Util(Context context){
        this.mContext=context;
        sharedPreferences=mContext.getSharedPreferences("partner",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }
    public void storeConfigModel(ConfigModel configModel){
        // Convert the object to a JSON string
        String json = new Gson().toJson(configModel);
        if(DEBUG)
            Log.d(TAG,"storeConfigModel json==>"+json);
        //Strore CONFIGMODEL_DATA
        editor.putString(CONFIGMODEL_DATA, json);
        editor.commit();
    }
    public  ConfigModel getConfigModel(){
        ConfigModel configModel=null;
        // Now convert the JSON string back to your java object
        Type type = new TypeToken<ConfigModel>(){}.getType();
        String json=sharedPreferences.getString(CONFIGMODEL_DATA, "NOTHING");
       /* if(DEBUG)
            Log.d(TAG,"getConfigModel json==>"+json);
       */ if(!json.equals("NOTHING"))
            configModel = new Gson().fromJson(json, type);

        /*if(DEBUG)
            Log.d(TAG,"configModel:"+configModel);
*/
        return configModel;
    }





    public  void storePartnerRegistration(String partnerid){
        editor.putString(PARTNER_REG, partnerid);
        editor.commit();
    }
    public void clearSharedPreferences(){
        //clear sharedPreferences
        editor.clear();
        editor.commit();

    }


    public boolean isRegisterPartner(){
        if(!(sharedPreferences.getString(PARTNER_REG,"NOTHING").equals("NOTHING")))
            return   true;
        else
            return   false;

    }


    public static String generateUniqueId(){
        String sha1id = null;
        String timeStamp =String.valueOf(System.currentTimeMillis());
        String id= UUID.randomUUID().toString()+timeStamp;

        try {
            sha1id=sha1(id);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sha1id;

    }
    public static String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }



    public static void showToastmessage(Context context,String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /* To hide keyboard */
    public static void hideKeyboard(Activity activity,Context context) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);


        }
    }
    /* To show keyboard */
    public static void showKeyboard(Activity activity,Context context) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

       /* if (activity.getCurrentFocus() != null) {
           *//* InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInputFromInputMethod(activity.getCurrentFocus().getWindowToken(), 0);
           *//* activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        }*/
    }

    public static void processSuccess(Context context ,Object object){
        GenieResponse response = (GenieResponse) object;
        Log.i("Successfull", response.getStatus());
        if(response.getResult()!=null){
            Log.i("Success Gson Response", response.getResult().toString());
        }

    }
    public static void processSendFailure(Context context ,Object object){
        GenieResponse response = (GenieResponse)object;
        String error = response.getError();
        Log.e("Genie Service Error Log",error);
        for(int i=0;i<response.getErrorMessages().size();i++){
            String errorPos=response.getErrorMessages().get(i);
            Log.e("Error info",errorPos);
        }
        if(response.getResult()!=null){
            Log.e("Failure Gson Response", response.getResult().toString());
        }

        if(error.equalsIgnoreCase(context.getResources().getString(R.string.MISSING_PARTNER_ID))){
            Toast.makeText(context, context.getResources().getString(R.string.MISSING_PARTNER_ID_MSG), Toast.LENGTH_LONG).show();
        }
        else if(error.equalsIgnoreCase(context.getResources().getString(R.string.MISSING_PUBLIC_KEY))){
            Toast.makeText(context, context.getResources().getString(R.string.MISSING_PUBLIC_KEY_MSG), Toast.LENGTH_LONG).show();
        }
        else if(error.equalsIgnoreCase(context.getResources().getString(R.string.INVALID_RSA_PUBLIC_KEY))){
            Toast.makeText(context, context.getResources().getString(R.string.INVALID_RSA_PUBLIC_KEY_MSG), Toast.LENGTH_LONG).show();
        }
        else if(error.equalsIgnoreCase(context.getResources().getString(R.string.INVALID_DATA))){
            Toast.makeText(context, context.getResources().getString(R.string.INVALID_DATA_MSG), Toast.LENGTH_LONG).show();
        }

    }
    public static String getPackageName(Context context){
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String version = pInfo.packageName;
        return version;

    }
    public static String getVersion(Context context){
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String version = pInfo.versionName+"."+pInfo.versionCode;
        return version;

    }
   // public static long getStartTime
}
