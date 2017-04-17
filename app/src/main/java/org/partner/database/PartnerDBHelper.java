package org.partner.database;

import android.database.Cursor;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.partner.model.Child;

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
    public static final String FIELD_FULL_NAME = "fullname";
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
    public static ArrayList<String> getAllValueForFieldWithCondition(
            String tableName, String fieldName, String conditionField, String conditionValue) {
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


    /**
     * It'll return the school id for given information
     * @param district User selected district
     * @param block User selected block with in the district
     * @param schoolName User selected school with in the block
     * @return if school exits it'll return otherwise return null
     */
    @Nullable
    public static String getSchoolID(String district, String block, String schoolName) {
        if (TextUtils.isEmpty(district)) {
            throw new NullPointerException("district should not be null");
        }

        if (TextUtils.isEmpty(block)) {
            throw new NullPointerException("block should not be null");
        }

        if (TextUtils.isEmpty(schoolName)) {
            throw new NullPointerException("school name should not be null");
        }

        final String where = FIELD_DISTRICT + " = ? AND " + FIELD_BLOCK + " = ? AND "
                + FIELD_SCHOOL_NAME + " = ?";
        final String [] whereArgs = {
                district,
                block,
                schoolName
        };

        Cursor cursor = PartnerDatabaseHandler.getInstance().query(TABLE_SCHOOL_INFO, null,
                where, whereArgs, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToNext();
            String schoolId = cursor.getString(cursor.getColumnIndex(FIELD_SCHOOL_ID));

            cursor.close();

            return schoolId;
        }

        return null;
    }


    /**
     * Get the list of the child (student) for given {@code schoolId}
     * @param schoolId Unique id of the school
     * @return list of {@link Child}
     */
    public static ArrayList<Child> getAllChild(String schoolId) {
        ArrayList<Child> childArrayList = new ArrayList<>();

        final String where = FIELD_SCHOOL_ID + " = ?";
        final String [] whereArgs = {
                schoolId
        };

        Cursor cursor = PartnerDatabaseHandler.getInstance().query(TABLE_STUDENT_INFO, null, where,
                whereArgs, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Child child = new Child();

                child.setStudentId(cursor.getString(cursor.getColumnIndex(FIELD_STUDENT_ID)));
                child.setFullName(cursor.getString(cursor.getColumnIndex(FIELD_FULL_NAME)));
                child.setStandard(cursor.getInt(cursor.getColumnIndex(FIELD_STANDARD)));
                child.setAge(cursor.getInt(cursor.getColumnIndex(FIELD_AGE)));
                child.setGender(cursor.getString(cursor.getColumnIndex(FIELD_GENDER)));
                child.setUid(cursor.getString(cursor.getColumnIndex(FIELD_UID)));

                childArrayList.add(child);
            }

            cursor.close();
        }

        return childArrayList;
    }

}
