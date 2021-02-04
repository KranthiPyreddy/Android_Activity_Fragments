package com.codewithpk.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.codewithpk.criminalintent.Crime;

@Database(entities = {Crime.class}, version = 1)
@TypeConverters({CrimeTypeConverters.class})
public abstract class CrimeDatabase extends RoomDatabase {
    public abstract CrimeDao crimeDao();
}
