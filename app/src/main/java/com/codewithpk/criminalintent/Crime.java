
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
    //give Crime a property that will hold the name of a suspect
    @NonNull
    private String suspect;
    public Crime() {
        id = UUID.randomUUID();
        date = new Date();
        title = "";
        isSolved = false;
        suspect = "";
    }
    // to give your pictures a place to live on disk locally
    public String getPhotoFilename() {
        return "IMG_" + this.id + ".jpg";
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

    public String getSuspect() {
        return suspect;
    }
    public void setSuspect(String suspect) {
        this.suspect = suspect;
    }
}
