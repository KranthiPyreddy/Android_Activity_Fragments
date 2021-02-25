package com.codewithpk.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.codewithpk.criminalintent.Crime;

@Database(entities = {Crime.class}, version = 2)
@TypeConverters({CrimeTypeConverters.class})
public abstract class CrimeDatabase extends RoomDatabase {
    public abstract CrimeDao crimeDao();
    //Adding database migration
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Crime ADD COLUMN suspect TEXT NOT NULL DEFAULT ''");
        }
    };
}
