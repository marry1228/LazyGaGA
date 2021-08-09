package com.aoslec.haezzo.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.aoslec.haezzo.ActivityOnDealList.OnDealTabIng;
import com.aoslec.haezzo.ActivityOnDealList.OnDealTabDone;

public class TabPagerAdapter extends FragmentStateAdapter {


    public TabPagerAdapter(FragmentManager fragmentManager,  Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                OnDealTabIng tab1Fragment = new OnDealTabIng();
                return tab1Fragment;
            case 1:
                OnDealTabDone tab2Fragment = new OnDealTabDone();
                return tab2Fragment;

            default:
                return null;

        }


    }//createFragment

    @Override
    public int getItemCount() {
        return 2;
    }//getItemCount
}//-----