package com.rdi.learningprogram.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rdi.learningprogram.R;
import com.rdi.learningprogram.adapters.LearningProgramAdapter;
import com.rdi.learningprogram.adapters.LectorSpinerAdapter;
import com.rdi.learningprogram.adapters.OnLectureClickListener;
import com.rdi.learningprogram.models.Lecture;
import com.rdi.learningprogram.provider.LearningProgramProvider;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

/**
 * Фрагмент со списком лекций
 *
 * @author Evgeny Chumak
 **/
public class LecturesFragment extends Fragment {

    private static final int POSITION_ALL = 0;
    private static final String SAVE_NUMBER_OF_LECTURE = "SAVE_NUMBER_OF_LECTURE";
    private LearningProgramProvider mLearningProgramProvider;
    private LearningProgramAdapter mLecturesAdapter;
    private RecyclerView mRecyclerView;
    List<?> lectures;
    List<String> lectors;
    Spinner lectorsSpinner;
    Spinner choseViewSpinner;
    LinearLayoutManager layoutManager;
    int positionStart = 1;
    private static Bundle mBundleRecyclerViewState;
    private OnLectureClickListener mOnLectureClickListener = ((lecture) ->
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.root, FragmentDetailLecture.newInstance(lecture))
                    .addToBackStack(FragmentDetailLecture.class.getSimpleName())
                    .commit());


    public static Fragment newInstance() {
        return new LecturesFragment();
    }

    {
        // нужно для того, чтобы инстанс LecturesProvider не убивался после смены конфигурации
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lectures, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lectorsSpinner = view.findViewById(R.id.lectors_spinner);
        choseViewSpinner = view.findViewById(R.id.chose_view_spinner);
        mRecyclerView = view.findViewById(R.id.learning_program_recycler);
        loadLearningProgram();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mBundleRecyclerViewState != null) {
            positionStart = mBundleRecyclerViewState.getInt(SAVE_NUMBER_OF_LECTURE);
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.scrollToPosition(positionStart);
                }
            });
            Log.d("onResume", "onResume: " + positionStart);
        }
    }

    private void initViewAfterLoadFinish() {
        mLecturesAdapter = new LearningProgramAdapter();
        lectors = mLearningProgramProvider.providerLector();
        initRecyclerView();
        filRecyclerView(mLearningProgramProvider.getLectures());
        initLectorsSpiner();
        initChoseViewSpinner();
    }

    private void loadLearningProgram() {
        mLearningProgramProvider = new LearningProgramProvider();
        lectures = mLearningProgramProvider.getLectures();
        if (lectures == null) {
            new LoadLecturesTask(this).execute();
        } else {
            initViewAfterLoadFinish();
        }
    }


    private void initLectorsSpiner() {

        Collections.sort(lectors);
        lectors.add(POSITION_ALL, getResources().getString(R.string.all));

        lectorsSpinner.setAdapter(new LectorSpinerAdapter(lectors));

        lectorsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                workWithTwoSpinner(position, choseViewSpinner.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void initChoseViewSpinner() {

        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(requireContext(), R.array.сhose_wiew_week, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        choseViewSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                workWithTwoSpinner(lectorsSpinner.getSelectedItemPosition(), position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void initRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), LinearLayout.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

    }

    private void filRecyclerView(List<?> mLecture) {
        mLecturesAdapter.setClickListener(mOnLectureClickListener);
        mLecturesAdapter.setLectures(lectures);
        mRecyclerView.setAdapter(mLecturesAdapter);

    }

    private void workWithTwoSpinner(int positionLectorsSpiner, int positionChoseViewSpinner) {

        if (positionLectorsSpiner == POSITION_ALL) {
            lectures = mLearningProgramProvider.providerLectures();
        } else {
            String lectorName = lectors.get(positionLectorsSpiner);
            lectures = mLearningProgramProvider.filterBy(lectorName);
        }

        if (positionChoseViewSpinner == 1) {        //С группировкой по неделям
            lectures = mLearningProgramProvider.choseViewGroup(lectures);
        }
        filRecyclerView(lectures);

    }

    private class LoadLecturesTask extends AsyncTask<Void, Void, List<Lecture>> {
        private final WeakReference<LecturesFragment> mFragmentRef;
        private final LearningProgramProvider mProvider;


        private LoadLecturesTask(LecturesFragment fragment) {
            mFragmentRef = new WeakReference<>(fragment);
            mProvider = fragment.mLearningProgramProvider;
        }

        @Override
        protected void onPreExecute() {
            LecturesFragment fragment = mFragmentRef.get();
            if (fragment != null) {

            }
        }

        @Override
        protected List<Lecture> doInBackground(Void... voids) {
            return mProvider.loadLecturesFromWeb();
        }

        @Override
        protected void onPostExecute(List<Lecture> lectures) {
            LecturesFragment fragment = mFragmentRef.get();
            if (fragment == null) {
                return;
            }
            //   mainActivity.mLoadingView.setVisibility(View.GONE);
            if (lectures == null) {
                Toast.makeText(fragment.requireContext(), R.string.error_loading, Toast.LENGTH_SHORT).show();
            } else {
                fragment.lectures = mProvider.getLectures();
                fragment.initViewAfterLoadFinish();

            }
        }
    }
}
