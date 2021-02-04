package com.codewithpk.criminalintent;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.UUID;
@Entity
public class Crime {
    @PrimaryKey
    @NonNull
    private UUID id;
    @NonNull
    private String title;
    @NonNull
    private Date date;
    private boolean isSolved;
    public Crime() {
        id = UUID.randomUUID();
        date = new Date();
    }

    public void setId(@NonNull UUID id) {
        this.id = id;
    }

    //Getter
    public UUID getId() {
        return id;
    }
//Getter & Setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public void setSolved(boolean solved) {
        isSolved = solved;
    }
}
