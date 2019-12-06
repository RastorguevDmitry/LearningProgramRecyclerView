package com.rdi.learningprogram.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Lecture implements Parcelable {
    private static final int LECTURES_PER_WEEK = 3;

    private final int mNumber;
    private final String mData;
    private final String mTheme;
    private final String mLector;
    private final int weekIndex;
    private final List<String> mSubtopics;

    @JsonCreator
    public Lecture(
                   @JsonProperty("number") int number,
                   @JsonProperty("date") @NonNull String date,
                   @JsonProperty("theme") @NonNull String theme,
                   @JsonProperty("lector") @NonNull String lector,
                   @JsonProperty("subtopics") @NonNull List<String> subtopics) {
        mNumber = number;
        mData = date;
        mTheme = theme;
        mLector = lector;
        mSubtopics = new ArrayList<>(subtopics);
        weekIndex = (mNumber - 1) / LECTURES_PER_WEEK;
    }


    public static final Creator<Lecture> CREATOR = new Creator<Lecture>() {
        @Override
        public Lecture createFromParcel(Parcel in) {
            return new Lecture(in);
        }

        @Override
        public Lecture[] newArray(int size) {
            return new Lecture[size];
        }
    };

    public int getNumber() {
        return mNumber;
    }

    public String getData() {
        return mData;
    }

    public String getTheme() {
        return mTheme;
    }

    public String getLector() {
        return mLector;
    }

    public int getWeekIndex() {
        return weekIndex;
    }

    public List<String> getSubtopics() {
        return mSubtopics == null ? null : new ArrayList<>(mSubtopics);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mNumber);
        dest.writeString(mData);
        dest.writeString(mTheme);
        dest.writeString(mLector);
        dest.writeInt(weekIndex);
        dest.writeStringList(mSubtopics);
    }

    protected Lecture(Parcel in) {
        mNumber = in.readInt();
        mData = in.readString();
        mTheme = in.readString();
        mLector = in.readString();
        weekIndex = in.readInt();
        mSubtopics = in.createStringArrayList();
    }

    }
