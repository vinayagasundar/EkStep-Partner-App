package org.partner.callback;

import org.ekstep.genieservices.sdks.response.GenieResponse;
import org.ekstep.genieservices.sdks.response.IResponseHandler;

public class EndSessionResponseHandler implements IResponseHandler {
    private IEndSession mIEndSession = null;

    public EndSessionResponseHandler(IEndSession endSession) {
        mIEndSession = endSession;
    }

    @Override
    public void onSuccess(GenieResponse response) {
        // Code to handle success scenario
        mIEndSession.onSuccessEndSession(response);


    }

    @Override
    public void onFailure(GenieResponse response) {
        // Code to handle error scenario
        mIEndSession.onFailureEndSession(response);

    }

}

