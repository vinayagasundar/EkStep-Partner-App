package org.partner.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import org.partner.BuildConfig;


/**
 * Preference Util for the App
 * @author vinayagasundar
 */

public final class PrefUtil {

    private static final String APP_PREFERENCES = BuildConfig.APPLICATION_ID + ".APP_PREFS";


    private static SharedPreferences mAppPrefs;

    private PrefUtil() {

    }


    /**
     * Initialize the Preference from the {@link Application#onCreate()} method
     * @param context context to initialize the Preference
     */
    public static void init(Context context) {
        if (mAppPrefs == null) {
            mAppPrefs = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        }
    }


    /**
     * Store the string value in the preferences
     * @param key Key for the preferences
     * @param value value to be stored in the preferences
     */
    public static void storeString(String key, String value) {
        mAppPrefs.edit()
                .putString(key, value)
                .apply();
    }

    /**
     * Get the value from the preference for given key
     * @param key Key for which we need to get value from the preferences
     * @return if the value exist it'll return the value otherwise null
     */
    public static String getString(String key) {
        return mAppPrefs.getString(key, null);
    }

}
