package com.rdi.learningprogram.provider;

import androidx.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rdi.learningprogram.models.Lecture;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LearningProgramProvider {
    private static final String LECTURES_URL = "http://landsovet.ru/learning_program.json";

    public List<Lecture> mLecture;


    @Nullable
    public List<Lecture> getLectures() {
        return mLecture == null ? null : new ArrayList<>(mLecture);
    }

    public List<Lecture> providerLectures() {
        return mLecture;
    }

    public List<String> providerLector() {
        Set<String> lectorsSet = new HashSet<>();
        for (Lecture lecture : mLecture) {
            lectorsSet.add(lecture.getLector());
        }
        return new ArrayList<>(lectorsSet);
    }

    public List<Lecture> filterBy(String lectorName) {
        List<Lecture> result = new ArrayList<>();
        for (Lecture lecture : mLecture) {
            if (lecture.getLector().equals(lectorName)) {
                result.add(lecture);
            }
        }
        return result;
    }


    public List<Object> choseViewGroup(List<?> lectures) {
        List<Object> mLectureWithWeek = new ArrayList<>();
        int numberOfWeekAdded = 0;
        int currentNumberOfWeek;
        for (Object lecture : lectures) {
            currentNumberOfWeek = ((Lecture) lecture).getWeekIndex();
            if (currentNumberOfWeek != numberOfWeekAdded) {
                mLectureWithWeek.add("Неделя " + currentNumberOfWeek);
                mLectureWithWeek.add(lecture);
                numberOfWeekAdded = currentNumberOfWeek;
            } else
                mLectureWithWeek.add(lecture);
        }
        return mLectureWithWeek;
    }

    @Nullable
    public List<Lecture> loadLecturesFromWeb() {
        if (mLecture != null) {
            return mLecture;
        }
        InputStream is = null;
        try {
            final URL url = new URL(LECTURES_URL);
            URLConnection connection = url.openConnection();
            is = connection.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            Lecture[] lectures = mapper.readValue(is, Lecture[].class);
            mLecture = Arrays.asList(lectures);
            return new ArrayList<>(mLecture);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }



}
