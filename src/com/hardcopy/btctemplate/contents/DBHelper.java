/*
 * Copyright (C) 2014 Bluetooth Connection Template
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hardcopy.btctemplate.contents;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hardcopy.btctemplate.utils.Logs;

/**
 * This is DB helper class
 * Below methods and parameters are example.
 * Modify this as you wish
 */
public class DBHelper {

    public static final String DATABASE_NAME = "btctemplate";
    //----------- Accel data table parameters
    public static final String TABLE_NAME_ACCEL_REPORT = "accel";
    public static final String KEY_ACCEL_ID = "_id";            // int		primary key, auto increment
    public static final String KEY_ACCEL_TYPE = "type";            // type
    public static final String KEY_ACCEL_TIME = "time";            // time
    public static final String KEY_ACCEL_YEAR = "year";            // year
    public static final String KEY_ACCEL_MONTH = "month";        // month
    public static final String KEY_ACCEL_DAY = "day";            // day
    public static final String KEY_ACCEL_HOUR = "hour";            // hour
    public static final String KEY_ACCEL_MINUTE = "minute";        // minute
    public static final String KEY_ACCEL_SECOND = "second";        // second
    public static final String KEY_ACCEL_DATA1 = "data1";        // mSumOfDifference    // theta wave
    public static final String KEY_ACCEL_DATA2 = "data2";        // mCount              // Alpha Wave
    public static final String KEY_ACCEL_DATA3 = "data3";        // mAverageDifference   // Beta Wave
    public static final String KEY_ACCEL_DATA4 = "data4";        // mSamplingInterval
    public static final String KEY_ACCEL_DATA5 = "data5";        // mTotalTime
    public static final String KEY_ACCEL_DATA6 = "data6";        // mTotalTime
    public static final String KEY_ACCEL_DATA7 = "data7";        // mTotalTime
    public static final String KEY_ACCEL_DATA8 = "data8";        // mTotalTime
    public static final String KEY_ACCEL_DATA9 = "data9";        // mTotalTime
    public static final String KEY_ACCEL_DATA10 = "data10";        // mTotalTime
    public static final String KEY_ACCEL_DATA11 = "data11";        // mTotalTime
    public static final String KEY_ACCEL_DATA12 = "data12";        // mTotalTime
    public static final String KEY_ACCEL_DATA13 = "data13";        // mTotalTime
    public static final String KEY_ACCEL_DATA14 = "data14";        // mTotalTime
    public static final String KEY_ACCEL_DATA15 = "data15";        // mTotalTime
    public static final String KEY_ACCEL_DATA16 = "data16";        // mTotalTime
    public static final String KEY_ACCEL_DATA17 = "data17";        // mTotalTime
    public static final String KEY_ACCEL_DATA18 = "data18";        // mTotalTime
    public static final String KEY_ACCEL_DATA19 = "data19";        // mTotalTime
    public static final String KEY_ACCEL_DATA20 = "data20";        // mTotalTime
    public static final String KEY_ACCEL_DATA21 = "data21";        // mTotalTime
    public static final String KEY_ACCEL_DATA22 = "data22";        // mTotalTime
    public static final String KEY_ACCEL_DATA23 = "data23";        // mTotalTime
    public static final String KEY_ACCEL_DATA24 = "data24";        // mTotalTime
    public static final String KEY_ACCEL_DATA25 = "data25";        // mTotalTime
    public static final String KEY_ACCEL_DATA26 = "data26";        // mTotalTime
    public static final String KEY_ACCEL_DATA27 = "data27";        // mTotalTime
    public static final String KEY_ACCEL_DATA28 = "data28";        // mTotalTime
    public static final String KEY_ACCEL_DATA29 = "data29";        // mTotalTime
    public static final String KEY_ACCEL_DATA30 = "data30";        // mTotalTime
    public static final String KEY_ACCEL_DATA31 = "data31";        // mTotalTime
    public static final String KEY_ACCEL_DATA32 = "data32";        // mTotalTime
    public static final String KEY_ACCEL_DATA33 = "data33";        // mTotalTime
    public static final String KEY_ACCEL_DATA34 = "data34";        // mTotalTime
    public static final String KEY_ACCEL_DATA35 = "data35";        // mTotalTime
    public static final String KEY_ACCEL_DATA36 = "data36";        // mTotalTime
    public static final String KEY_ACCEL_DATA37 = "data37";        // mTotalTime


