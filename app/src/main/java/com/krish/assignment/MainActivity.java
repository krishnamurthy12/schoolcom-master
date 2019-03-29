package com.krish.assignment;

import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.krish.assignment.adapters.MyPagerAdapter;
import com.krish.assignment.api_responses.sources.Source;
import com.krish.assignment.api_responses.sources.SourceSResponse;
import com.krish.assignment.utils.OnResponseListener;
import com.krish.assignment.utils.Utilities;
import com.krish.assignment.utils.WebServices;

import java.util.ArrayList;
import java.util.List;

import static com.krish.assignment.utils.Utilities.showSnackBar;
import static com.krish.assignment.utils.Utilities.showToast;
import static com.krish.assignment.utils.Utilities.toggleVisibility;

public class MainActivity extends AppCompatActivity implements OnResponseListener {

    TabLayout mTabLayout;
    ViewPager viewPager;
    MyPagerAdapter myPagerAdapter;

    ProgressBar mProgressBar;
   public static List<Source> sourceList=new ArrayList<>();
   boolean doublePresstoExitPressedOnce=false;

    /*@Override
    protected void onStart() {
        super.onStart();
        if(mProgressBar!=null)
        {
            if(mProgressBar.getVisibility()== View.VISIBLE)
            {
                showSnackBar(this,"wait untill the current process to finish");
            }
            else {
                callDemoAPI();
            }
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
    }

    private void initializeViews() {

        mTabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        mProgressBar=findViewById(R.id.vP_ah_progressbar);

        callGetSourcesAPI();

    }


    public void callGetSourcesAPI() {
        if (Utilities.isConnectedToInternet(this)) {
            toggleVisibility(true, mProgressBar);
            WebServices<SourceSResponse> webServices = new WebServices<SourceSResponse>(this);
            webServices.callgetSources(Utilities.getBaseURL(), WebServices.ApiType.sources);

        } else {
            showToast(this, getResources().getString(R.string.err_msg_nointernet));

        }

    }

    @Override
    public void onResponse(Object response, WebServices.ApiType URL, boolean isSucces, int code) {
        switch (URL)
        {
            case sources:
                toggleVisibility(false, mProgressBar);
                if (isSucces) {
                    SourceSResponse sourceSResponse = (SourceSResponse) response;
                    if (sourceSResponse != null) {
                        if (sourceSResponse.getSources() != null) {
                            //Success case
                            sourceList = sourceSResponse.getSources();
                            initializeTabLayout(sourceList);

                        } else {
                            //List is null
                            showSnackBar(this, "List is empty");
                        }
                    } else {
                        //Null Response
                        showSnackBar(this, "Something went wrong please try again");
                    }

                } else {
                    //API Call Failed
                    showSnackBar(this, "API call failed");
                }

                break;
        }
    }

    private void initializeTabLayout(List<Source> mList) {

        mTabLayout.addTab(mTabLayout.newTab().setText(mList.get(0).getName()));
        mTabLayout.addTab(mTabLayout.newTab().setText(mList.get(1).getName()));
        mTabLayout.addTab(mTabLayout.newTab().setText(mList.get(2).getName()));
        mTabLayout.addTab(mTabLayout.newTab().setText(mList.get(3).getName()));
        mTabLayout.addTab(mTabLayout.newTab().setText(mList.get(4).getName()));

        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());

        viewPager.setAdapter(myPagerAdapter);
        viewPager.setOffscreenPageLimit(5);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
/*
    @Override
    protected void onStop() {
        super.onStop();
        mTabLayout.removeAllTabs();
    }*/

    @Override
    public void onBackPressed() {

        if(doublePresstoExitPressedOnce)
        {
            super.onBackPressed();
            finishAffinity();
        }
        showSnackBar(this, getResources().getString(R.string.back_press_to_exit));
        doublePresstoExitPressedOnce=true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doublePresstoExitPressedOnce=false;
            }
        },2000);
    }
}
