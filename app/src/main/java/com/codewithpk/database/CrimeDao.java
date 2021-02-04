package com.codewithpk.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.codewithpk.criminalintent.Crime;

import java.util.List;
import java.util.UUID;

@Dao
public interface CrimeDao {
    @Query("SELECT * FROM crime")
    //List<Crime> getCrimes();
    LiveData<List<Crime>> getCrimes();
    @Query("SELECT * FROM crime WHERE id=:id")
    //Crime getCrime(UUID id);
    LiveData<Crime> getCrime(UUID id);
}
