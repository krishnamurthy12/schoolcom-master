package com.krish.assignment.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.SearchView;

import com.krish.assignment.fragments.FragmentOne;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    int no_of_pages;
    SearchView.OnQueryTextListener context;
    public MyPagerAdapter(FragmentManager fm, int numofPages) {
        super(fm);
        this.no_of_pages=numofPages;
    }

    public MyPagerAdapter(FragmentManager fm, SearchView.OnQueryTextListener context) {
        super(fm);
        this.context = context;
    }


    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        FragmentOne fragmentOne = new FragmentOne();
        switch (position) {
            case 0:
                bundle.clear();
                bundle.putInt("SOURCE_POS", position);
                fragmentOne.setArguments(bundle);
                return fragmentOne;

            case 1:
                bundle.clear();
                bundle.putInt("SOURCE_POS", position);
                fragmentOne.setArguments(bundle);
                return fragmentOne;

            case 2:
                bundle.clear();
                bundle.putInt("SOURCE_POS", position);
                fragmentOne.setArguments(bundle);
                return fragmentOne;

            case 3:
                bundle.clear();
                bundle.putInt("SOURCE_POS", position);
                fragmentOne.setArguments(bundle);
                return fragmentOne;

            case 4:
                bundle.clear();
                bundle.putInt("SOURCE_POS", position);
                fragmentOne.setArguments(bundle);
                return fragmentOne;
        }
        return null;
    }

    @Override
    public int getCount() {
        return no_of_pages;
    }
}
