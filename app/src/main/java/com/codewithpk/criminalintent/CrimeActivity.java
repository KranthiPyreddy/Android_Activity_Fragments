package com.codewithpk.criminalintent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import java.util.UUID;

public class CrimeActivity extends AppCompatActivity implements CrimeListFragment.Callbacks {
    private static String TAG = "CrimeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            //fragment = new CrimeFragment();
            fragment = CrimeListFragment.newInstance();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
    @Override
    public void onCrimeSelected(UUID crimeId) {
        //Log.d(TAG, "MainActivity.onCrimeSelected: " + crimeId);
        //Fragment fragment = new CrimeFragment();
        Fragment fragment = CrimeFragment.newInstance(crimeId);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}