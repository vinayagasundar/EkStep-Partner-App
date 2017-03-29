package org.partner.Util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import org.ekstep.genieservices.sdks.response.GenieResponse;
import org.partner.R;

/**
 * Common utils methods will available here
 *
 * @author vinayagasundar
 */

public final class Utils {


    /**
     * Check given package is Installed or Not
     *
     * @param context     Context to initialize the package managaer
     * @param packageName Package Name which need to be check
     * @return {@code true} if the packageName is installed on the device
     * otherwise {@code false}
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        boolean installed;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }


    /**
     * Handle the failure message for Partner Registration
     * @param context Context used to access string & show Toast
     * @param object instance of the genie response object
     */
    public static void processPartnerRegFailure(Context context, Object object) {
        GenieResponse response = (GenieResponse) object;
        String error = response.getError();

        Log.e("PartnerApp", error);


        for (int i = 0; i < response.getErrorMessages().size(); i++) {
            String errorPos = response.getErrorMessages().get(i);
            Log.e("PartnerApp", errorPos);
        }
        if (response.getResult() != null) {
            Log.e("PartnerApp", response.getResult().toString());
        }

        if (error.equalsIgnoreCase(context.getString(R.string.MISSING_PARTNER_ID))) {
            Toast.makeText(context, context.getString(R.string.MISSING_PARTNER_ID_MSG),
                    Toast.LENGTH_LONG).show();
        } else if (error.equalsIgnoreCase(context.getString(R.string.MISSING_PUBLIC_KEY))) {
            Toast.makeText(context, context.getString(R.string.MISSING_PUBLIC_KEY_MSG),
                    Toast.LENGTH_LONG).show();
        } else if (error.equalsIgnoreCase(context.getString(R.string.INVALID_RSA_PUBLIC_KEY))) {
            Toast.makeText(context, context.getString(R.string.INVALID_RSA_PUBLIC_KEY_MSG),
                    Toast.LENGTH_LONG).show();
        } else if (error.equalsIgnoreCase(context.getString(R.string.INVALID_DATA))) {
            Toast.makeText(context, context.getString(R.string.INVALID_DATA_MSG),
                    Toast.LENGTH_LONG).show();
        }

    }
}
