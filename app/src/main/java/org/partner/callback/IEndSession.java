package org.partner.callback;

import org.ekstep.genieservices.sdks.response.GenieResponse;

/**
 * Created by Jaya on 10/5/2015.
 */
public interface IEndSession {

    public void onSuccessEndSession(GenieResponse genieResponse);
    public void  onFailureEndSession(GenieResponse genieResponse);
}
