package com.codewithpk.criminalintent;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.codewithpk.database.CrimeDao;
import com.codewithpk.database.CrimeDatabase;

import java.util.List;
import java.util.UUID;

public class CrimeRepository {
    private final String DATABASE_NAME = "crime-database";
    private CrimeDao mCrimeDao;
    private static CrimeRepository sCrimeRepository;
    private Context mContext;

    public static CrimeRepository get(Context context) {
        if (sCrimeRepository == null) {
            sCrimeRepository = new CrimeRepository(context);
        }
        return sCrimeRepository;
    }
    private CrimeRepository(Context context) {
        mContext = context;
        CrimeDatabase database = Room.databaseBuilder(mContext.getApplicationContext(), CrimeDatabase.class, DATABASE_NAME).build();
        mCrimeDao = database.crimeDao();
    }
    public LiveData<List<Crime>>getCrimes() {
        return mCrimeDao.getCrimes();
    }
    public LiveData<Crime>getCrime(UUID id) {
        return mCrimeDao.getCrime(id);
    }
}
