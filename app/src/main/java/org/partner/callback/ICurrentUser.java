package org.partner.callback;

import org.ekstep.genieservices.sdks.response.GenieResponse;

/**
 * Created by Jaya on 10/5/2015.
 */
public interface ICurrentUser {

    public void onSuccessCurrentUser(GenieResponse genieResponse);
    public void  onFailureCurrentUser(GenieResponse genieResponse);
}
