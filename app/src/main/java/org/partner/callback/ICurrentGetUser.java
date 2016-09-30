package org.partner.callback;

import org.ekstep.genieservices.sdks.response.GenieResponse;

/**
 * Created by Jaya on 10/5/2015.
 */
public interface ICurrentGetUser {

    public void onSuccessCurrentGetUser(GenieResponse genieResponse);
    public void  onFailureCurrentGetUser(GenieResponse genieResponse);
}
