package com.rdi.learningprogram.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rdi.learningprogram.R;
import com.rdi.learningprogram.adapters.DetailsFragmentSubtopicsAdapter;
import com.rdi.learningprogram.models.Lecture;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDetailLecture extends Fragment {


    public FragmentDetailLecture() {
        // Required empty public constructor
    }

    private static final String ARG_LECTURE = "ARG_LECTURE";

    public static FragmentDetailLecture newInstance(@NonNull Lecture lecture) {
        FragmentDetailLecture fragment = new FragmentDetailLecture();
        Bundle args = new Bundle();
        args.putParcelable(ARG_LECTURE, lecture);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_deteil_lecture, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Lecture lecture = getLectureFromArgs();
        ((TextView) view.findViewById(R.id.number)).setText(String.valueOf(lecture.getNumber()));
        ((TextView) view.findViewById(R.id.date)).setText(lecture.getData());
        ((TextView) view.findViewById(R.id.theme)).setText(lecture.getTheme());
        ((TextView) view.findViewById(R.id.lectore)).setText(lecture.getLector());

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new DetailsFragmentSubtopicsAdapter(lecture.getSubtopics()));
    }

    @NonNull
    private Lecture getLectureFromArgs() {
        Bundle arguments = getArguments();
        if (arguments == null) {
            throw new IllegalStateException("Arguments must be set");
        }
        Lecture lecture = arguments.getParcelable(ARG_LECTURE);
        if (lecture == null) {
            throw new IllegalStateException("Lecture must be set");
        }
        return lecture;
    }
}