    public static final String KEY_ACCEL_ARG0 = "arg0";        // int		// count of walk
    public static final String KEY_ACCEL_ARG1 = "arg1";        // int 		// calorie
    public static final String KEY_ACCEL_ARG2 = "arg2";        // string
    public static final String KEY_ACCEL_ARG3 = "arg3";        // string
    // Table 에 따라 기록되는 순서로 '추정'되는 int값을 조정해 준다
    public static final int INDEX_ACCEL_ID = 0;
    //    public static final int INDEX_ACCEL_TYPE = 1;
    public static final int INDEX_ACCEL_TIME = 1;
    //    public static final int INDEX_ACCEL_YEAR = 3;
//    public static final int INDEX_ACCEL_MONTH = 4;
//    public static final int INDEX_ACCEL_DAY = 5;
//    public static final int INDEX_ACCEL_HOUR = 6;
//    public static final int INDEX_ACCEL_MINUTE = 7;
//    public static final int INDEX_ACCEL_SECOND = 8;
    public static final int INDEX_ACCEL_DATA1 = 2;
    public static final int INDEX_ACCEL_DATA2 = 3;
    public static final int INDEX_ACCEL_DATA3 = 4;
    //    public static final int INDEX_ACCEL_DATA4 = 12;
//    public static final int INDEX_ACCEL_DATA5 = 13;
//    public static final int INDEX_ACCEL_ARG0 = 14;
//    public static final int INDEX_ACCEL_ARG1 = 15;
//    public static final int INDEX_ACCEL_ARG2 = 16;
//    public static final int INDEX_ACCEL_ARG3 = 17;
    private static final String TAG = "DBHelper";
    private static final int DATABASE_VERSION = 1;
    // DB Table을 입맛에 맞게 변경
    private static final String DATABASE_CREATE_ACCEL_TABLE = "CREATE TABLE " + TABLE_NAME_ACCEL_REPORT + "("
            + KEY_ACCEL_ID + " Integer primary key autoincrement, "
//            + KEY_ACCEL_TYPE + " Integer not null, "
            + KEY_ACCEL_TIME + " Text, "
//            + KEY_ACCEL_YEAR + " Integer, "
//            + KEY_ACCEL_MONTH + " Integer, "
//            + KEY_ACCEL_DAY + " Integer, "
//            + KEY_ACCEL_HOUR + " Integer, "
//            + KEY_ACCEL_MINUTE + " Integer, "
//            + KEY_ACCEL_SECOND + " integer, "
            + KEY_ACCEL_DATA1 + " integer, "
            + KEY_ACCEL_DATA2 + " integer, "
            + KEY_ACCEL_DATA3 + " integer, "
            + KEY_ACCEL_DATA4 + " integer, "
            + KEY_ACCEL_DATA5 + " integer, "
            + KEY_ACCEL_DATA6 + " integer, " + KEY_ACCEL_DATA7 + " integer, " + KEY_ACCEL_DATA8 + " integer, " + KEY_ACCEL_DATA9 + " integer, "
            + KEY_ACCEL_DATA10 + " integer, " + KEY_ACCEL_DATA11 + " integer, " + KEY_ACCEL_DATA12 + " integer, " + KEY_ACCEL_DATA13 + " integer, "
            + KEY_ACCEL_DATA14 + " integer, " + KEY_ACCEL_DATA15 + " integer, " + KEY_ACCEL_DATA16 + " integer, " + KEY_ACCEL_DATA17 + " integer, "
            + KEY_ACCEL_DATA18 + " integer, " + KEY_ACCEL_DATA19 + " integer, " + KEY_ACCEL_DATA20 + " integer, " + KEY_ACCEL_DATA21 + " integer, "
            + KEY_ACCEL_DATA22 + " integer, " + KEY_ACCEL_DATA23 + " integer, " + KEY_ACCEL_DATA24 + " integer, " + KEY_ACCEL_DATA25 + " integer, "
            + KEY_ACCEL_DATA26 + " integer, " + KEY_ACCEL_DATA27 + " integer, " + KEY_ACCEL_DATA28 + " integer, " + KEY_ACCEL_DATA29 + " integer, "
            + KEY_ACCEL_DATA30 + " integer, " + KEY_ACCEL_DATA31 + " integer, " + KEY_ACCEL_DATA32 + " integer, " + KEY_ACCEL_DATA33 + " integer, "
            + KEY_ACCEL_DATA34 + " integer, " + KEY_ACCEL_DATA35 + " integer, " + KEY_ACCEL_DATA36 + " integer, " + KEY_ACCEL_DATA37 + " integer"

//            + KEY_ACCEL_DATA4 + " integer, "
//            + KEY_ACCEL_DATA5 + " integer, "
//            + KEY_ACCEL_ARG0 + " integer, "
//            + KEY_ACCEL_ARG1 + " integer, "
//            + KEY_ACCEL_ARG2 + " Text, "
//            + KEY_ACCEL_ARG3 + " Text"
            + ")";
    private static final String DATABASE_DROP_ACCEL_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME_ACCEL_REPORT;
    //----------- End of Accel data table parameters

