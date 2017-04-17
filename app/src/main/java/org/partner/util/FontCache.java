package org.partner.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

/**
 * Created by Dhruv on 3/28/2016.
 */
public class FontCache {
    private static Typeface typeface;
    private FontCache(){}
    public  static Typeface getTypeface(String fontName,Context context){
        if(typeface==null){
            try{
               Log.e("CustomTextView", "---------------------------------------------------");

                typeface=Typeface.createFromAsset(context.getAssets(),fontName);
            }catch (Exception e){
                return null;
            }
        }

        return typeface;
    }
}
