package com.math4.user.mathplace;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Adapter2 extends FragmentPagerAdapter {
    private int PAGER_COUNT;
    private Fragment[] m;
    @Override
    public int getCount(){
        return this.PAGER_COUNT;
    }
    @Override
    public Fragment  getItem(int position){
        return this.m[position];
    }
    Adapter2(@NonNull FragmentManager fm,int pager_count,Fragment[] m2) {
        super(fm);
        this.PAGER_COUNT=pager_count;
        this.m=m2;

    }

}
