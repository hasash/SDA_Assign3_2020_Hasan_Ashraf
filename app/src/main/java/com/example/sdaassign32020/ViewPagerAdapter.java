package com.example.sdaassign32020;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/*
 * viewPager adapter.
 * @author Hasan Ashraf
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, Context nContext) {
        super(fm, behavior);
        context = nContext;
    }
    /**
     * Determines the default fragment that is displayed to the user
     *
     * @param position Parameter 1.
     * @return The fragment that the user navigates too.
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = new Fragment();

        //finds the tab position (note array starts at 0)
        position = position+1;

        //finds the fragment
        switch (position)
        {
            case 1:
                //code
                fragment = new WelcomeTab();
                break;
            case 2:
                //code
                fragment = new ProductList();
                break;
            case 3:
                //code
                fragment = new OrderTshirt();
                break;
        }

        return fragment;
    }
    /**
     * Count of the total number of fragmnet present in the layout
     */
    @Override
    public int getCount() {
        return 3;
    }

    /**
     * Determines the naming of the tabs
     *
     * @param position Parameter 1.
     * @return The tab title for the tab clicked by user
     */
    @Override
    public CharSequence getPageTitle(int position) {
        position = position+1;

        CharSequence tabTitle = "";

        //finds the fragment
        switch (position)
        {
            case 1:
                //code
                tabTitle = "Welcome";
                break;
            case 2:
                //code
                tabTitle = "Product List";
                break;
            case 3:
                //code
                tabTitle = "Order T Shirt";
                break;

        }

        return tabTitle;
    }
}
