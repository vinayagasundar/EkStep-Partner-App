package org.partner.callback;

import org.ekstep.genieservices.sdks.response.GenieListResponse;
import org.ekstep.genieservices.sdks.response.GenieResponse;

/**
 * Created by Jaya on 10/5/2015.
 */
public interface IUserProfile {

    public void onSuccessUserProfile(GenieListResponse genieListResponse);
    public void  onFailureUserprofile(GenieListResponse genieListResponse);
}
