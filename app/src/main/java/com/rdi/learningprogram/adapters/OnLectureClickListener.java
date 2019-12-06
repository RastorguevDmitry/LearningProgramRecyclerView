package com.rdi.learningprogram.adapters;

import androidx.annotation.NonNull;

import com.rdi.learningprogram.models.Lecture;

public interface OnLectureClickListener {

    /**
     * Обрабатывает нажатие на элемент списка с переданной лекцией
     *
     * @param lecture на элемент списка с какой лекцией нажали
     */
    void onItemClick(@NonNull Lecture lecture);
}