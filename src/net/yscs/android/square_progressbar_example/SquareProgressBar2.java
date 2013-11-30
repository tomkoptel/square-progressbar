package net.yscs.android.square_progressbar_example;

import android.content.Context;
import android.graphics.Color;
import android.os.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

import java.text.DecimalFormat;

@EViewGroup(resName = "square_pbv")
public class SquareProgressBar2 extends RelativeLayout {
    public static final String TAG = SquareProgressBar2.class.getSimpleName();
    private static final int MSG = 100;
    private static final long ONE_SECOND = 1000;

    @ViewById
    protected SquareProgressView bar;
    @ViewById
    protected ViewGroup timerContainer;
    @ViewById
    protected TextView seconds;

    private CountDownTimer countDownTimer;
    private long timerDuration;
    private long timeLeftToEnd;
    private long currentTimeInMillis;
    private double mProgress = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            synchronized (SquareProgressBar2.this) {
                final long frequencyUpdate = timerDuration / 100;
                if (mProgress <= 100) {
                    setProgress(mProgress);
                    mProgress++;
                    sendMessageDelayed(obtainMessage(MSG), frequencyUpdate);
                }
            }
        }
    };


    public SquareProgressBar2(Context context) {
        super(context);
    }

    public SquareProgressBar2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareProgressBar2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @AfterViews
    final void init() {
        bar.bringToFront();
        setWidth(6);
    }

    public void startCountDownTimer(long duration) {
        timerDuration = duration;
        startTimer(duration);
    }

    private void startTimer(final long duration) {
        cancel();
        resetProgress();
        startProgressUpdates();
        startCountDown(duration);
    }

    private void startCountDown(final long duration) {
        countDownTimer = new CountDownTimer(duration, 1000) {
            public void onTick(long millis) {
                timeLeftToEnd = millis;
                formatTime(millis);
            }

            public void onFinish() {
                timeLeftToEnd = 0;
                updateUITimer(0);
            }
        };
        countDownTimer.start();
    }

    private void startProgressUpdates() {
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
    }

    @UiThread
    protected void formatTime(long millis) {
        long seconds = millis / 1000;
        updateUITimer(seconds);
    }

    @UiThread
    protected void updateUITimer(long seconds) {
        DecimalFormat df = new DecimalFormat("00");
        this.seconds.setText(df.format(seconds));
    }

    public void setProgress(double progress) {
        bar.setProgress(progress);
    }

    private void resetProgress() {
        mProgress = 0;
        setProgress(0);
    }

    public void showProgress(boolean showProgress) {
        bar.setShowProgress(showProgress);
    }

    public boolean isShowProgress() {
        return bar.isShowProgress();
    }

    public void setHoloColor(int androidHoloColor) {
        bar.setColor(getContext().getResources().getColor(androidHoloColor));
    }

    public void setColor(String colorString) {
        bar.setColor(Color.parseColor(colorString));
    }

    public void setColorRGB(int r, int g, int b) {
        bar.setColor(Color.rgb(r, g, b));
    }

    public void setWidth(int width) {
        int padding1 = SquareProgressView.convertDpToPx(width, getContext());
        setPadding(padding1, padding1, padding1, padding1);
        bar.setWidthInDp(width);
        int padding2 = SquareProgressView.convertDpToPx(width / 2, getContext());
        timerContainer.setPadding(padding2, padding2, padding2, padding2);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        timerDuration = savedState.timerDuration;
        currentTimeInMillis = savedState.currentTimeInMillis;
        timeLeftToEnd = savedState.timeLeftToEnd;

        onResume();

        requestLayout();
    }


    public void onPause() {
        currentTimeInMillis = SystemClock.elapsedRealtime() + timeLeftToEnd;
        Log.d(TAG, "onPause=========================");
        Log.d(TAG, "timeLeftToEnd: " + timeLeftToEnd / 1000);
    }

    public void onResume() {
        if (timerDuration == 0) {
            mProgress = bar.getProgress();
            formatTime(timeLeftToEnd);
        } else {
            Log.d(TAG, "onResume=========================");
            Log.d(TAG, "Timer Duration: " + timerDuration);
            Log.d(TAG, "cached timeLeftToEnd: " + timeLeftToEnd / 1000);
            long timeLeftToEnd = currentTimeInMillis - SystemClock.elapsedRealtime();
            Log.d(TAG, "real timeLeftToEnd: " + timeLeftToEnd / 1000);

            if (timeLeftToEnd <= 0) {
                updateUITimer(0);
                setProgress(100);
            } else {
                long alreadyPassed = timerDuration - timeLeftToEnd;
                mProgress = (int) (alreadyPassed * 100 / timerDuration);
                Log.d(TAG, "Progress: " + (alreadyPassed * 100 / timerDuration));

                cancel();
                startProgressUpdates();
                startCountDown(timeLeftToEnd);
            }
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        Log.d(TAG, "onSaveInstanceState-------------------------");
        Log.d(TAG, "timeLeftToEnd: " + timeLeftToEnd / 1000);
        Log.d(TAG, "Progress: " + mProgress);
        savedState.currentTimeInMillis = SystemClock.elapsedRealtime() + timeLeftToEnd;
        savedState.timerDuration = timerDuration;
        savedState.timeLeftToEnd = timeLeftToEnd;

        cancel();

        return savedState;
    }

    public void stopCountDownTimer() {
        cancel();
        timerDuration = 0;
    }

    private void cancel() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        mHandler.removeMessages(MSG);
    }

    static class SavedState extends BaseSavedState {
        long currentTimeInMillis;
        long timerDuration;
        long timeLeftToEnd;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            long[] values = new long[3];
            in.readLongArray(values);
            currentTimeInMillis = values[0];
            timerDuration = values[1];
            timeLeftToEnd = values[2];
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeLongArray(new long[]{currentTimeInMillis, timerDuration, timeLeftToEnd});
        }

        @SuppressWarnings("UnusedDeclaration")
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
