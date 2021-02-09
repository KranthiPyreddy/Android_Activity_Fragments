package com.codewithpk.criminalintent;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.codewithpk.database.CrimeDao;
import com.codewithpk.database.CrimeDatabase;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CrimeRepository {
    private final String DATABASE_NAME = "crime-database";
    private CrimeDao mCrimeDao;
    private static CrimeRepository sCrimeRepository;
    private Context mContext;
//Add a property to the executor to hold a reference, then execute your insert and update functions using the executor
    private ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    //Inserting and updating with an executor
    public void updateCrime(Crime crime) {
        mExecutor.execute(() -> {
            mCrimeDao.updateCrime(crime);
        });
    }
    public void addCrime(Crime crime) {
        mExecutor.execute(() -> {
            mCrimeDao.addCrime(crime);
        });
    }
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
