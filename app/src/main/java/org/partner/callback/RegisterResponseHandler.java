package org.partner.callback;

import org.ekstep.genieservices.sdks.response.GenieResponse;
import org.ekstep.genieservices.sdks.response.IResponseHandler;

public class RegisterResponseHandler implements IResponseHandler {
    private IRegister mRegister = null;

    public RegisterResponseHandler(IRegister register) {
        mRegister = register;
    }

    @Override
    public void onSuccess(GenieResponse response) {
        // Code to handle success scenario
        mRegister.onSuccess(response);

    }

    @Override
    public void onFailure(GenieResponse response) {
        // Code to handle error scenario
        mRegister.onFailure(response);
    }

}

