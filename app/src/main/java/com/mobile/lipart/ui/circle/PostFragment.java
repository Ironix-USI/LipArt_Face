package com.mobile.lipart.ui.circle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.mobile.lipart.R;
import com.mobile.lipart.ui.post.MyPostsFragment;
import com.mobile.lipart.ui.post.MyTopPostsFragment;

public class PostFragment extends Fragment {

    private static final String TAG = "CircleActivity";
    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private String mText = "";
    private DatabaseReference mDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_circle, container, false);

        mPagerAdapter = new FragmentPagerAdapter(getFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            private final Fragment[] mFragments = new Fragment[]{
                    new MyTopPostsFragment(),
                    new MyPostsFragment(),
            };
            private final String[] mFragmentNames = new String[]{
                    getString(R.string.heading_my_top_posts),
                    getString(R.string.heading_my_posts),
            };

            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };
        // Set up the ViewPager with the sections adapter.
        mViewPager = root.findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = root.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.commit();

        return root;
    }
}