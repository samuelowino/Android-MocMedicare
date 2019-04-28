package com.manage.hospital.hmapp.database.contracts;

import android.provider.BaseColumns;

public class DatabaseContract {

    private DatabaseContract() {
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

    public static class PatientEntry implements BaseColumns {

        public static final String TABLE_NAME = "patient";
        public static final String COLUMN_NAME_UUID = "uuid";
        public static final String COLUMN_NAME_FIRST_NAME = "first_name";
        public static final String COLUMN_NAME_LAST_NAME = "last_name";
        public static final String COLUMN_NAME_DOB = "dob";
        public static final String COLUMN_NAME_GENDER = "gender";
        public static final String COLUMN_NAME_WEIGHT = "weight";
        public static final String COLUMN_NAME_AGE = "age";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_CONTACT = "contact";
        public static final String COLUMN_NAME_ADDRESS = "address";
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

    public static final String SQL_CREATE_PATIENT_TABLE =
            "CREATE TABLE " + PatientEntry.TABLE_NAME + " ("+
                    PatientEntry.COLUMN_NAME_UUID + " TEXT," +
                    PatientEntry.COLUMN_NAME_FIRST_NAME + " TEXT," +
                    PatientEntry.COLUMN_NAME_LAST_NAME + " TEXT," +
                    PatientEntry.COLUMN_NAME_DOB + " TEXT," +
                    PatientEntry.COLUMN_NAME_GENDER + " TEXT," +
                    PatientEntry.COLUMN_NAME_WEIGHT + " TEXT," +
                    PatientEntry.COLUMN_NAME_AGE + " TEXT," +
                    PatientEntry.COLUMN_NAME_EMAIL + " TEXT," +
                    PatientEntry.COLUMN_NAME_CONTACT + " TEXT," +
                    PatientEntry.COLUMN_NAME_ADDRESS + " TEXT)";

    public static final String SQL_DELETE_USER_TABLE =
            "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;

    public static final String SQL_DELETE_PATIENT_TABLE =
            "DROP TABLE IF EXISTS " + PatientEntry.TABLE_NAME;










}
