package com.rdi.learningprogram;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rdi.learningprogram.adapters.LearningProgramAdapter;
import com.rdi.learningprogram.adapters.LectorSpinerAdapter;
import com.rdi.learningprogram.models.Lecture;
import com.rdi.learningprogram.provider.LearningProgramProvider;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int POSITION_ALL = 0;
    private static final String SAVE_NUMBER_OF_LECTURE = "SAVE_NUMBER_OF_LECTURE";
    private LearningProgramProvider mLearningProgramProvider;
    private LearningProgramAdapter adapter;
    private RecyclerView recyclerView;
    List<?> lectures;
    List<String> lectors;
    Spinner lectorsSpinner;
    Spinner choseViewSpinner;
    LinearLayoutManager layoutManager;
    int positionStart = 1;
    private static Bundle mBundleRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadLearningProgram();
    }

    private void initViewAfterLoadFinish() {
        adapter = new LearningProgramAdapter();
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


    @Override
    protected void onResume() {
        super.onResume();
        if (mBundleRecyclerViewState != null) {
            positionStart = mBundleRecyclerViewState.getInt(SAVE_NUMBER_OF_LECTURE);
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    recyclerView.scrollToPosition(positionStart);
                }
            });
            Log.d("onResume", "onResume: " + positionStart);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        positionStart = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition() + 1;
        mBundleRecyclerViewState = new Bundle();
        mBundleRecyclerViewState.putInt(SAVE_NUMBER_OF_LECTURE, positionStart);
    }

    private void initLectorsSpiner() {
        lectorsSpinner = findViewById(R.id.lectors_spinner);
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
        choseViewSpinner = findViewById(R.id.chose_view_spinner);
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.сhose_wiew_week, android.R.layout.simple_spinner_item);
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
        recyclerView = findViewById(R.id.learning_program_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void filRecyclerView(List<?> mLecture) {
        adapter.setLectures(lectures);
        recyclerView.setAdapter(adapter);
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
        private final WeakReference<MainActivity> mActivityRef;
        private final LearningProgramProvider mProvider;


        private LoadLecturesTask(MainActivity mainActivity) {
            mActivityRef = new WeakReference<>(mainActivity);
            mProvider = mainActivity.mLearningProgramProvider;
        }

        @Override
        protected void onPreExecute() {
            MainActivity mainActivity = mActivityRef.get();
            if (mainActivity != null) {
            //    mainActivity.mLoadingView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected List<Lecture> doInBackground(Void... voids) {
            return mProvider.loadLecturesFromWeb();
        }

        @Override
        protected void onPostExecute(List<Lecture> lectures) {
            MainActivity mainActivity = mActivityRef.get();
            if (mainActivity == null) {
                return;
            }
         //   mainActivity.mLoadingView.setVisibility(View.GONE);
            if (lectures == null) {
                Toast.makeText(mainActivity.getBaseContext(), R.string.error_loading, Toast.LENGTH_SHORT).show();
            } else {
                mainActivity.lectures = mProvider.getLectures();
                mainActivity.initViewAfterLoadFinish();

            }
        }
    }
}
