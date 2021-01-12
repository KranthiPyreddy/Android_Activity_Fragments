package com.codewithpk.criminalintent;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

public class CrimeListViewModel extends ViewModel {
    List<Crime> mCrimes;
    public CrimeListViewModel() {
        mCrimes = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0);
            mCrimes.add(crime);
        }
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }
    public static class CrimeListFragment extends Fragment {
        private static String TAG = "CrimeListFragment";
        private CrimeListViewModel mCrimeListViewModel;

        public static CrimeListFragment newInstance() {
            return new CrimeListFragment();
        }
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ViewModelProvider provider = ViewModelProviders.of(this);
            mCrimeListViewModel = provider.get(CrimeListViewModel.class);
            Log.d(TAG, "Total crimes: " + mCrimeListViewModel.getCrimes().size());
        }
}
}
