package net.yscs.android.square_progressbar_example;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.Paint.Style;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

public class SquareProgressView extends View {
    public static final String TAG = SquareProgressView.class.getSimpleName();
    private static final boolean DEBUG = false;
    private double progress;
    private Paint progressBarPaint;

    private float widthInDp = 0;
    private float mOffloat = 0;

    private boolean showProgress = false;


    public SquareProgressView(Context context) {
        this(context, null);
    }

    public SquareProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.spbProgressViewStyle);
    }

    public SquareProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) return;

        //Load defaults from resources
        final Resources res = getResources();
        final int defaultProgressBarColor = res.getColor(R.color.default_progress_bar_color);
        final float defaultOffloatWidth = res.getDimension(R.dimen.default_progress_offloat_width);
        final float defaultBarWidth = res.getDimension(R.dimen.default_progress_bar_width);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SquareProgressView, defStyle, 0);
        int progressBarColor = a.getColor(R.styleable.SquareProgressView_progressColor, defaultProgressBarColor);
        mOffloat = a.getDimension(R.styleable.SquareProgressView_progressOffloat, defaultOffloatWidth);
        widthInDp = a.getDimension(R.styleable.SquareProgressView_progressWidth, defaultBarWidth);

        progressBarPaint = new Paint();
        progressBarPaint.setColor(progressBarColor);
        progressBarPaint.setStrokeWidth(1);
        progressBarPaint.setStyle(Style.FILL_AND_STROKE);
        progressBarPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        log(String.format("canvas(width,height): (%d, %d)",
                canvas.getWidth(), canvas.getHeight()));

        float scope = canvas.getWidth() + canvas.getHeight()
                + canvas.getHeight() + canvas.getWidth();
        float percent = (scope / 100) * Float.valueOf(String.valueOf(progress));
        float halfOfTheImage = canvas.getWidth() / 2;

        float left, top, right, bottom;
        if (percent > halfOfTheImage) {
            paintFirstHalfOfTheTop(canvas);
            float second = percent;

            if (second > canvas.getWidth()) {
                paintSecondHalfOfTheTop(canvas);
                float third = second - canvas.getWidth();

                if (third > canvas.getHeight()) {
                    paintRightSide(canvas);
                    float forth = third - canvas.getHeight();

                    if (forth > halfOfTheImage) {
                        paintBottomSide(canvas);
                        float fifth = forth;

                        if (fifth > canvas.getWidth()) {
                            paintLeftSide(canvas);

                            float sixth = fifth - canvas.getWidth();
                            left = 0;
                            bottom = canvas.getHeight() - widthInDp;
                            right = widthInDp;
                            top = canvas.getHeight() - sixth;

                            drawPathLeft(canvas, left, top, right, bottom);

                            log(String.format("%s -> (%.0f,%d) moveTo(x,y): (%.0f,%.0f) lineTo(x,y): (%.0f,%.0f)", "(fifth > canvas.getWidth())",
                                    sixth, canvas.getHeight(), left, top, right, bottom));

                        } else {
                            right = canvas.getWidth() - widthInDp;
                            top = canvas.getHeight() - widthInDp;
                            left = canvas.getWidth() - forth;
                            bottom = canvas.getHeight();

                            drawPathBottom(canvas, left, top, right, bottom);

                            log(String.format("%s -> (%.0f,%d) moveTo(x,y): (%.0f,%.0f) lineTo(x,y): (%.0f,%.0f)", "(fifth < canvas.getWidth())",
                                    fifth, canvas.getWidth(), left, top, right, bottom));
                        }
                    } else {
                        right = canvas.getWidth() - widthInDp;
                        top = canvas.getHeight() - widthInDp;
                        left = canvas.getWidth() - forth;
                        bottom = canvas.getHeight();

                        drawPathBottom(canvas, left, top, right, bottom);

                        log(String.format("%s -> (%.0f,%.0f) moveTo(x,y): (%.0f,%.0f) lineTo(x,y): (%.0f,%.0f)", "(forth < halfOfTheImage)",
                                forth, halfOfTheImage, left, top, right, bottom));
                    }

                } else {
                    left = canvas.getWidth() - widthInDp;
                    top = widthInDp;
                    right = canvas.getWidth();
                    bottom = widthInDp + third;
                    drawPathRight(canvas, left, top, right, bottom);

                    log(String.format("%s -> (%.0f,%d) moveTo(x,y): (%.0f,%.0f) lineTo(x,y): (%.0f,%.0f)", "(third < canvas.getHeight())",
                            third, canvas.getHeight(), left, top, right, bottom));
                }
            } else {
                left = canvas.getWidth() / 2;
                top = 0;
                right = second;
                bottom = widthInDp;

                drawPathTop(canvas, left, top, right, bottom);

                log(String.format("%s -> (%.0f,%d) moveTo(x,y): (%.0f,%.0f) lineTo(x,y): (%.0f,%.0f)", "(second < canvas.getWidth())",
                        second, canvas.getWidth(), left, top, right, bottom));
            }

        } else {
            left = 0;
            top = 0;
            right = percent;
            bottom = widthInDp;

            drawPathTop(canvas, left, top, right, bottom);

            log(String.format("%s -> (%.0f,%.0f) moveTo(x,y): (%.0f,%.0f) lineTo(x,y): (%.0f,%.0f)", "(percent < halfOfTheImage)",
                    percent, halfOfTheImage, left, top, right, bottom));
        }
    }

    public void paintFirstHalfOfTheTop(Canvas canvas) {
        float moveToX = 0;
        float moveToY = 0;
        float lineToX = canvas.getWidth() / 2;
        float lineToY = widthInDp;
        drawRect(canvas, moveToX, moveToY, lineToX, lineToY);

        log(String.format("%s moveTo(x,y): (%.0f,%.0f) lineTo(x,y): (%.0f,%.0f)", "paintFirstHalfOfTheTop",
                moveToX, moveToY, lineToX, lineToY));
    }

    public void paintSecondHalfOfTheTop(Canvas canvas) {
        float moveToX = canvas.getWidth() / 2;
        float moveToY = 0;
        float lineToX = canvas.getWidth() + (widthInDp / 2);
        float lineToY = widthInDp;
        drawRect(canvas, moveToX, moveToY, lineToX, lineToY);

        log(String.format("%s moveTo(x,y): (%.0f,%.0f) lineTo(x,y): (%.0f,%.0f)", "paintSecondHalfOfTheTop",
                moveToX, moveToY, lineToX, lineToY));
    }

    public void paintRightSide(Canvas canvas) {
        float moveToX = canvas.getWidth() - widthInDp;
        float moveToY = widthInDp;
        float lineToX = canvas.getWidth();
        float lineToY = canvas.getHeight();
        drawRect(canvas, moveToX, moveToY, lineToX, lineToY);

        log(String.format("%s moveTo(x,y): (%.0f,%.0f) lineTo(x,y): (%.0f,%.0f)", "paintRightSide",
                moveToX, moveToY, lineToX, lineToY));
    }

    public void paintBottomSide(Canvas canvas) {
        float lineToX = canvas.getWidth() - widthInDp;
        float moveToY = canvas.getHeight() - widthInDp;
        float moveToX = canvas.getWidth() / 2;
        float lineToY = canvas.getHeight();
        drawRect(canvas, moveToX, moveToY, lineToX, lineToY);

        log(String.format("%s moveTo(x,y): (%.0f,%.0f) lineTo(x,y): (%.0f,%.0f)", "paintBottomSide",
                moveToX, moveToY, lineToX, lineToY));
    }

    public void paintLeftSide(Canvas canvas) {
        float lineToX = canvas.getWidth() - widthInDp;
        float moveToY = canvas.getHeight() - widthInDp;
        float moveToX = 0;
        float lineToY = canvas.getHeight();
        drawRect(canvas, moveToX, moveToY, lineToX, lineToY);

        log(String.format("%s moveTo(x,y): (%.0f,%.0f) lineTo(x,y): (%.0f,%.0f)", "paintLeftSide",
                moveToX, moveToY, lineToX, lineToY));
    }

    private void drawRect(Canvas canvas, float left, float top, float right, float bottom) {
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(left, top);
        path.lineTo(right, top);
        path.lineTo(right, bottom);
        path.lineTo(left, bottom);
        path.close();
        canvas.drawPath(path, progressBarPaint);
    }

    private void drawPathTop(Canvas canvas, float left, float top, float right, float bottom) {
        boolean widthIsZero = (right - left == 0);
        if (widthIsZero) return;

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(left, top);
        path.lineTo(right, top);
        path.lineTo(right - mOffloat, bottom);
        path.lineTo(left, bottom);
        path.close();

        canvas.drawPath(path, progressBarPaint);
    }

    private void drawPathRight(Canvas canvas, float left, float top, float right, float bottom) {
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(left, top);
        path.lineTo(right, top);
        path.lineTo(right, bottom);
        path.lineTo(left, bottom - mOffloat);
        path.close();

        canvas.drawPath(path, progressBarPaint);
    }

    private void drawPathBottom(Canvas canvas, float left, float top, float right, float bottom) {
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(left + mOffloat, top);
        path.lineTo(right, top);
        path.lineTo(right, bottom);
        path.lineTo(left, bottom);
        path.close();

        canvas.drawPath(path, progressBarPaint);
    }

    private void drawPathLeft(Canvas canvas, float left, float top, float right, float bottom) {
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(left, top);
        path.lineTo(right, top + mOffloat);
        path.lineTo(right, bottom);
        path.lineTo(left, bottom);
        path.close();

        canvas.drawPath(path, progressBarPaint);
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
        this.invalidate();
    }

    public void setColor(int color) {
        progressBarPaint.setColor(color);
        this.invalidate();
    }

    /**
     * @return the border
     */
    public float getWidthInDp() {
        return widthInDp;
    }

    /**
     * @return the border
     */
    public void setWidthInDp(int width) {
        this.widthInDp = convertDpToPx(
                width, getContext());
        this.invalidate();
    }


    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
        this.invalidate();
    }

    private void log(String message) {
        if (DEBUG) {
            Log.d(TAG, message);
        }
    }

    public static int convertDpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState)state;
        super.onRestoreInstanceState(savedState.getSuperState());
        progress = savedState.currentProgress;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentProgress = progress;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        double currentProgress;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentProgress = in.readDouble();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeDouble(currentProgress);
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
