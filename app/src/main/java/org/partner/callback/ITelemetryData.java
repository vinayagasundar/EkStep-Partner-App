package org.partner.callback;

import org.ekstep.genieservices.sdks.response.GenieResponse;

/**
 * Created by Jaya on 10/5/2015.
 */
public interface ITelemetryData {

    public void onSuccessTelemetry(GenieResponse genieResponse);
    public void  onFailureTelemetry(GenieResponse genieResponse);
}
