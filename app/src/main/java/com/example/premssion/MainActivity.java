package com.example.premssion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TabLayout main_TAB_tab;
    private ViewPager main_View;
    private int[] tabIcons = {
            R.drawable.pattern,
            R.drawable.point,
            R.drawable.password
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        ArrayList<String> list = new ArrayList<>();

        initArray(list);
        main_TAB_tab.setupWithViewPager(main_View);
        preapareViewPager(main_View,list);
    }
    private void initArray( ArrayList<String> list) {
        list.add("Basic");
        list.add("Advance");
        list.add("Pro");

    }

    private void findViews() {

        main_TAB_tab=findViewById(R.id.main_TAB_tab) ;
        main_View   =findViewById(R.id.main_View) ;
    }
    private void preapareViewPager(ViewPager main_view, ArrayList<String> list) {

        MainAdapter adapter = new MainAdapter(getSupportFragmentManager());
            MainFragment mainFragment = new MainFragment();
            adapter.addFragment(new BasicFragment(),"Basic");
            adapter.addFragment(new MainFragment(),"PRO");
            adapter.addFragment(new MainFragment(),"ADVANCE");
       /* for (int i = 0; i < list.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putString("title",list.get(i));
            mainFragment.setArguments(bundle);
            adapter.addFragment(mainFragment,list.get(i));
            mainFragment = new MainFragment();
        }*/
        main_view.setAdapter(adapter);
    }
    private class MainAdapter extends FragmentPagerAdapter{

        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

        ArrayList<String> stringArrayList = new ArrayList<>();
        int[] imageList= {R.drawable.point,R.drawable.pattern,R.drawable.password};

        public void addFragment(Fragment fragment,String s)
        {
            fragmentArrayList.add(fragment);
            stringArrayList.add(s);
        }

        public MainAdapter(FragmentManager supportFragment)
        {
            super(supportFragment);

        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            //return fragment position
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),imageList[position]);

            drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
            SpannableString spannableString = new SpannableString("    " + stringArrayList.get(position));
            ImageSpan imageSpan = new ImageSpan(drawable,ImageSpan.ALIGN_BOTTOM);
            spannableString.setSpan(imageSpan,0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            return spannableString;
        }
    }


}