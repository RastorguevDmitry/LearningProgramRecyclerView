package com.rdi.learningprogram.models;

import androidx.annotation.NonNull;

public class Lecture {

    private final String mNumber;
    private final String mData;
    private final String mTheme;
    private final String mLector;

    public String getmNumber() {
        return mNumber;
    }

    public String getmData() {
        return mData;
    }

    public String getmTheme() {
        return mTheme;
    }

    public String getmLector() {
        return mLector;
    }

    public Lecture(
            @NonNull String mNumber,
            @NonNull String mData,
            @NonNull String mTheme,
            @NonNull String mLector) {
        this.mNumber = mNumber;
        this.mData = mData;
        this.mTheme = mTheme;
        this.mLector = mLector;
    }


}
