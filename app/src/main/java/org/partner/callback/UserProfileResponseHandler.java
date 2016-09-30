package org.partner.callback;

import org.ekstep.genieservices.sdks.response.GenieListResponse;
import org.ekstep.genieservices.sdks.response.GenieResponse;
import org.ekstep.genieservices.sdks.response.IListResponseHandler;
import org.ekstep.genieservices.sdks.response.IResponseHandler;

public class UserProfileResponseHandler implements IListResponseHandler {
    private IUserProfile mIUserProfile = null;

    public UserProfileResponseHandler(IUserProfile userProfile) {
        mIUserProfile = userProfile;
    }
 @Override
    public void onSuccess(GenieListResponse genieListResponse) {
        // Code to handle success scenario
        mIUserProfile.onSuccessUserProfile(genieListResponse);
    }

    @Override
    public void onFailure(GenieListResponse genieListResponse) {
        // Code to handle error scenario
        mIUserProfile.onFailureUserprofile(genieListResponse);
    }
}

