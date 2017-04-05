package org.partner.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import org.ekstep.genieservices.sdks.response.GenieResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.partner.R;
import org.partner.Util.TelemetryEventGenertor;
import org.partner.Util.Util;
import org.partner.callback.ILanguage;
import org.partner.callback.ITelemetryData;
import org.partner.callback.IUserProfile;
import org.partner.callback.LanguageResponseHandler;
import org.partner.callback.TelemetryResponseHandler;
import org.partner.callback.UserProfileResponseHandler;
import org.partner.fragment.DisplayChildProfileFragment;
import org.ekstep.genieservices.sdks.LanguageList;
import org.ekstep.genieservices.sdks.Partner;
import org.ekstep.genieservices.sdks.Telemetry;
import org.ekstep.genieservices.sdks.UserProfile;
import org.ekstep.genieservices.sdks.response.GenieListResponse;
import org.partner.fragment.LevelFragment;
import org.partner.model.ConfigModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity implements NavigationDrawer.FragmentDrawerListener,
        ITelemetryData,IUserProfile {
    private boolean D= Util.DEBUG;
    private String TAG=MainActivity.class.getSimpleName();
    private Fragment fragment;
    private Context mContext;
    private Toolbar mToolbar;
    private TextView mToolbar_text;
    private NavigationDrawer mDrawerFragment;
    private ImageView mToolbar_menu;
    private Fragment mFragment;
    private FragmentManager mFragment_mgr;
    private DrawerLayout mDrawerLayout;
    private MaterialDialog materialDialog;
    private int writeDB=0;
    private Partner partner;
    private Telemetry telemetry;
    private UserProfile userProfile;
    private LanguageList languageList;
    private UserProfileResponseHandler userProfileResponseHandler;
    private TelemetryResponseHandler telemetryResponseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(D)
            Log.d(TAG,"onCreate");
        setContentView(R.layout.activity_home);

        mFragment_mgr = getSupportFragmentManager();
        if (savedInstanceState != null) {
            mFragment = getSupportFragmentManager().findFragmentByTag("mContent");
        }

        initWidget();




    }

    @Override
    protected void onResume() {
        super.onResume();
        if(D)
            Log.d(TAG,"onResume");

    }

    @Override
    protected void onRestart() {
        if(D)
            Log.d(TAG,"onRestart");
        super.onRestart();
        initWidget();
    }

    private  void  initWidget() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        mToolbar_text = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        mToolbar_menu=(ImageView)mToolbar.findViewById(R.id.imageView_menu);
        mDrawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        mContext = MainActivity.this;
        mDrawerFragment = (NavigationDrawer)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mDrawerFragment.setUp(R.id.fragment_navigation_drawer, mDrawerLayout, mToolbar);
        mDrawerFragment.setDrawerListener(this);
         mFragment_mgr=getSupportFragmentManager();

        ConfigModel configModel=new Util(mContext).getConfigModel();
        String partnerName=configModel.getPartnerName();
       // Log.e(TAG, "--------partnerName-->" + partnerName);
        mToolbar_text.setText(partnerName);

        //initialitize the partner
        partner=new Partner(MainActivity.this);
        userProfile=new UserProfile(MainActivity.this);
        telemetry=new Telemetry(MainActivity.this);
        //Store object
        Util util=(Util)mContext.getApplicationContext();
        util.setPartner(partner);
        util.setUserProfile(userProfile);
        util.setTelemetry(telemetry);
        util.setStartTime(System.currentTimeMillis());
        /*telemetryResponseHandler=new TelemetryResponseHandler(MainActivity.this);
        telemetry.send(TelemetryEventGenertor.generateOEStartEvent(mContext).toString(), telemetryResponseHandler);
*/

        userProfileResponseHandler=new UserProfileResponseHandler(MainActivity.this);
        userProfile.getAllProfiles(userProfileResponseHandler);



    }




    @Override
    public void onDrawerItemSelected(View view, int position) {
        showMaterialDialog("Are you sure to exit?", "", "YES", "CANCEL");
        materialDialog.show();
    }

    private void displayView(int position) {
        // Fragment fragment;

        switch (position) {
            case 0:
                 fragment = new LevelFragment();
                switchContent(fragment, 1, true,"LevelFragment");
                break;


        }

    }

    public void switchContent(Fragment fragment, int id, boolean addToBackStack,String fragmentName) {
        this.mFragment = fragment;
        FragmentTransaction fragmentTransaction=mFragment_mgr.beginTransaction();
        Fragment previouFragment=mFragment_mgr.findFragmentByTag(fragmentName);
        if(D)
            Log.d(TAG,"switchContent fragmentName:"+fragmentName);
       ListIterator<Fragment> fragmentList= getSupportFragmentManager().getFragments().listIterator();
        boolean isFragmentAdded=false;
        while (fragmentList.hasNext()){
            Fragment fragment1=(Fragment)fragmentList.next();
            Log.d(TAG, "switchContent fragment:" + fragment1);
                if(fragment1==fragment ){
                    isFragmentAdded=true;
                   fragmentTransaction.replace(R.id.container_body, fragment ).commitAllowingStateLoss();
                    break;
                }


        }

            if(!isFragmentAdded)
            fragmentTransaction.replace(R.id.container_body, fragment,null ).addToBackStack(null).commitAllowingStateLoss();


      /*
        // If fragment doesn't exist yet, create one
        if (previouFragment == null) {
            fragmentTransaction.add(R.id.container_body, fragment, fragmentName).addToBackStack(fragmentName).commitAllowingStateLoss();
        }
        else { // re-use the old fragment
            fragmentTransaction.replace(R.id.container_body, previouFragment).addToBackStack(fragmentName).commitAllowingStateLoss();
        }
*/

       /* if(previouFragment!=null){
            fragmentTransaction.remove(previouFragment);
         }
        if (addToBackStack) {
            fragmentTransaction.replace(R.id.container_body, fragment,null ).addToBackStack(fragmentName).commitAllowingStateLoss();

        } else {
            fragmentTransaction.replace(R.id.container_body, fragment).commitAllowingStateLoss();
        }
       */


       /* if(isFragmentInBackstack(mFragment_mgr,fragmentName)){
            fragmentTransaction.replace(R.id.container_body, fragment).commitAllowingStateLoss();
         } else {
            fragmentTransaction.replace(R.id.container_body, fragment,null ).addToBackStack(fragmentName).commitAllowingStateLoss();
        }

*/
    }
    public  boolean isFragmentInBackstack(final FragmentManager fragmentManager, final String fragmentTagName) {
        int fragmentSize=fragmentManager.getBackStackEntryCount();
        Log.d("MainActivity","fragmentSize==>"+fragmentSize);
        for (int entry = 0; entry <fragmentSize ; entry++) {

                Log.d("MainActivity",entry+"==>entry , frag======>"+fragmentManager.getBackStackEntryAt(entry).getName());
            if (fragmentTagName.equals(fragmentManager.getBackStackEntryAt(entry).getName())) {
                return true;
            }
        }
        return false;
    }
   /*
   java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
    1. Overide onSaveInstanceState
    2. commitAllowingStateLoss on frgament transaction(getSupportFragmentManager)
   * */
    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onBackPressed() {

        if(D)
            Log.d(TAG,"onBackPressed:"+getSupportFragmentManager().getBackStackEntryCount()+",mFragment:"+mFragment+",getFragments:"+ getSupportFragmentManager().getFragments());
        if (getSupportFragmentManager().getBackStackEntryCount()>1) {
            getSupportFragmentManager().popBackStack();
            if(mFragment instanceof LevelFragment){
                exitApp();}
           /* else{
            mToolbar_menu.setVisibility(View.VISIBLE);
            getSupportFragmentManager().popBackStack();}
       */ }else if(getSupportFragmentManager().getBackStackEntryCount()==1){
            MainActivity.this.finish();
        }
    }

    private void showMaterialDialog(String title,String content, final String positiveText, String negativeText) {
        materialDialog=new MaterialDialog
                .Builder(MainActivity.this)
                .title(title)
                .content(content)
                .negativeText(negativeText)
                .positiveText(positiveText)
                .buttonsGravity(GravityEnum.CENTER)
                .autoDismiss(false)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                        //exit appln
                        exitApp();


                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        dialog.dismiss();
                    }
                }).build();
        materialDialog.setCancelable(false);
    }

    public void showBackIcon(String title,Fragment fragment){
        mToolbar_menu.setVisibility(View.GONE);
        mToolbar_text.setText(title);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        this.mFragment=fragment;
    }
    public void showTitle(String title,Fragment fragment){
        mToolbar_menu.setVisibility(View.VISIBLE);
        mToolbar_text.setText(title);

    }



    public  boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            //Toast.makeText(MainActivity.this, packageName, Toast.LENGTH_LONG).show();
            Intent intent = manager.getLaunchIntentForPackage(packageName);
            if (intent == null) {
                return false;
                //throw new PackageManager.NameNotFoundException();
            }
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            context.startActivity(intent);
            finish();
            return true;
        } catch (Exception e) {

            return false;

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(D)
            Log.d(TAG, "onStop");
        closeAllData();
    }


    public  void exitApp(){
        if(D)
            Log.d(TAG,"exitApp");
        Intent intent=new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

       // MainActivity.this.finish();
       // openApp(MainActivity.this, Util.GENIE_PACKAGENAME);

    }

  @Override
    protected void onDestroy() {
      super.onDestroy();
        if (D)
            Log.d(TAG,"onDestroy: userProfile->"+userProfile+" partner-->"+partner);
      //closeAllData();

    }

    private void closeAllData(){
        if(userProfile!=null)
            userProfile.finish();
        if (partner!=null)
            partner.finish();

        if (telemetry!=null)
            telemetry=null;

       // android.os.Process.killProcess(android.os.Process.myPid());



    }

    @Override
    public void onSuccessTelemetry(GenieResponse genieResponse) {
        String result=new Gson().toJson(genieResponse.getResult());

        if(D)
            Log.d(TAG, " onSuccessTelemetry  result:" + result);
        userProfileResponseHandler=new UserProfileResponseHandler(MainActivity.this);
        userProfile.getAllProfiles(userProfileResponseHandler);




    }

    @Override
    public void onFailureTelemetry(GenieResponse genieResponse) {
        if(D)
            Log.d(TAG," onFailureTelemetry :"+genieResponse);
        Util.showToastmessage(MainActivity.this, genieResponse.getError());

    }

    @Override
    public void onSuccessUserProfile(GenieListResponse genieListResponse) {
        if(D)
            Log.d(TAG," onSuccessUserProfile :"+genieListResponse.getResults());
        ArrayList<String> uids=new ArrayList();
        ArrayList<String> handles=new ArrayList();

        JSONArray jsonArray=new JSONArray(genieListResponse.getResults());
        for(int i=0;i<jsonArray.length();i++){
            try {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                uids.add(jsonObject.getString("uid"));
                handles.add(jsonObject.getString("handle"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Util util=(Util)getApplicationContext();
        util.setUidProfile(uids);
        util.setHandleProfile(handles);
        displayView(0);

    }

    @Override
    public void onFailureUserprofile(GenieListResponse genieListResponse) {
        if(D)
            Log.d(TAG," onFailureUserProfile :"+genieListResponse);
        Util.showToastmessage(MainActivity.this, genieListResponse.getError());

    }
}
