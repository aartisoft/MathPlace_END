package com.math4.user.mathplace;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import static com.math4.user.mathplace.MainActivity.searchItem;
import static com.math4.user.mathplace.MainActivity.settingsItem;

public class ViewPagerLikeBookmark extends Fragment {
    //----------------------------------------------------------------------------------------------
    static ViewPager viewPager;
    TabLayout tabLayout;

    public class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            Log.e("checkcheckThemeMainProv", String.valueOf(position));
            if (position == 0) {
                return new Bookmark();
            } else {
                return new Like();
            }

        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Избранные";
            } else{
                return "Понравившиеся";
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    View view;

    @Override
    // Переопределяем метод onCreateView
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main_like_bookmark, container, false);
        setAdap();
        return view;
    }

    public void setAdap() {
        tabLayout = view.findViewById(R.id.sliding_tabsLikeBookmark);
        viewPager = view.findViewById(R.id.viewpagerLikeBookmark);
        ViewPagerLikeBookmark.MyAdapter adapter = new ViewPagerLikeBookmark.MyAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter); // устанавливаем адаптер
        viewPager.setCurrentItem(0); // выводим второй экран
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(searchItem!=null) {
            searchItem.setVisible(false);
        }
        if(settingsItem!=null) {
            settingsItem.setVisible(false);
        }


    }
}