package org.partner.callback;

import org.ekstep.genieservices.sdks.response.GenieResponse;
import org.ekstep.genieservices.sdks.response.IResponseHandler;

public class CurrentGetUserResponseHandler implements IResponseHandler {
    private ICurrentGetUser mICurrentGetUser = null;

    public CurrentGetUserResponseHandler(ICurrentGetUser currentGetUser) {
        mICurrentGetUser = currentGetUser;
    }
 @Override
    public void onSuccess(GenieResponse genieResponse) {
        // Code to handle success scenario
     mICurrentGetUser.onSuccessCurrentGetUser(genieResponse);

    }

    @Override
    public void onFailure(GenieResponse genieResponse) {
        // Code to handle error scenario
        mICurrentGetUser.onFailureCurrentGetUser(genieResponse);
    }
}

