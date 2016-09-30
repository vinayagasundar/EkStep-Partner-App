package org.partner.callback;

import org.ekstep.genieservices.sdks.response.GenieResponse;
import org.ekstep.genieservices.sdks.response.IResponseHandler;

public class PartnerDataResponseHandler implements IResponseHandler {
    private IPartnerData mIPartnerData = null;

    public PartnerDataResponseHandler(IPartnerData partnerData) {
        mIPartnerData = partnerData;
    }

    @Override
    public void onSuccess(GenieResponse response) {
        // Code to handle success scenario
        mIPartnerData.onSuccessPartner(response);

    }

    @Override
    public void onFailure(GenieResponse response) {
        // Code to handle error scenario
        mIPartnerData.onFailurePartner(response);
    }

}

