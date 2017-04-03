package org.partner.callback;

import android.support.annotation.Nullable;

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
}
