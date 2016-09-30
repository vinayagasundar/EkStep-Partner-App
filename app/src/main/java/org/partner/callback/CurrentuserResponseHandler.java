package org.partner.callback;

import org.ekstep.genieservices.sdks.response.GenieResponse;
import org.ekstep.genieservices.sdks.response.IResponseHandler;

public class CurrentuserResponseHandler implements IResponseHandler {
    private ICurrentUser mICurrentUser = null;

    public CurrentuserResponseHandler(ICurrentUser currentUser) {
        mICurrentUser = currentUser;
    }
 @Override
    public void onSuccess(GenieResponse genieResponse) {
        // Code to handle success scenario
     mICurrentUser.onSuccessCurrentUser(genieResponse);
    }

    @Override
    public void onFailure(GenieResponse genieResponse) {
        // Code to handle error scenario
        mICurrentUser.onFailureCurrentUser(genieResponse);
    }
}

