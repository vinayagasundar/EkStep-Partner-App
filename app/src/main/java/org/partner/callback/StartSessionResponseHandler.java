package org.partner.callback;

import org.ekstep.genieservices.sdks.response.GenieResponse;
import org.ekstep.genieservices.sdks.response.IResponseHandler;

public class StartSessionResponseHandler implements IResponseHandler {
    private IStartSession mIStartSession = null;

    public StartSessionResponseHandler(IStartSession startSession) {
        mIStartSession = startSession;
    }

    @Override
    public void onSuccess(GenieResponse response) {
        // Code to handle success scenario
        mIStartSession.onSuccessSession(response);

    }

    @Override
    public void onFailure(GenieResponse response) {
        // Code to handle error scenario
        mIStartSession.onFailureSession(response);
    }

}

