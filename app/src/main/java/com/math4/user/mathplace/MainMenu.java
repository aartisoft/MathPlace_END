package com.math4.user.mathplace;

import android.content.Context;
import android.content.Intent;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.math4.user.mathplace.olymp.Olymp;

import static com.math4.user.mathplace.MainActivity.searchItem;
import static com.math4.user.mathplace.MainActivity.settingsItem;

public class MainMenu extends Fragment {
    //----------------------------------------------------------------------------------------------
    static ViewPager viewPager;
    TabLayout tabLayout;
    Bookmark fragmentSend;
    String[] arrayTheme = {"школа", "огэ", "геометрия", "алгебра", "комбинаторика", "логика", "графы", "идеи"};

    public class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 4;
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            Log.e("checkcheckThemeMainProv", String.valueOf(position));
            if(position==0){
               return new SectionsMenu();
            }else if (position == 1) {
                return new Menu();
            }else if(position==2){
                return  new ShowOlymp();
            }else if(position==3){
                fragmentSend = new Bookmark();
                //                fragmentSend = new ThemeMenu(arrayTheme[position - 1]);
                return fragmentSend;
            }
            return fragmentSend;

        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Разделы";
            }else if(position==1){
                return "Меню";
            }else if (position == 2) {
                return "Уроки";
            } else {
                return "Избранное";
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    View view;

    @Override
    // Переопределяем метод onCreateView
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main_menu, container, false);
        FloatingActionButton fab=view.findViewById(R.id.fab);
        final Context context=getActivity();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Olymp.class);
                startActivity(intent);
            }
        });
        return view;
    }

    public void setAdap() {
        tabLayout = view.findViewById(R.id.sliding_tabsTheme);
        viewPager = view.findViewById(R.id.viewpagerTheme);
        MainMenu.MyAdapter adapter = new MainMenu.MyAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter); // устанавливаем адаптер
        viewPager.setCurrentItem(1); // выводим второй экран
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onStart() {
        super.onStart();
        setAdap();
        if(searchItem!=null) {
            searchItem.setVisible(true);
        }
        if(settingsItem!=null) {
            settingsItem.setVisible(false);
        }


    }
}