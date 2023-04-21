package com.demo.PocketStore


import android.database.sqlite.SQLiteDatabase
import com.demo.PocketStore.UserDataManager.DataBaseManagementHelper
import android.database.sqlite.SQLiteOpenHelper
import com.demo.PocketStore.UserDataManager
import android.util.Log
import android.content.ContentValues
import android.content.Context
import kotlin.Throws
import android.database.Cursor
import android.database.SQLException
import java.util.ArrayList

class UserDataManager(context: Context?) {
    private var mContext: Context? = null

    enum class COL(val code: Int) {
        id(0), nname(1), email(2), phone(3), pro(4), status(5), pwd(6);

    }

    //UserData manager_User = new UserData("wky","wky");
    private var mSQLiteDatabase: SQLiteDatabase? = null
    private var mDatabaseHelper: DataBaseManagementHelper? = null

    //  long i= insertUserData( manager_User);//insert amdmin
    //DataBaseManagementHelper extend from SQLiteOpenHelper
    private class DataBaseManagementHelper internal constructor(context: Context?) :
        SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            Log.v(TAG, "db.getVersion()=" + db.version)
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";")
            db.execSQL(DB_CREATE)
            val values = ContentValues()
            values.put(USER_NAME, "University of Leeds")
            values.put(USER_EMAIL, "leedsuniversity@organisation.com")
            values.put(USER_PWD, "test")
            db.insert(TABLE_NAME, ID, values)
            Log.v(TAG, "db.execSQL(DB_CREATE)")
            Log.e(TAG, DB_CREATE)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            Log.v(TAG, "DataBaseManagementHelper onUpgrade")
            onCreate(db)
        }
    }

    //open database
    @Throws(SQLException::class)
    fun openDataBase() {
        mDatabaseHelper = DataBaseManagementHelper(mContext)
        mSQLiteDatabase = mDatabaseHelper!!.writableDatabase
    }

    //close database
    @Throws(SQLException::class)
    fun closeDataBase() {
        mDatabaseHelper!!.close()
    }

    //add new user(sign up)
    fun insertUserData(userData: UserData): Long {
        val userName = userData.userName
        val userPwd = userData.userPwd
        val values = ContentValues()
        values.put(USER_NAME, userName)
        values.put(USER_EMAIL, userData.userEmail)
        values.put(USER_PHONE, userData.userPhone)
        values.put(USER_STATUS, "0")
        values.put(USER_PWD, userPwd)
        return mSQLiteDatabase!!.insert(TABLE_NAME, ID, values)
    }

    //update user data, eg change password
    fun updateUserData(userData: UserData): Boolean {
        //int id = userData.getUserId();
        val userName = userData.userName
        val userPwd = userData.userPwd
        val values = ContentValues()
        values.put(USER_NAME, userName)
        values.put(USER_PWD, userPwd)
        return mSQLiteDatabase!!.update(TABLE_NAME, values, null, null) > 0
        //return mSQLiteDatabase.update(TABLE_NAME, values, ID + "=" + id, null) > 0;
    }

    //
    @Throws(SQLException::class)
    fun fetchUserData(id: Int): Cursor? {
        val mCursor = mSQLiteDatabase!!.query(
            false, TABLE_NAME, null, ID
                    + "=" + id, null, null, null, null, null
        )
        mCursor?.moveToFirst()
        return mCursor
    }

    //
    fun fetchAllUserDatas(): Cursor {
        return mSQLiteDatabase!!.query(
            TABLE_NAME, null, null, null, null, null,
            null
        )
    }

    val allUserDataList: List<UserData>
        get() {
            val cursor = mSQLiteDatabase!!.query(
                TABLE_NAME, null, null, null, null, null,
                null
            )
            val userList: MutableList<UserData> = ArrayList()
            while (cursor.moveToNext()) {
                userList.add(
                    UserData(
                        cursor.getInt(COL.id.code),
                        cursor.getString(COL.nname.code),
                        cursor.getString(COL.email.code),
                        cursor.getString(COL.phone.code),
                        cursor.getString(COL.pro.code),
                        cursor.getString(COL.status.code),
                        cursor.getString(COL.pwd.code)
                    )
                )
            }
            return userList
        }

    fun checkUserDataValid(name: String, email: String): Boolean {
        if ("admin" == name) {
            return false
        }
        val cursor = mSQLiteDatabase!!.query(
            TABLE_NAME, null, null, null, null, null,
            null
        )
        val userList: MutableList<UserData> = ArrayList()
        while (cursor.moveToNext()) {
            userList.add(
                UserData(
                    cursor.getInt(COL.id.code),
                    cursor.getString(COL.nname.code),
                    cursor.getString(COL.email.code),
                    cursor.getString(COL.phone.code),
                    cursor.getString(COL.pro.code),
                    cursor.getString(COL.status.code),
                    cursor.getString(COL.pwd.code)
                )
            )
        }
        for (userData in userList) {
            val _email = userData.userEmail
            val _name = userData.userName
            if (!_email!!.isEmpty() && _email == email) {
                return false
            }
            if (!_name!!.isEmpty() && _name == name) {
                return false
            }
        }
        return true
    }

    //delete user by id
    fun deleteUserData(id: Int): Boolean {
        return mSQLiteDatabase!!.delete(TABLE_NAME, ID + "=" + id, null) > 0
    }

    //delete user by name
    fun deleteUserDatabyname(name: String): Boolean {
        return mSQLiteDatabase!!.delete(TABLE_NAME, USER_NAME + "=" + name, null) > 0
    }

    //delete all user
    fun deleteAllUserDatas(): Boolean {
        return mSQLiteDatabase!!.delete(TABLE_NAME, null, null) > 0
    }

    //
    fun getStringByColumnName(columnName: String?, id: Int): String {
        val mCursor = fetchUserData(id)
        val columnIndex = mCursor!!.getColumnIndex(columnName)
        val columnValue = mCursor.getString(columnIndex)
        mCursor.close()
        return columnValue
    }

    //
    fun updateUserDataById(
        columnName: String?, id: Int,
        columnValue: String?
    ): Boolean {
        val values = ContentValues()
        values.put(columnName, columnValue)
        return mSQLiteDatabase!!.update(TABLE_NAME, values, ID + "=" + id, null) > 0
    }

    fun updateUserDataById(userData: UserData, id: Int): Boolean {
        val values = ContentValues()
        values.put(ID, userData.userId)
        values.put(USER_NAME, userData.userName)
        values.put(USER_EMAIL, userData.userEmail)
        values.put(USER_PHONE, userData.userPhone)
        values.put(USER_STATUS, userData.userStatus)
        values.put(USER_PRO, userData.userPro)
        values.put(USER_PWD, userData.userPwd)
        return mSQLiteDatabase!!.update(TABLE_NAME, values, ID + "=" + id, null) > 0
    }

    //find user by username, determine whether this username exists in database
    fun findUserByName(userName: String): Int {
        Log.v(TAG, "findUserByName , userName=$userName")
        val result = 0
        val cur = mSQLiteDatabase!!.query(
            TABLE_NAME,
            null,
            USER_NAME + "=?",
            arrayOf(userName),
            null,
            null,
            null
        )
        while (cur.moveToNext()) {
            val user = cur.getString(COL.nname.code)
            Log.v("db_query", user)
            if (user == userName) {
                cur.close()
                return 1
            } else cur.close()
            return 0
        }
        Log.v(TAG, "findUserByName , result=$result")
        return result
    }

    //find user by username and password, for login
    fun findUserByNameAndPwd(userName: String, pwd: String): UserData? {
        Log.v(TAG, "findUserByNameAndPwd")
        var userData: UserData? = null
        val cur = mSQLiteDatabase!!.query(
            TABLE_NAME,
            null,
            USER_NAME + "=?",
            arrayOf(userName),
            null,
            null,
            null
        )
        while (cur.moveToNext()) {
            val userPwd = cur.getString(COL.pwd.code)
            val userStatus = cur.getString(COL.status.code)
            Log.v("db_query", userPwd)
            if (pwd == userPwd) {
                userData = UserData(
                    cur.getInt(COL.id.code),
                    cur.getString(COL.nname.code),
                    cur.getString(COL.email.code),
                    cur.getString(COL.phone.code),
                    cur.getString(COL.pro.code),
                    cur.getString(COL.status.code),
                    cur.getString(COL.pwd.code)
                )
                cur.close()
                return userData
            }
            //            else
//			{
//				cur.close();
//				return 0;
//			}
        }
        return userData
    }

    companion object {
        //class for user data management
        //global value and declaration
        private const val TAG = "UserDataManager"
        private const val DB_NAME = "Organisation"
        private const val TABLE_NAME = "users"
        const val ID = "_id"
        const val USER_NAME = "name"
        const val USER_EMAIL = "email"
        const val USER_PHONE = "phone_number"
        const val USER_PRO = "profile_picture"
        const val USER_STATUS = "register_status"
        const val USER_PWD = "password"
        private const val DB_VERSION = 2

        //create table for user
        private const val DB_CREATE = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID + " integer primary key,"
                + USER_NAME + " varchar,"
                + USER_EMAIL + " varchar,"
                + USER_PHONE + " varchar,"
                + USER_PRO + " varchar,"
                + USER_STATUS + " varchar,"
                + USER_PWD + " varchar" + ");")
    }

    init {
        mContext = context
        Log.v(TAG, "UserDataManager construction!")
    }
}