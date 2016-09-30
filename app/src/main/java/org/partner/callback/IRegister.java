package org.partner.callback;

import org.ekstep.genieservices.sdks.response.GenieResponse;

/**
 * Created by Jaya on 10/5/2015.
 */
public interface IRegister {

    public void onSuccess(GenieResponse genieResponse);
    public void  onFailure(GenieResponse genieResponse);
}
