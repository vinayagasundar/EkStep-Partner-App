package org.partner.callback;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Update the Fragment in the MainActivity
 * @author vinayagasundar
 */

public interface LaunchFragmentCallback {

    /**
     * Switch the current fragment with given fragment
     * @param fragmentId id of the Fragment
     * @param uid optional UID of the child
     */
    void switchFragment(int fragmentId, @Nullable String uid);


    /**
     * It'll directly launch the fragment
     * @param fragment instance of the fragment
     * @param addToBackStack flag to indicate do we need add it into Back stack or not
     */
    void launchFragment(@NonNull Fragment fragment, boolean addToBackStack);
}
