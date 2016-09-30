package org.partner.callback;

import org.ekstep.genieservices.sdks.response.GenieResponse;

/**
 * Created by Jaya on 10/5/2015.
 */
public interface IStartSession {

    public void onSuccessSession(GenieResponse genieResponse);
    public void  onFailureSession(GenieResponse genieResponse);
}
