package org.partner.callback;

import org.ekstep.genieservices.sdks.response.GenieListResponse;
import org.ekstep.genieservices.sdks.response.IListResponseHandler;

public class LanguageResponseHandler implements IListResponseHandler {
    private ILanguage mILanguage = null;

    public LanguageResponseHandler(ILanguage language) {
        mILanguage = language;
    }

    @Override
    public void onSuccess(GenieListResponse genieListResponse) {
        // Code to handle success scenario
        mILanguage.onSuccessLanguage(genieListResponse);
    }

    @Override
    public void onFailure(GenieListResponse genieListResponse) {
        // Code to handle error scenario
        mILanguage.onFailureLanguage(genieListResponse);
    }



}

