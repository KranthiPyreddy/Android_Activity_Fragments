package com.codewithpk.criminalintent;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import java.util.UUID;

public class CrimeFragment extends Fragment {
    private static final String ARG_CRIME_ID = "crime_id";
    private static final String TAG = "CrimeFragment";
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private CrimeDetailViewModel mCrimeDetailViewModel;

    //Writing a newInstance(UUID) function
    /*write a newInstance(UUID) function that accepts a UUID,
     creates an arguments bundle, creates a fragment instance,
     and then attaches the arguments to the fragment */

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCrime = new Crime();
    //Hooking CrimeFragment up to CrimeDetailViewModel
        ViewModelProvider provider = ViewModelProviders.of(this);
        mCrimeDetailViewModel = provider.get(CrimeDetailViewModel.class);
        mCrimeDetailViewModel.initData(this.getContext());
        //Getting crime ID from the arguments
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        Log.d(TAG, "args bundle crime ID: " + crimeId);
        // Eventually, load crime from database
        mCrimeDetailViewModel.loadCrime(crimeId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText) v.findViewById(R.id.crime_title);

        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
// This space intentionally left blank
            }
            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
// This space intentionally left blank
            }
        });
        mDateButton = (Button) v.findViewById(R.id.crime_date);
        mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setEnabled(false);

        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });
        return v;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCrimeDetailViewModel.mCrimeLiveData.observe(this.getViewLifecycleOwner(), new Observer<Crime>() {
            @Override
            public void onChanged(@Nullable final Crime crime) {
                mCrime = crime;
                updateUI();
            }
        });
    }
    //update CrimeFragment to save the user’s edited crime data to the database
    @Override
    public void onStop() {
        super.onStop();
        mCrimeDetailViewModel.saveCrime(mCrime);
    }
    private void updateUI() {
        mTitleField.setText(mCrime.getTitle());
        mDateButton.setText(mCrime.getDate().toString());
        //mSolvedCheckBox.setChecked(mCrime.isSolved());
        //code to skip the checkbox animation
        mSolvedCheckBox.jumpDrawablesToCurrentState();
    }
}
