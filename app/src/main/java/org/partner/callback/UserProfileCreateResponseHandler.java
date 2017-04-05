package org.partner.callback;

import org.ekstep.genieservices.sdks.response.GenieResponse;
import org.ekstep.genieservices.sdks.response.IResponseHandler;

public class UserProfileCreateResponseHandler implements IResponseHandler {
    private IUserCreateProfile mIUserCreateProfile = null;


    public UserProfileCreateResponseHandler(IUserCreateProfile mIUserCreateProfile) {
        this.mIUserCreateProfile = mIUserCreateProfile;
    }

    @Override
    public void onSuccess(GenieResponse genieResponse) {
        if (mIUserCreateProfile != null) {
            mIUserCreateProfile.onSuccessUserProfile(genieResponse);
        }
    }

    @Override
    public void onFailure(GenieResponse genieResponse) {
        if (mIUserCreateProfile != null) {
            mIUserCreateProfile.onFailureUserProfile(genieResponse);
        }
    }
}

