package com.example.observer.crazymath;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int []  colors;
    private RelativeLayout background;
    private static Random ran_img;
    private TextView text_counter, mark;
//    private Handler mHandler;
//    private Runnable mHandlerTask;
    private boolean check_result;
    private static final int time_delay = 8;
    private static final int MAX_PROGRESS_BAR = 100;
    private static int mark_level = 0;
    private static Counter counter;
    private static CountTask countTask;
    private Fragment questionFragment;
    private FragmentManager fm;
    private ProgressBar bar;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        background = (RelativeLayout) findViewById(R.id.background_img);
        text_counter = (TextView) findViewById(R.id.text_counter);
        mark = (TextView) findViewById(R.id.mark);
        bar = (ProgressBar) findViewById(R.id.timing);
        fm = getFragmentManager();
        questionFragment = (QuestionContinue) fm.findFragmentById(R.id.dialog_fragment);
        hideFragment();

        colors = new int[3];
        colors[0] = R.color.colorAccent;
        colors[1] = R.color.colorPrimary;
        colors[2] = R.color.colorPrimaryDark;

        ran_img = new Random();
        int ran_no = ran_img.nextInt(3);
        background.setBackgroundColor(getResources().getColor(colors[ran_no]));
        counter = new Counter();
        text_counter.setText(counter.getResultString());
        text_counter.setTag(counter);

//        startTimer(time_delay);
        countTask = new CountTask();
        countTask.execute(MAX_PROGRESS_BAR);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bar.setProgress(0);
        mark.setText(0 + "");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

//    private void startTimer(final long time) {
//        mHandler = new Handler();
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //mHandler.postDelayed(this, time);
//            }
//        }, time);
//    }

    private class CountTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {
            for(; count <= params[0]; count++) {
                if (isCancelled()) break;
                try {
                    Thread.sleep((time_delay*1000)/MAX_PROGRESS_BAR);
                    publishProgress(count);
                } catch (Exception ex) {
                     ex.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            bar.setProgress(values[0]);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onPostExecute(String result) {
            if(!check_result && bar.getProgress() >= MAX_PROGRESS_BAR) {
                setFragment();
//                resetView();
                bar.setProgress(0);
                cancel(true);
            }
        }
    }

    private void resetView() {
        background.setBackgroundColor(getResources().getColor(colors[ran_img.nextInt(3)]));
        counter = new Counter();
        text_counter.setText(counter.getResultString());
        text_counter.setTag(counter);
        count = 0;
    }

    private void runCheck() {
        countTask.cancel(true);
        if(check_result) {
            mark_level++;
        } else {
            setFragment();
            mark_level = 0;
        }
        mark.setText(String.valueOf(mark_level));
        resetView();
        countTask = new CountTask();
        countTask.execute(MAX_PROGRESS_BAR);
    }

    private boolean checkResult(Counter counter) {
        return (counter.left + counter.right) == counter.result;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_right :
                if(checkResult((Counter)text_counter.getTag())) check_result = true;
                else check_result = false;
                break;
            case R.id.btn_wrong :
                if(!checkResult((Counter)text_counter.getTag())) check_result = true;
                else check_result = false;
                break;
        }
        runCheck();
    }

    private void setFragment() {
        if(questionFragment.isHidden())
            fm.beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .show(questionFragment)
                    .commit();
    }

    private void hideFragment() {
        fm.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .hide(questionFragment)
                .commit();
    }
}
