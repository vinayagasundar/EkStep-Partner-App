package org.partner.util;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;
import org.partner.PartnerApp;


public class TelemetryEventGenertor {
    public static final String OE_START	= "OE_START";
    public static final String OE_END	= "OE_END";
    public static final String EVENT_DATA	= "edata";
    public static final String GAME_DATA= "gdata";
    public static final String EVENT_ID	= "eid";
    public static final String TIME_STAMP	= "ts";
    public static final String VERSION	= "ver";
    public static final String ID	= "id";
    public static final String EKS	= "eks";
    public static final String EXT	= "ext";
    public static final String TELEMETRY_VERSION	= "1.0";
    public static final String SESSION_UUID	= "sid";
    public static final String USER_UUID	= "uid";
    public static final String DEVICE_UUID	= "did";

    public static final String LENGTH	= "length";

    public static JSONObject generateOEStartEvent(Context context,String uid){
        JSONObject mainJSONObject=new JSONObject();
        try {


            JSONObject gameDataJsonObject=null;
            mainJSONObject.put(EVENT_ID, OE_START);
            mainJSONObject.put(TIME_STAMP, "");

            mainJSONObject.put(VERSION, TELEMETRY_VERSION);

            gameDataJsonObject=new JSONObject();
            gameDataJsonObject.put(ID, Utils.getPackageName(context));
            gameDataJsonObject.put(VERSION, Utils.getVersion(context));

            mainJSONObject.put(GAME_DATA,gameDataJsonObject);
            mainJSONObject.put(SESSION_UUID, "");
            mainJSONObject.put(USER_UUID, uid);
            mainJSONObject.put(DEVICE_UUID, "");

            JSONObject dspecJsonObject=new JSONObject();
            JSONObject eDataJsonObject=new JSONObject();
            eDataJsonObject.put(EKS, dspecJsonObject);

            JSONObject extObject=new JSONObject();
            eDataJsonObject.put(EXT, extObject);
            mainJSONObject.put(EVENT_DATA, eDataJsonObject);

            if(PartnerApp.DEBUG)
            Log.e("OE_START_EVENT", mainJSONObject.toString());


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return mainJSONObject;

    }
    public static JSONObject generateOEEndEvent(Context context,String uid,long startInterActionTime,long endInterActionTime){
        JSONObject mainJSONObject=new JSONObject();
        try {


            JSONObject gameDataJsonObject=null;
            mainJSONObject.put(EVENT_ID, OE_END);
            mainJSONObject.put(TIME_STAMP, "");
            mainJSONObject.put(VERSION, TELEMETRY_VERSION);

            gameDataJsonObject=new JSONObject();
            gameDataJsonObject.put(ID, Utils.getPackageName(context));
            gameDataJsonObject.put(VERSION, Utils.getVersion(context));

            mainJSONObject.put(GAME_DATA,gameDataJsonObject);
            mainJSONObject.put(SESSION_UUID, "");
            mainJSONObject.put(USER_UUID, uid);
            mainJSONObject.put(DEVICE_UUID, "");

            JSONObject eksChildJsonObject=new JSONObject();
            long timedifferenceSession=Long.valueOf(endInterActionTime)-Long.valueOf(startInterActionTime);
            long timeinSecondsSession=timedifferenceSession/1000;
            eksChildJsonObject.put(LENGTH, timeinSecondsSession);


            JSONObject eksJsonObject=new JSONObject();
            eksJsonObject.put(EKS,eksChildJsonObject);
            JSONObject extObject=new JSONObject();
            eksJsonObject.put(EXT, extObject);
            mainJSONObject.put(EVENT_DATA, eksJsonObject);
            if(PartnerApp.DEBUG)
            Log.e("OE_END_EVENT", mainJSONObject.toString());


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return mainJSONObject;

    }
}
