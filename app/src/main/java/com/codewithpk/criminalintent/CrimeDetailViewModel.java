package com.codewithpk.criminalintent;

import android.content.Context;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.util.UUID;

public class CrimeDetailViewModel extends ViewModel {
    MutableLiveData<UUID> mCrimeIdLiveData = new MutableLiveData<>();
    LiveData<Crime> mCrimeLiveData;
    CrimeRepository mCrimeRepository;
    public CrimeDetailViewModel() {
    }
    public void initData(Context context) {
        mCrimeRepository = CrimeRepository.get(context);
        mCrimeLiveData = Transformations.switchMap(mCrimeIdLiveData, new Function<UUID, LiveData<Crime>>() {
            @Override
            public LiveData<Crime> apply(UUID crimeId) {
                return mCrimeRepository.getCrime(crimeId);
            }
        });
    }
    public void loadCrime(UUID crimeId) {
        mCrimeIdLiveData.setValue(crimeId);
    }
    //add a function to save a crime object to the database
    //Adding save capability
    public void saveCrime(Crime crime) {
        mCrimeRepository.updateCrime(crime);
    }

//Add a function to CrimeDetailViewModel to expose the file information to CrimeFragment
    public File getPhotoFile(Crime crime) {
        return mCrimeRepository.getPhotoFile(crime);
    }
}

