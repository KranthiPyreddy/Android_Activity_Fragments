package com.codewithpk.criminalintent;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeListFragment extends Fragment {
    private static String TAG = "CrimeListFragment";
    private CrimeListViewModel mCrimeListViewModel;
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    /**
     * Required interface for hosting activities
     */
    interface Callbacks {
        void onCrimeSelected(UUID crimeId);
    }
    private Callbacks mCallbacks = null;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view
                .findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CrimeAdapter(new ArrayList<Crime>());
        mCrimeRecyclerView.setAdapter(mAdapter);
        //updateUI();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Observer<List<Crime>> crimesObserver = new Observer<List<Crime>>() {
            @Override
            public void onChanged(@Nullable final List<Crime> crimes) {
                updateUI(crimes);
            }
        };
        mCrimeListViewModel.getCrimes().observe(this.getViewLifecycleOwner(), crimesObserver);
    }
    //Override onAttach(Context) and onDetach() to set and unset the callbacks property
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
    private void updateUI(List<Crime> crimes) {
        //List<Crime> crimes = mCrimeListViewModel.getCrimes();
        mAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mAdapter);
    }
    public static CrimeListFragment newInstance() {
        return new CrimeListFragment();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = ViewModelProviders.of(this);
        mCrimeListViewModel = provider.get(CrimeListViewModel.class);
        mCrimeListViewModel.initData(this.getContext());
        //Log.d(TAG, "Total crimes: " + mCrimeListViewModel.getCrimes().size());
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Crime mCrime;
        private ImageView mSolvedImageView;
        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved);
        }
        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
        }
        @Override
        public void onClick(View view) {
            /* Toast.makeText(getActivity(),
                    mCrime.getTitle() + " clicked!", Toast.LENGTH_SHORT)
                    .show(); */
            if (mCallbacks != null) {
                mCallbacks.onCrimeSelected(mCrime.getId());
            }
        }
    }
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;
        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
        }
        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}


