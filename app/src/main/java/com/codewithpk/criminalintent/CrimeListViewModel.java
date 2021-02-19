package com.codewithpk.criminalintent;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CrimeListViewModel extends ViewModel {
    LiveData<List<Crime>> mCrimeListLiveData;
    CrimeRepository mCrimeRepository;
    public CrimeListViewModel() {
        /* mCrimes = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0);
            mCrimes.add(crime);
        } */
    }
    public void initData(Context context) {
        /* CrimeRepository crimeRepository = CrimeRepository.get(context);
        mCrimeListLiveData = crimeRepository.getCrimes(); */
        mCrimeRepository = CrimeRepository.get(context);
        mCrimeListLiveData = mCrimeRepository.getCrimes();
    }
    public LiveData<List<Crime>>getCrimes() {
        return mCrimeListLiveData;
    }
    //Adding a new crime
    public void addCrime(Crime crime) {
        mCrimeRepository.addCrime(crime);
    }
}

