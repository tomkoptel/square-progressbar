package net.yscs.android.square_progressbar_example;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(resName = "my_main")
public class TestActivity extends FragmentActivity {
    public static final String TAG = TestActivity.class.getSimpleName();
    
    @ViewById
    protected SeekBar progressSeekBar;
    @ViewById
    protected SeekBar widthSeekBar;
    @ViewById
    protected SquareProgressBar2 squareProgressBar;
    @ViewById
    protected TextView progressDisplay;
    @ViewById
    protected Button startTimer;

    @Click
    final void startTimer() {
        String label = startTimer.getText().toString();
        if (label.equals("Start")) {
            startTimer.setText("Stop");
            squareProgressBar.startCountDownTimer(120000);
        } else {
            startTimer.setText("Start");
            squareProgressBar.stopCountDownTimer();
        }

    }

    @AfterViews
    final void init() {
//        squareProgressBar.startCountDownTimer(20000);

//        progressSeekBar
//                .setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//
//                    @Override
//                    public void onStopTrackingTouch(SeekBar seekBar) {
//                        // nothing to do
//                    }
//
//                    @Override
//                    public void onStartTrackingTouch(SeekBar seekBar) {
//                        // nothing to do
//                    }
//
//                    @Override
//                    public void onProgressChanged(SeekBar seekBar,
//                                                  int progress, boolean fromUser) {
//                        squareProgressBar.setProgress(progress);
//                        progressDisplay.setText(progress + "%");
//                    }
//                });
//        widthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                // nothing to do
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                // nothing to do
//            }
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress,
//                                          boolean fromUser) {
//                squareProgressBar.setWidth(progress);
//            }
//        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "oncreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        squareProgressBar.onResume();
        Log.d(TAG, "onresume");
    };

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onstart");
    };

    @Override
    protected void onPause() {
        super.onPause();
        squareProgressBar.onPause();
        Log.d(TAG, "onpause");
    };

    @Override
    protected void onDestroy() {
        Log.d(TAG, "ondestroy");
        super.onDestroy();
    };
}
