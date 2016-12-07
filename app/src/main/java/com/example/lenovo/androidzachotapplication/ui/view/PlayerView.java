package com.example.lenovo.androidzachotapplication.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Lenovo on 12/7/2016.
 */

public class PlayerView extends View {

    private int progress;
    private String progressString = "00:00";
    private int duration;
    private String durationString = "23:23";
    private Paint progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint backGrayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int width;
    private int height;
    private OnChangeProgressListener listener;

    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Rect textContainer = new Rect();

    public PlayerView(Context context) {
        super(context);
        init();
    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setOnChangeProgressListener(OnChangeProgressListener listener) {
        this.listener = listener;
    }

    private void init() {
        textPaint.setTextSize(40);
        progressPaint.setStrokeWidth(20);
        backGrayPaint.setStrokeWidth(20);
        backGrayPaint.setColor(Color.LTGRAY);
        progressPaint.setColor(Color.parseColor("#1B5E20"));
    }

    public void setProgress(int progress) {
        this.progress = progress;
        this.progressString = DateUtils.formatElapsedTime(progress);
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                invalidate();
            }
        });
    }

    public void setDuration(int duration) {
        this.duration = duration;
        this.durationString = DateUtils.formatElapsedTime(duration);
        invalidate();
    }

    private void drawTexts(Canvas canvas) {
        int y = getMeasuredHeight() - 30;

        canvas.getClipBounds(textContainer);
        textPaint.getTextBounds(progressString, 0, progressString.length(), textContainer);

        //draw the left text
        canvas.drawText(progressString, 0, y, textPaint);

        canvas.getClipBounds(textContainer);
        textPaint.getTextBounds(durationString, 0, durationString.length(), textContainer);
        float x = getMeasuredWidth() - textContainer.width() - 20;

        //draw the right text
        canvas.drawText(durationString, x, y, textPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(10, height - 10, width - 10, height - 10, backGrayPaint);
        if (progress > 0) {
            float from = 10;
            float to = width * progress / duration;
            if (to < from) {
                to = from + 1;
            }
            canvas.drawLine(from, height - 10, to, height - 10, progressPaint);
        }
        drawTexts(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        setProgress((int) (duration * x / width));
        if(listener != null) {
            listener.onTouch(progress);
        }
        return true;
    }

    public interface OnChangeProgressListener {
        void onTouch(int progress);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        int desiredWidth = 200;
        int desiredHeight = 50;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }
        setMeasuredDimension(width, height);
    }
}
