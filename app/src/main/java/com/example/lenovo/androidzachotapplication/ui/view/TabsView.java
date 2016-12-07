package com.example.lenovo.androidzachotapplication.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.lenovo.androidzachotapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 12/6/2016.
 */

public class TabsView extends LinearLayout {
    private List<TextView> titles;
    private int textColor;
    private int selectedTabColor;
    private int currentTab = 0;
    private OnTabChangedListener tabChangedListener;
    private OnClickListener onTabClick;

    public void setOnTabChangedListener(OnTabChangedListener listener) {
        tabChangedListener = listener;
    }

    public TabsView(Context context) {
        super(context);
        init(context, null);
    }

    public TabsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TabsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        titles = new ArrayList<>();
        tabChangedListener = new OnTabChangedListener() {
            @Override
            public void onTabChanged(int position) {

            }
        };
        onTabClick = new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTab(titles.indexOf(v));
            }
        };
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TabsView, 0, 0);
        try {
            textColor = typedArray.getColor(R.styleable.TabsView_textColor, Color.BLACK);
            selectedTabColor = typedArray.getColor(R.styleable.TabsView_selectedTabColor, Color.GREEN);
        } finally {
            typedArray.recycle();
        }
    }

    public void addTab(String title) {
        TextView titleTextView = new TextView(getContext());
        LayoutParams layoutParams = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        titleTextView.setLayoutParams(layoutParams);
        titleTextView.setText(title.toUpperCase());
        titleTextView.setGravity(Gravity.CENTER);
        titleTextView.setTextColor(selectedTabColor);
        addView(titleTextView);
        if (titles.isEmpty()) {
            titleTextView.setBackgroundColor(selectedTabColor);
            titleTextView.setTextColor(textColor);
        }
        titles.add(titleTextView);
        titleTextView.setOnClickListener(onTabClick);
    }

    public void changeTab(int index) {
        if (currentTab != index) {
            titles.get(currentTab).setBackgroundColor(Color.TRANSPARENT);
            titles.get(currentTab).setTextColor(selectedTabColor);
            titles.get(index).setBackgroundColor(selectedTabColor);
            titles.get(index).setTextColor(textColor);
            currentTab = index;
            tabChangedListener.onTabChanged(index);
        }
    }

    public interface OnTabChangedListener {
        void onTabChanged(int position);
    }
}
