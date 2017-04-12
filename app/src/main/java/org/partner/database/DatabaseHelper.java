package org.partner.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.partner.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Helper class to create a database or upgrade the database
 * <p>
 * For Partner apps we'll copy database from the assets folder.
 *
 * @author vinayagasundar
 */

/*package*/ class DatabaseHelper extends SQLiteOpenHelper {


    private static final String TAG = "DatabaseHelper";

    private static final boolean DEBUG = BuildConfig.DEBUG;

    /**
     * Name of the Database
     */
    private static final String DATABASE_NAME = "sample_partner_app_data.db";


    /**
     * Version of the Database
     */
    private static final int DATABASE_VERSION = 1;


    /**
     * Location of the database
     */
    private static final String DB_PATH_SUFFIX = "/databases/";


    private Context mContext;

    /*package*/ DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // If you're copying the database from the assets folder
        // Don't do anything here

        if (DEBUG) {
            Log.i(TAG, "onCreate: ");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (DEBUG) {
            Log.i(TAG, String.format("onUpgrade: oldVersion : %s newVersion : %s", oldVersion,
                    newVersion));
        }
    }


    /**
     * Get the instance of the database for Database operation.
     * @return instance of the database
     */
    /*package*/ SQLiteDatabase getDatabase() {
        File dbFile = mContext.getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                copyDatabaseFromAssets();
            } catch (IOException e) {
                if (DEBUG) {
                    Log.e(TAG, "getDatabase: ", e);
                }
                throw new RuntimeException("Error creating source database", e);
            }
        }

        return SQLiteDatabase.openDatabase(dbFile.getPath(), null,
                SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }


    /**
     * Copy the database from the Asset folder
     */
    private void copyDatabaseFromAssets() throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = mContext.getAssets().open(DATABASE_NAME);

            String outFileName = getDatabasePath();

            // if the path doesn't exist first, create it
            File f = new File(mContext.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();

            outputStream = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            // Close the streams
            outputStream.flush();

        } finally {
            if (inputStream != null) {
                inputStream.close();
            }

            if (outputStream != null) {
                outputStream.close();
            }
        }

    }


    /**
     * Give the full path of the Database in the App folder
     *
     * @return full path of the database
     */
    private String getDatabasePath() {
        return mContext.getApplicationInfo().dataDir + DB_PATH_SUFFIX
                + DATABASE_NAME;
    }
}
