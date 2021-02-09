package com.codewithpk.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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
    //Adding update and insert database functions
    @Update
    void updateCrime(Crime crime);
    @Insert
    void addCrime(Crime crime);
}
