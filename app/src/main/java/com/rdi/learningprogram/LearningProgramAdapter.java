package com.rdi.learningprogram;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rdi.learningprogram.models.Lecture;

import java.util.ArrayList;
import java.util.List;

public class LearningProgramAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Lecture> mLectures;
    private List<?> mAllLectures;

    private class VIEW_TYPES {
        public static final int Header = 1;
        public static final int Normal = 2;
    }

    public void setLectures(List<?> lectures) {
        mAllLectures = lectures == null ? null : new ArrayList<>(lectures);
        if (lectures == null) {
            mLectures = null;
        } else {
            mLectures = new ArrayList<>();
            for (Object lecture : lectures
            ) {
                if (lecture instanceof Lecture) {
                    mLectures.add((Lecture) lecture);
                }
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPES.Header) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_week, parent, false);
            return new WeekHolder(view);

        } else if (viewType == VIEW_TYPES.Normal) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lecture, parent, false);
            return new LectureHolder(view);

        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof WeekHolder) {
            ((WeekHolder) holder).mWeek.setText(mAllLectures.get(position).toString());
        } else if (holder instanceof LectureHolder) {
            Lecture lecture = (Lecture) mAllLectures.get(position);
            ((LectureHolder) holder).mNumber.setText(String.valueOf(lecture.getNumber()));
            ((LectureHolder) holder).mData.setText(lecture.getData());
            ((LectureHolder) holder).mTheme.setText(lecture.getTheme());
            ((LectureHolder) holder).mLector.setText(lecture.getLector());
        }
    }

    @Override
    public int getItemCount() {
        return mLectures == null ? 0 : mAllLectures.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return VIEW_TYPES.Header;
        }
        return VIEW_TYPES.Normal;
    }

    private boolean isPositionHeader(int position) {
        if (mAllLectures.get(position) instanceof Lecture) {
            return false;
        }
        return true;
    }


    static class LectureHolder extends RecyclerView.ViewHolder {
        private final TextView mNumber;
        private final TextView mData;
        private final TextView mTheme;
        private final TextView mLector;

        public LectureHolder(@NonNull View itemView) {
            super(itemView);
            mNumber = itemView.findViewById(R.id.number);
            mData = itemView.findViewById(R.id.date);
            mTheme = itemView.findViewById(R.id.theme);
            mLector = itemView.findViewById(R.id.lectore);
        }
    }

    static class WeekHolder extends RecyclerView.ViewHolder {
        private final TextView mWeek;

        public WeekHolder(@NonNull View itemView) {
            super(itemView);
            mWeek = itemView.findViewById(R.id.week);
        }
    }
}
