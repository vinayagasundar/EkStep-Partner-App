package org.partner;

import android.app.Application;



/**
 * Created by Jaya on 9/24/2015.
 */
public class PartnerApp extends Application {
    public static final boolean DEBUG=false;

    private long startTime;

    public long getStartTime() {
        return startTime;
    }
}
