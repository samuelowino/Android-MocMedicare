package com.manage.hospital.hmapp.database.contracts;

import android.provider.BaseColumns;

public class UserContract {

    private UserContract() {
    }

    public static class UserEntry implements BaseColumns {

        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_UUID = "user_uuid";
        public static final String COLUMN_NAME_FIRST_NAME = "first_name";
        public static final String COLUMN_NAME_LAST_NAME = "last_name";
        public static final String COLUMN_NAME_EMAIL = "user_email";
        public static final String COLUMN_NAME_HASHED_PASSWORD = "user_password";
        public static final String COLUMN_NAME_DATE_OF_BIRTH = "date_of_birth";
        public static final String COLUMN_NAME_GENDER = "gender";
        public static final String COLUMN_NAME_ROLE = "user_role";

    }

    public static final String SQL_CREATE_USER_TABLE =
            "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry.COLUMN_NAME_UUID + " TEXT," +
                    UserEntry.COLUMN_NAME_FIRST_NAME + " TEXT ," +
                    UserEntry.COLUMN_NAME_LAST_NAME + " TEXT ," +
                    UserEntry.COLUMN_NAME_EMAIL + " TEXT ," +
                    UserEntry.COLUMN_NAME_HASHED_PASSWORD + " TEXT ," +
                    UserEntry.COLUMN_NAME_DATE_OF_BIRTH + " TEXT ," +
                    UserEntry.COLUMN_NAME_GENDER + " TEXT ," +
                    UserEntry.COLUMN_NAME_ROLE + " TEXT )";

    public static final String SQL_DELETE_USER_TABLE =
            "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;


}
