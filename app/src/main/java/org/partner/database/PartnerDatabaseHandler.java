package org.partner.database;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * This handler class will help you to access the database
 * it'll use a singleton instance of the Database for all the operation
 *
 *
 * @author vinayagasundar
 */

public final class PartnerDatabaseHandler {


    /**
     * Singleton instance of the class
     */
    private static PartnerDatabaseHandler mInstance;


    /**
     * Database instance
     */
    private SQLiteDatabase mDatabase;


    /**
     * Initialize the Database handler
     * It need to call from {@link android.app.Application} class {@link Application#onCreate()}
     * method
     *
     * @param context context to create or get the database
     */
    public static void init(Context context) {
        mInstance = new PartnerDatabaseHandler(context);
    }


    /**
     * It'll give the single ton instance of the Database handler class
     * @return instance of the class
     */
    public static PartnerDatabaseHandler getInstance() {
        if (mInstance == null) {
            throw new IllegalAccessError("Database need to initialize on " +
                    "Application class onCreate() method");
        }

        return mInstance;
    }


    private PartnerDatabaseHandler(Context context) {
       mDatabase = new DatabaseHelper(context).getDatabase();
    }


    /**
     * Convenience method for inserting a row into the database.
     *
     * @param table          the table to insert the row into.
     * @param nullColumnHack optional; may be null. SQL doesn't allow inserting a completely empty row without
     *                       naming at least one column name. If your provided values is empty, no column names
     *                       are known and an empty row can't be inserted. If not set to null, the
     *                       nullColumnHack parameter provides the name of nullable column name to explicitly
     *                       insert a NULL into in the case where your values is empty.
     * @param values         this map contains the initial column values for the row. The keys should be the
     *                       column names and the values the column values.
     * @return the row ID of the newly inserted row, or -1 if an error occurred.
     */
    public long insert(String table, String nullColumnHack, ContentValues values) {
        return mDatabase.insert(table, null, values);
    }


    /**
     * General method for inserting a row into the database.
     *
     * @param table             the table to insert the row into
     * @param nullColumnHack    optional; may be <code>null</code>.
     *                          SQL doesn't allow inserting a completely empty row without
     *                          naming at least one column name.  If your provided <code>initialValues</code> is
     *                          empty, no column names are known and an empty row can't be inserted.
     *                          If not set to null, the <code>nullColumnHack</code> parameter
     *                          provides the name of nullable column name to explicitly insert a NULL into
     *                          in the case where your <code>initialValues</code> is empty.
     * @param initialValues     this map contains the initial column values for the
     *                          row. The keys should be the column names and the values the
     *                          column values
     * @param conflictAlgorithm for insert conflict resolver
     * @return the row ID of the newly inserted row OR <code>-1</code> if either the
     * input parameter <code>conflictAlgorithm</code> = {@link SQLiteDatabase#CONFLICT_IGNORE}
     * or an error occurred.
     */
    public long insertWithOnConflict(String table, String nullColumnHack,
                                     ContentValues initialValues, int conflictAlgorithm) {
        return mDatabase.insertWithOnConflict(table, nullColumnHack, initialValues,
                conflictAlgorithm);
    }

