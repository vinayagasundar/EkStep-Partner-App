package org.partner.database;

import android.database.Cursor;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * A helper class to access the access database for particular purpose
 * @author vinayagasundar
 */

public final class PartnerDBHelper {

    // Table Names

    public static final String TABLE_SCHOOL_INFO = "school_info";
    public static final String TABLE_STUDENT_INFO = "student_info";

    // TABLE_SCHOOL_INFO field list

    public static final String FIELD_STATE = "state_name";
    public static final String FIELD_DISTRICT = "district";
    public static final String FIELD_BLOCK = "block";
    public static final String FIELD_SCHOOL_NAME = "school_name";
    public static final String FIELD_SCHOOL_ID = "school_id";


    // TABLE_STUDENT_INFO field list
    // FIELD_SCHOOL_ID also exist in TABLE_STUDENT_INFO

    public static final String FIELD_STUDENT_ID = "student_id";
    public static final String FIELD_FULL_NAME = "full_name";
    public static final String FIELD_STANDARD = "standard";
    public static final String FIELD_AGE = "age";
    public static final String FIELD_GENDER = "gender";
    public static final String FIELD_UID = "uid";


    /**
     * It'll return a distinct value for the {@code field} in the given {@code table}
     * @param table Name of the table
     * @param field Field which we need to find distinct value
     * @return List of distinct value
     */
    public static ArrayList<String> getDistinctValues(String table, String field) {
        ArrayList<String> fieldData = new ArrayList<>();

        if (TextUtils.isEmpty(table) || TextUtils.isEmpty(field)) {
            return fieldData;
        }

        Cursor cursor = PartnerDatabaseHandler.getInstance()
                .query(true, table, new String[]{field}, null, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                fieldData.add(cursor.getString(0));
            }

            cursor.close();
        }

        return fieldData;
    }

    /**
     * Get the all unique value for a field in a given Table
     *
     * @param tableName      Name of the Table
     * @param fieldName      Field which need to be get from the Table
     * @param conditionField Field which need to used for condition
     * @param conditionValue Value for condition field
     * @return if value exits it'll return list of string otherwise empty list
     */
    public static ArrayList<String> getAllValueForFieldWithCondition(String tableName, String fieldName,
                                                         String conditionField,
                                                         String conditionValue) {
        ArrayList<String> fieldData = new ArrayList<>();

        if (TextUtils.isEmpty(tableName) || TextUtils.isEmpty(fieldName)
                || TextUtils.isEmpty(conditionField) || TextUtils.isEmpty(conditionValue)) {
            return fieldData;
        }

        String where = conditionField + " = ?";
        String[] whereArgs = {
                conditionValue
        };

        Cursor cursor = PartnerDatabaseHandler.getInstance()
                .query(true, tableName, new String[]{fieldName}, where,
                whereArgs, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                fieldData.add(cursor.getString(0));
            }

            cursor.close();
        }

        return fieldData;
    }

}
