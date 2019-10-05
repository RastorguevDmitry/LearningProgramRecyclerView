package com.rdi.learningprogram;

import com.rdi.learningprogram.models.Lecture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LearningProgramProvider {

    public List<Lecture> mLecture = Arrays.asList(
            new Lecture("1", "24.09.2019", "Вводное занятие", "Соколов"),
            new Lecture("2", "26.09.2019", "View, Layouts", "Соколов"),
            new Lecture("3", "28.09.2019", "Drawables", "Соколов"),
            new Lecture("4", "01.10.2019", "Activity", "Сафарян"),
            new Lecture("5", "03.10.2019", "Адаптеры", "Чумак"),
            new Lecture("6", "05.10.2019", "UI: практика", "Кудрявцев"),
            new Lecture("7", "08.10.2019", "Custom View", "Кудрявцев"),
            new Lecture("8", "10.10.2019", "Touch events", "Бильчук"),
            new Lecture("9", "12.10.2019", "Сложные жесты", "Соколов"),
            new Lecture("10", "24.09.2019", "Вводное занятие", "Соколов"),
            new Lecture("11", "26.09.2019", "View, Layouts", "Соколов"),
            new Lecture("12", "28.09.2019", "Drawables", "Соколов"),
            new Lecture("13", "01.10.2019", "Activity", "Сафарян"),
            new Lecture("14", "03.10.2019", "Адаптеры", "Чумак"),
            new Lecture("15", "05.10.2019", "UI: практика", "Кудрявцев"),
            new Lecture("16", "08.10.2019", "Custom View", "Кудрявцев"),
            new Lecture("17", "10.10.2019", "Touch events", "Бильчук"),
            new Lecture("18", "12.10.2019", "Сложные жесты", "Соколов")
    );


    public List<Lecture> providerLectures() {
        return mLecture;
    }

    public List<String> providerLector() {
        Set<String> lectorsSet = new HashSet<>();
        for (Lecture lecture : mLecture) {
            lectorsSet.add(lecture.getmLector());
        }
        return new ArrayList<>(lectorsSet);
    }

    public List<Lecture> filterBy(String lectorName) {
        List<Lecture> result = new ArrayList<>();
        for (Lecture lecture : mLecture) {
            if (lecture.getmLector().equals(lectorName)) {
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
            currentNumberOfWeek = (Integer.parseInt(((Lecture) lecture).getmNumber()) - 1) / 3 + 1;
            if (currentNumberOfWeek != numberOfWeekAdded) {
                mLectureWithWeek.add("Неделя " + currentNumberOfWeek);
                mLectureWithWeek.add(lecture);
                numberOfWeekAdded = currentNumberOfWeek;
            } else
                mLectureWithWeek.add(lecture);
        }
        return mLectureWithWeek;
    }
}