    /**
     * Convenience method for updating rows in the database.
     *
     * @param table       the table to update in.
     * @param values      a map from column names to new column values. null is a valid value that will be
     *                    translated to NULL.
     * @param whereClause the optional WHERE clause to apply when updating. Passing null will update all
     *                    rows.
     * @param whereArgs   You may include ?s in the where clause, which will be replaced by the values
     *                    from whereArgs. The values will be bound as Strings.
     * @return the number of rows affected
     */
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return mDatabase.update(table, values, whereClause, whereArgs);
    }

    /**
     * Runs the provided SQL and returns a Cursor over the result set.
     * <p/>
     *
     * @param sql           the SQL query. The SQL string must not be ; terminated
     * @param selectionArgs You may include ?s in where clause in the query, which will be
     *                      replaced by the values from selectionArgs. The values will be bound as Strings.
     *                      Returns:
     * @return A Cursor object, which is positioned before the first entry. Note that Cursors are not
     * synchronized, see the documentation for more details.
     */
    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return mDatabase.rawQuery(sql, selectionArgs);
    }

    /**
     * Convenience method for deleting rows in the database.
     *
     * @param table       the table to delete from
     * @param whereClause the optional WHERE clause to apply when deleting. Passing null will delete
     *                    all rows.
     * @param whereArgs   You may include ?s in the where clause, which will be replaced by the values
     *                    from whereArgs. The values will be bound as Strings.
     * @return the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all
     * rows and get a count pass "1" as the whereClause.
     */
    public int delete(String table, String whereClause, String[] whereArgs) {
        return mDatabase.delete(table, whereClause, whereArgs);
    }

    /**
     * Query the given table, returning a Cursor over the result set.
     *
     * @param table         The table name to compile the query against.
     * @param columns       A list of which columns to return. Passing null will return all columns, which is
     *                      discouraged to prevent reading data from storage that isn't going to be used.
     * @param selection     A filter declaring which rows to return, formatted as an SQL WHERE clause
     *                      (excluding the WHERE itself). Passing null will return all rows for the given
     *                      table.
     * @param selectionArgs You may include ?s in selection, which will be replaced by the values from
     *                      selectionArgs, in order that they appear in the selection. The values will be
     *                      bound as Strings.
     * @param groupBy       A filter declaring how to group rows, formatted as an SQL GROUP BY clause
     *                      (excluding the GROUP BY itself). Passing null will cause the rows to not be
     *                      grouped.
     * @param having        A filter declare which row groups to include in the cursor, if row grouping is
     *                      being used, formatted as an SQL HAVING clause (excluding the HAVING itself).
     *                      Passing null will cause all row groups to be included, and is required when row
     *                      grouping is not being used.
     * @param orderBy       How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY
     *                      itself). Passing null will use the default sort order, which may be unordered.
     * @return A Cursor object, which is positioned before the first entry. Note that Cursors are
     * not synchronized, see the documentation for more details.
     */
    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs,
                        String groupBy, String having, String orderBy) {
        return mDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    /**
     * Query the given table, returning a Cursor over the result set.
     *
     * @param table         The table name to compile the query against.
     * @param columns       A list of which columns to return. Passing null will return all columns, which is
     *                      discouraged to prevent reading data from storage that isn't going to be used.
     * @param selection     A filter declaring which rows to return, formatted as an SQL WHERE clause
     *                      (excluding the WHERE itself). Passing null will return all rows for the given
     *                      table.
     * @param selectionArgs You may include ?s in selection, which will be replaced by the values from
     *                      selectionArgs, in order that they appear in the selection. The values will be
     *                      bound as Strings.
     * @param groupBy       A filter declaring how to group rows, formatted as an SQL GROUP BY clause
     *                      (excluding the GROUP BY itself). Passing null will cause the rows to not be
     *                      grouped.
     * @param having        A filter declare which row groups to include in the cursor, if row grouping is
     *                      being used, formatted as an SQL HAVING clause (excluding the HAVING itself).
     *                      Passing null will cause all row groups to be included, and is required when row
     *                      grouping is not being used.
     * @param orderBy       How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY
     *                      itself). Passing null will use the default sort order, which may be unordered.
     * @param limit         Total number of item need to fetch from this query.
     * @return A Cursor object, which is positioned before the first entry. Note that Cursors are
     * not synchronized, see the documentation for more details.
     */
    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs,
                        String groupBy, String having, String orderBy, String limit) {
        return mDatabase
                .query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    /**
     * Query the given URL, returning a {@link Cursor} over the result set.
     *
     * @param distinct true if you want each row to be unique, false otherwise.
     * @param table The table name to compile the query against.
     * @param columns A list of which columns to return. Passing null will
     *            return all columns, which is discouraged to prevent reading
     *            data from storage that isn't going to be used.
     * @param selection A filter declaring which rows to return, formatted as an
     *            SQL WHERE clause (excluding the WHERE itself). Passing null
     *            will return all rows for the given table.
     * @param selectionArgs You may include ?s in selection, which will be
     *         replaced by the values from selectionArgs, in order that they
     *         appear in the selection. The values will be bound as Strings.
     * @param groupBy A filter declaring how to group rows, formatted as an SQL
     *            GROUP BY clause (excluding the GROUP BY itself). Passing null
     *            will cause the rows to not be grouped.
     * @param having A filter declare which row groups to include in the cursor,
     *            if row grouping is being used, formatted as an SQL HAVING
     *            clause (excluding the HAVING itself). Passing null will cause
     *            all row groups to be included, and is required when row
     *            grouping is not being used.
     * @param orderBy How to order the rows, formatted as an SQL ORDER BY clause
     *            (excluding the ORDER BY itself). Passing null will use the
     *            default sort order, which may be unordered.
     * @param limit Limits the number of rows returned by the query,
     *            formatted as LIMIT clause. Passing null denotes no LIMIT clause.
     * @return A {@link Cursor} object, which is positioned before the first entry. Note that
     * {@link Cursor}s are not synchronized, see the documentation for more details.
     * @see Cursor
     */
    public Cursor query(boolean distinct, String table, String[] columns,
                        String selection, String[] selectionArgs, String groupBy,
                        String having, String orderBy, String limit) {
        return mDatabase.query(distinct, table, columns, selection, selectionArgs, groupBy, having,
                orderBy, limit);
    }


}
