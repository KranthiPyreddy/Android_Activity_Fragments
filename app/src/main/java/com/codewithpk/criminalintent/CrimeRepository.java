package com.codewithpk.criminalintent;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.codewithpk.database.CrimeDao;
import com.codewithpk.database.CrimeDatabase;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CrimeRepository {
    private final String DATABASE_NAME = "crime-database";
    private File mFilesDir;
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

        //After you create your Migration, you need to provide it to your database when it is created
        //Providing migration to Room

        CrimeDatabase database = Room.databaseBuilder(mContext.getApplicationContext(), CrimeDatabase.class, DATABASE_NAME).
                addMigrations(CrimeDatabase.MIGRATION_1_2).build();
        mCrimeDao = database.crimeDao();

        mFilesDir = context.getApplicationContext().getFilesDir();
    }
//Adding a getPhotoFile(Crime) function to CrimeRepository that provides
// a complete local file path for Crime’s image
    public File getPhotoFile(Crime crime) {
        return new File(mFilesDir, crime.getPhotoFilename());
    }
    public LiveData<List<Crime>>getCrimes() {
        return mCrimeDao.getCrimes();
    }
    public LiveData<Crime>getCrime(UUID id) {
        return mCrimeDao.getCrime(id);
    }
}
