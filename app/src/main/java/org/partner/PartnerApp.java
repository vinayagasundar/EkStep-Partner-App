package org.partner;

import android.app.Application;

import org.partner.database.PartnerDatabaseHandler;
import org.partner.util.PrefUtil;


/**
 * Created by Jaya on 9/24/2015.
 */
public class PartnerApp extends Application {
    public static final boolean DEBUG=false;


    @Override
    public void onCreate() {
        super.onCreate();

        PartnerDatabaseHandler.init(this);

        PrefUtil.init(this);
    }

    private long startTime;

    public long getStartTime() {
        return startTime;
    }
}