    // Context, System
    private final Context mContext;
    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;

    // Constructor
    public DBHelper(Context context) {

        this.mContext = context;

    }


    //----------------------------------------------------------------------------------
    // Public classes
    //----------------------------------------------------------------------------------
    // DB open (Writable)
    public DBHelper openWritable() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    // DB open (Readable)
    public DBHelper openReadable() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getReadableDatabase();
        return this;
    }

    // Terminate DB
    public void close() {
        if (mDb != null) {
            mDb.close();
            mDb = null;
        }
        if (mDbHelper != null) {
            mDbHelper.close();
            mDbHelper = null;
        }
    }

    //----------------------------------------------------------------------------------
    // INSERT
    //----------------------------------------------------------------------------------
    // day, month, year, hour, String subdata, int type 는 필요없어서 삭제
    public long insertActivityReport(String time, String[] dataArray) throws SQLiteConstraintException {
//        if (time < 1 || dataArray == null || dataArray.length < 5){
//            return -1;
//        }


        ContentValues insertValues = new ContentValues();
        //  insertValues.put(KEY_ACCEL_TYPE, type);
        //  insertValues.put(KEY_ACCEL_TYPE, type);

        insertValues.put(KEY_ACCEL_TIME, time);

        //insertValues.put(KEY_ACCEL_HOUR, hour);


        insertValues.put(KEY_ACCEL_DATA1, dataArray[0]);
        insertValues.put(KEY_ACCEL_DATA2, dataArray[1]);
        insertValues.put(KEY_ACCEL_DATA3, dataArray[2]);
        insertValues.put(KEY_ACCEL_DATA4, dataArray[3]);
        insertValues.put(KEY_ACCEL_DATA5, dataArray[4]);
        insertValues.put(KEY_ACCEL_DATA6, dataArray[5]);
        insertValues.put(KEY_ACCEL_DATA7, dataArray[6]);
        insertValues.put(KEY_ACCEL_DATA8, dataArray[7]);
        insertValues.put(KEY_ACCEL_DATA9, dataArray[8]);
        insertValues.put(KEY_ACCEL_DATA10, dataArray[9]);
        insertValues.put(KEY_ACCEL_DATA11, dataArray[10]);
        insertValues.put(KEY_ACCEL_DATA12, dataArray[11]);
        insertValues.put(KEY_ACCEL_DATA13, dataArray[12]);
        insertValues.put(KEY_ACCEL_DATA14, dataArray[13]);
        insertValues.put(KEY_ACCEL_DATA15, dataArray[14]);
        insertValues.put(KEY_ACCEL_DATA16, dataArray[15]);
        insertValues.put(KEY_ACCEL_DATA17, dataArray[16]);
        insertValues.put(KEY_ACCEL_DATA18, dataArray[17]);
        insertValues.put(KEY_ACCEL_DATA19, dataArray[18]);
        insertValues.put(KEY_ACCEL_DATA20, dataArray[19]);
        insertValues.put(KEY_ACCEL_DATA21, dataArray[20]);
        insertValues.put(KEY_ACCEL_DATA22, dataArray[21]);
        insertValues.put(KEY_ACCEL_DATA23, dataArray[22]);
        insertValues.put(KEY_ACCEL_DATA24, dataArray[23]);
        insertValues.put(KEY_ACCEL_DATA25, dataArray[24]);
        insertValues.put(KEY_ACCEL_DATA26, dataArray[25]);
        insertValues.put(KEY_ACCEL_DATA27, dataArray[26]);
        insertValues.put(KEY_ACCEL_DATA28, dataArray[27]);
        insertValues.put(KEY_ACCEL_DATA29, dataArray[28]);
        insertValues.put(KEY_ACCEL_DATA30, dataArray[29]);
        insertValues.put(KEY_ACCEL_DATA31, dataArray[30]);
        insertValues.put(KEY_ACCEL_DATA32, dataArray[31]);
        insertValues.put(KEY_ACCEL_DATA33, dataArray[32]);
        insertValues.put(KEY_ACCEL_DATA34, dataArray[33]);
        insertValues.put(KEY_ACCEL_DATA35, dataArray[34]);
        insertValues.put(KEY_ACCEL_DATA36, dataArray[35]);
        insertValues.put(KEY_ACCEL_DATA37, dataArray[36]);


        //   insertValues.put(KEY_ACCEL_DATA1, dataArray[0]);
        //   insertValues.put(KEY_ACCEL_DATA2, dataArray[1]);
        //   insertValues.put(KEY_ACCEL_DATA3, dataArray[2]);
        // insertValues.put(KEY_ACCEL_ARG0, 0);
        // insertValues.put(KEY_ACCEL_ARG1, 0);
        // insertValues.put(KEY_ACCEL_ARG2, subData);



        synchronized (mDb) {
            if (mDb == null) {

                return -1;
            }
            return mDb.insertOrThrow(TABLE_NAME_ACCEL_REPORT, null, insertValues);      // 실질적으로 insert 하는 부분
        }
    }


    //----------------------------------------------------------------------------------
    // SELECT methods
    //----------------------------------------------------------------------------------
    public Cursor selectReportAll() {
        synchronized (mDb) {
            if (mDb == null) return null;
            return mDb.query(
                    TABLE_NAME_ACCEL_REPORT,    // Table : String
                    null,        // Columns : String[]
                    null,        // Selection : String
                    null,        // Selection arguments: String[]
                    null,        // Group by : String
                    null,        // Having : String
                    null,        // Order by : String
                    null);        // Limit : String
        }
    }

    public Cursor selectReportWithType(int type, int count) {
        synchronized (mDb) {
            if (mDb == null) return null;
            String countString = null;
            if (count > 0)
                countString = Integer.toString(count);
            return mDb.query(
                    TABLE_NAME_ACCEL_REPORT,        // Table : String
                    null,                        // Columns : String[]
                    KEY_ACCEL_TYPE + "=" + Integer.toString(type),        // Selection 	: String
                    null,            // Selection arguments: String[]
                    null,            // Group by 	: String
                    null,            // Having 		: String
                    KEY_ACCEL_ID + " DESC",            // Order by 	: String
                    countString);        // Limit		: String
        }
    }

    public Cursor selectReportWithTime(int type, long timeBiggerThan, long timeSmallerThan) {
        synchronized (mDb) {
            if (mDb == null) return null;
            return mDb.query(
                    TABLE_NAME_ACCEL_REPORT,        // Table : String
                    null,                            // Columns : String[]
                    KEY_ACCEL_TYPE + "=" + Integer.toString(type)
                            + " AND " + KEY_ACCEL_TIME + ">" + Long.toString(timeBiggerThan)
                            + " AND " + KEY_ACCEL_TIME + "<" + Long.toString(timeSmallerThan),        // Selection 	: String
                    null,            // Selection arguments: String[]
                    null,            // Group by 	: String
                    null,            // Having 		: String
                    KEY_ACCEL_ID + " DESC",            // Order by 	: String
                    null);        // Limit		: String
        }
    }

    public Cursor selectReportWithDate(int type, int year, int month, int day, int hour) {
        synchronized (mDb) {
            if (mDb == null) return null;

            StringBuilder sb = new StringBuilder();
            sb.append(KEY_ACCEL_TYPE).append("=").append(type);
            sb.append(" AND ").append(KEY_ACCEL_YEAR).append("=").append(year);

            if (month > -1 && month < 12) {
                sb.append(" AND ").append(KEY_ACCEL_MONTH).append("=").append(month);
            }
            if (day > -1 && day < 31) {
                sb.append(" AND ").append(KEY_ACCEL_DAY).append("=").append(day);
            }
            if (hour > -1 && hour < 24) {
                sb.append(" AND ").append(KEY_ACCEL_HOUR).append("=").append(hour);
            }
            return mDb.query(
                    TABLE_NAME_ACCEL_REPORT,        // Table : String
                    null,                            // Columns : String[]
                    sb.toString(),        // Selection 	: String
                    null,            // Selection arguments: String[]
                    null,            // Group by 	: String
                    null,            // Having 		: String
                    KEY_ACCEL_ID + " DESC",            // Order by 	: String
                    null);        // Limit		: String
        }
    }

    //----------------------------------------------------------------------------------
    // Update methods
    //----------------------------------------------------------------------------------
