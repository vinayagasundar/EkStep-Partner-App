package org.partner.callback;

import org.ekstep.genieservices.sdks.response.GenieResponse;

/**
 * Callback for User Profile Created events
 * @author vinayagasundar
 */

public interface IUserCreateProfile {

    public void onSuccessUserProfile(GenieResponse genieResponse);
    public void onFailureUserProfile(GenieResponse genieResponse);
}
