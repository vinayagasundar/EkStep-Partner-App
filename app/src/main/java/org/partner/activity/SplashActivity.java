package org.partner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;

import org.ekstep.genieservices.sdks.Partner;
import org.ekstep.genieservices.sdks.response.GenieResponse;
import org.partner.BuildConfig;
import org.partner.R;
import org.partner.Util.AppConstants;
import org.partner.Util.Utils;
import org.partner.callback.IRegister;
import org.partner.callback.RegisterResponseHandler;


/**
 * Splash screen to initialize the Partner
 */
public class SplashActivity extends AppCompatActivity
        implements IRegister {

    private static final String TAG = "SplashActivity";

    private static final boolean DEBUG = BuildConfig.DEBUG;


    /**
     * Partner used to register the Partner Info to Genie Service
     */
    private Partner mPartner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        boolean isGenieInstalled = Utils.isAppInstalled(this,
                AppConstants.GENIE_SERVICES_PACKAGE_NAME);

        if (isGenieInstalled) {
            registerPartnerApp();
        } else {
            // Handle the failure

            if (DEBUG) {
                Log.e(TAG, "onCreate: Genie Service Not installed on Device");
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (DEBUG) {
            Log.i(TAG, "onStop: ");
        }

        if (mPartner != null) {
            mPartner.finish();
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // Callback from RegisterResponseHandler
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void onSuccess(GenieResponse genieResponse) {
        String result = new Gson().toJson(genieResponse.getResult());

        if (DEBUG) {
            Log.i(TAG, "onSuccess: Registration Successful : " + result);
        }

        moveToMainScreen();
    }

    @Override
    public void onFailure(GenieResponse genieResponse) {
        String result = new Gson().toJson(genieResponse);

        if (DEBUG) {
            Log.e(TAG, "onFailure: Partner App Registration Failed " + result);
        }

        Utils.processPartnerRegFailure(this, genieResponse);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }



    /**
     * Register the Partner with Genie Service
     */
    private void registerPartnerApp() {
        if (DEBUG) {
            Log.i(TAG, "registerPartnerApp: ");
        }

        mPartner = new Partner(this);
        RegisterResponseHandler registerResponseHandler = new RegisterResponseHandler(this);

        mPartner.register(AppConstants.PARTNER_ID, AppConstants.PARTNER_PUBLIC_KEY,
                registerResponseHandler);
    }


    /**
     * Move to MainActivity and kill this Activity after 2 seconds
     */
    private void moveToMainScreen() {
        if (DEBUG) {
            Log.d(TAG, "moveToMainScreen: ");
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LandingActivity.class);
                startActivity(intent);

                finish();
            }
        }, 2000);
    }


}