/*
    public int updateFilter(FilterObject filter)
	{
		if(filter.mType < 0 || filter.mCompareType < 0 
				|| filter.mOriginalString == null || filter.mOriginalString.length() < 1)
			return -1;
		
		ContentValues insertValues = new ContentValues();
		insertValues.put(KEY_FILTER_TYPE, filter.mType);
		insertValues.put(KEY_FILTER_ICON_TYPE, filter.mIconType);
		insertValues.put(KEY_FILTER_MATCHING, filter.mCompareType);
		insertValues.put(KEY_FILTER_REPLACE_TYPE, filter.mReplaceType);
		insertValues.put(KEY_FILTER_ORIGINAL, filter.mOriginalString);
		insertValues.put(KEY_FILTER_REPLACE, filter.mReplaceString);
//		insertValues.put(KEY_FILTER_ARG0, 0);		// for future use
//		insertValues.put(KEY_FILTER_ARG1, 0);
//		insertValues.put(KEY_FILTER_ARG2, "");
//		insertValues.put(KEY_FILTER_ARG3, "");
		
		synchronized (mDb) {
			if(mDb == null) 
				return -1;
			return mDb.update( TABLE_NAME_FILTERS,		// table
					insertValues, 		// values
					KEY_FILTER_ID + "='" + filter.mId + "'", // whereClause
					null ); 			// whereArgs
		}
	}
*/

    //----------------------------------------------------------------------------------
    // Delete methods
    //----------------------------------------------------------------------------------

    public void deleteReportWithID(int id) {
        if (mDb == null) return;

        synchronized (mDb) {
            int count = mDb.delete(TABLE_NAME_ACCEL_REPORT,
                    KEY_ACCEL_ID + "=" + id, // whereClause
                    null);            // whereArgs
            Logs.d(TAG, "- Delete record : id=" + id + ", count=" + count);
        }
    }

    public void deleteReportWithType(int type) {
        if (mDb == null) return;

        synchronized (mDb) {
            int count = mDb.delete(TABLE_NAME_ACCEL_REPORT,
                    KEY_ACCEL_TYPE + "=" + type, // whereClause
                    null);            // whereArgs
            Logs.d(TAG, "- Delete record : type=" + type + ", deleted count=" + count);
        }
    }

    public void deleteReportWithTime(int type, long timeBiggerThan, long timeSmallerThan) {
        if (mDb == null) return;

        synchronized (mDb) {
            int count = mDb.delete(TABLE_NAME_ACCEL_REPORT,
                    KEY_ACCEL_TYPE + "=" + type
                            + " AND " + KEY_ACCEL_TIME + ">" + Long.toString(timeBiggerThan)
                            + " AND " + KEY_ACCEL_TIME + "<" + Long.toString(timeSmallerThan), // whereClause
                    null);            // whereArgs
            Logs.d(TAG, "- Delete record : type=" + type + ", " + timeBiggerThan + " < time < " + timeSmallerThan + ", deleted count=" + count);
        }
    }

    public void deleteReportWithDate(int type, int year, int month, int day, int hour) {
        if (mDb == null) return;

        synchronized (mDb) {
            int count = mDb.delete(TABLE_NAME_ACCEL_REPORT,
                    KEY_ACCEL_TYPE + "=" + type
                            + " AND " + KEY_ACCEL_YEAR + "=" + Integer.toString(year)
                            + " AND " + KEY_ACCEL_MONTH + "=" + Integer.toString(month)
                            + " AND " + KEY_ACCEL_DAY + "=" + Integer.toString(day)
                            + " AND " + KEY_ACCEL_HOUR + "=" + Integer.toString(hour), // whereClause
                    null);            // whereArgs
        }
    }

    //----------------------------------------------------------------------------------
    // Count methods
    //----------------------------------------------------------------------------------
    public int getReportCount() {
        String query = "select count(*) from " + TABLE_NAME_ACCEL_REPORT;
        Cursor c = mDb.rawQuery(query, null);
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        return count;
    }

    public int getReportCountWithType(int type) {
        String query = "select count(*) from " + TABLE_NAME_ACCEL_REPORT + " where " + KEY_ACCEL_TYPE + "=" + Integer.toString(type);
        Cursor c = mDb.rawQuery(query, null);
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        return count;
    }

    public int getReportCountWithTime(int type, long timeBiggerThan, long timeSmallerThan) {
        String query = "select count(*) from " + TABLE_NAME_ACCEL_REPORT + " where "
                + KEY_ACCEL_TYPE + "=" + Integer.toString(type)
                + " AND " + KEY_ACCEL_TIME + ">" + Long.toString(timeBiggerThan)
                + " AND " + KEY_ACCEL_TIME + "<" + Long.toString(timeSmallerThan);
        Cursor c = mDb.rawQuery(query, null);
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        return count;
    }


    //----------------------------------------------------------------------------------
    // SQLiteOpenHelper
    //----------------------------------------------------------------------------------
    private static class DatabaseHelper extends SQLiteOpenHelper {
        // Constructor
        public DatabaseHelper(Context context) {
            super(context, "/mnt/sdcard/" + "BrainDB.db", null, DATABASE_VERSION);
        }

        // Will be called one time at first access
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_ACCEL_TABLE);
        }

        // Will be called when the version is increased
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO: Keep previous data
            db.execSQL(DATABASE_DROP_ACCEL_TABLE);
            db.execSQL(DATABASE_CREATE_ACCEL_TABLE);
        }

    }    // End of class DatabaseHelper

}
