package com.example.lenovo.androidzachotapplication.ui.activity;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.lenovo.androidzachotapplication.R;
import com.example.lenovo.androidzachotapplication.ui.fragment.AlarmFragment;
import com.example.lenovo.androidzachotapplication.ui.fragment.NotesFragment;
import com.example.lenovo.androidzachotapplication.ui.fragment.PlayerFagment;
import com.example.lenovo.androidzachotapplication.ui.view.TabsView;

public class MainActivity extends AppCompatActivity {

    private TabsView tabsView;
    private FragmentPagerAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        tabsView = (TabsView) findViewById(R.id.tabs_view);
        tabsView.addTab("Notes");
        tabsView.addTab("Player");
        tabsView.addTab("Alarm");
        tabsView.setOnTabChangedListener(new TabsView.OnTabChangedListener() {
            @Override
            public void onTabChanged(int position) {
                viewPager.setCurrentItem(position);
            }
        });
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new NotesFragment();
                    case 1:
                        return new PlayerFagment();
                    case 2:
                        return new AlarmFragment();
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabsView.changeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
